package org.technologybrewery.fermenter.mda;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.technologybrewery.fermenter.mda.element.ExpandedFamily;
import org.technologybrewery.fermenter.mda.element.ExpandedProfile;
import org.technologybrewery.fermenter.mda.element.Family;
import org.technologybrewery.fermenter.mda.element.Profile;
import org.technologybrewery.fermenter.mda.element.Target;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.Generator;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceUrl;
import org.technologybrewery.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.technologybrewery.fermenter.mda.notification.NotificationService;
import org.technologybrewery.fermenter.mda.reporting.StatisticsService;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Executes the Fermenter MDA process.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class GenerateSourcesMojo extends AbstractMojo {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(GenerateSourcesMojo.class);

    private Map<String, ExpandedProfile> profiles = new HashMap<>();

    private Map<String, Target> targets = new HashMap<>();

    private Map<String, ExpandedFamily> families = new HashMap<>();

    @Inject
    private StatisticsService statisticsService;

    @Inject
    private NotificationService notificationService;

    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${plugin}", readonly = true)
    private PluginDescriptor plugin;

    @Parameter(required = true)
    private String profile;

    @Parameter
    private List<String> metadataDependencies;

    @Parameter
    private String basePackage;

    /**
     * Captures the target programming language in which source code artifacts will be generated. This
     * configuration drives the automatic configuration of {@link #mainSourceRoot}, {@link #generatedSourceRoot},
     * {@link #generatedTestSourceRoot}, and other language specific structural elements.
     */
    @Parameter(required = true, defaultValue = "java")
    private String language;

    private String targetsFileLocation = "targets.json";
    private String profilesFileLocation = "profiles.json";
    private String familiesFileLocation = "families.json";

    /**
     * Represents the artifactIds that will be targeted for generation if the
     * metadataContext equals 'targeted'.
     */
    @Parameter(required = false)
    private List<String> targetModelInstances;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/main")
    private File mainSourceRoot;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/generated")
    private File generatedSourceRoot;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/test")
    private File testSourceRoot;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/generated-test")
    private File generatedTestSourceRoot;

    /**
     * Local path from which metadata will be loaded. Defaults to "{@link #mainSourceRoot}/resources"
     * via {@link #getLocalMetadataRoot()}.
     */
    @Parameter
    private File localMetadataRoot;

    @Parameter(required = true, defaultValue = "${project.basedir}/src/main/resources/types.json")
    private File localTypes;

    @Parameter(required = true, defaultValue = "org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository")
    private String metadataRepositoryImpl;

    /**
     * List of general properties to pass to the {@link GenerationContext}.
     */
    @Parameter
    private Map<String, String> propertyVariables;

    private VelocityEngine engine;

    @Parameter(property = "session", required = true, readonly = true)
    protected MavenSession session;

    /**
     * Creates a {@link GenerateSourcesHelper.LoggerDelegate} implementation for use with
     * {@link GenerateSourcesHelper} that delegates to the {@link Log} that has
     * been injected into this mojo.
     */
    protected GenerateSourcesHelper.LoggerDelegate mavenLoggerDelegate = new GenerateSourcesHelper.LoggerDelegate() {

        @Override
        public void log(LogLevel level, String message) {
            Log log = getLog();
            switch (level) {
                case TRACE:
                case DEBUG:
                    log.debug(message);
                    break;
                case INFO:
                    log.info(message);
                    break;
                case WARN:
                    log.warn(message);
                    break;
                case ERROR:
                    log.error(message);
                    break;
                default:
                    log.info(message);
                    break;
            }
        }
    };

    @Override
    public void execute() throws MojoExecutionException {
        GenerateSourcesHelper.suppressKrauseningWarnings();

        try {
            setup();
            GenerateSourcesHelper.performSourceGeneration(profile, profiles, this::createGenerationContext,
                this::handleInvalidProfile, mavenLoggerDelegate, project.getBasedir());
        } catch (Exception e) {
            String message = "Error while performing source generation";
            // NB logging and re-throwing isn't usually a best practice as it
            // can result in duplicative error logging and clutter, but here it
            // can be helpful in providing additional context about an error
            // without needing to re-run Maven with -e or -X turned on
            getLog().error(message, e);
            throw new MojoExecutionException(message, e);

        } finally {
            GenerateSourcesHelper.cleanUp();

        }

        // store notifications in the target directory between plugin invocations so they can be output
        // at the end of the build:
        notificationService.recordNotifications(getProject());

    }

    /**
     * Performs all setup activities required to load and validate metamodels
     * prior to code generation, including loading generation targets and
     * profiles, automatically adding src/generated/java to the project's list
     * of source directories, and loading/validating metamodels into the
     * appropriate {@link ModelInstanceRepository}.
     *
     * @throws MojoExecutionException any unexpected error occurs during metamodel loading and
     *                                validation.
     */
    private void setup() throws MojoExecutionException {
        if (metadataDependencies == null) {
            metadataDependencies = new ArrayList<>();
        }

        updateMojoConfigsBasedOnLanguage();
        validateMojoConfigs();

        loadTargets();
        loadProfiles();
        loadFamilies();
        TypeManager.getInstance().loadLocalTypes(localTypes);

        if (isGeneratingJavaProject()) {
            try {
                project.addCompileSourceRoot(getJavaCompilePathForGeneratedSource());
            } catch (IOException e) {
                throw new MojoExecutionException("Could not add generated Java source root to project compilation path list", e);
            }
        }

        try {
            ModelRepositoryConfiguration config = createMetadataConfiguration();
            ModelInstanceRepository newRepository = GenerateSourcesHelper.loadMetamodelRepository(config,
                metadataRepositoryImpl, mavenLoggerDelegate);

            GenerateSourcesHelper.validateMetamodelRepository(newRepository, mavenLoggerDelegate);
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | InvocationTargetException e) {
            throw new MojoExecutionException("Could not successfully load metamodel repository", e);
        }

        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }


    /**
     * Scans the classpath for any families.json files and loads all defined
     * {@link Family} configurations.
     *
     * @throws MojoExecutionException
     */
    public void loadFamilies() throws MojoExecutionException {
        Enumeration<URL> familyEnumeration = null;
        try {
            familyEnumeration = getClass().getClassLoader().getResources(familiesFileLocation);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find families", ioe);
        }

        URL familiesResource;
        while (familyEnumeration.hasMoreElements()) {
            familiesResource = familyEnumeration.nextElement();
            getLog().info(String.format("Loading families from: %s", familiesResource.toString()));

            try (InputStream familiesStream = familiesResource.openStream()) {
                families = GenerateSourcesHelper.loadFamilies(familiesStream, families);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to parse " + familiesFileLocation, e);
            }
        }

        for (ExpandedFamily f : families.values()) {
            f.dereference(families, profiles);
        }

    }

    /**
     * Scans the classpath for any targets.json files and loads all defined
     * {@link Target} configurations.
     *
     * @throws MojoExecutionException
     */
    public void loadTargets() throws MojoExecutionException {
        Enumeration<URL> targetEnumeration = null;
        try {
            targetEnumeration = getClass().getClassLoader().getResources(targetsFileLocation);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find targets", ioe);
        }

        URL targetsResource;
        while (targetEnumeration.hasMoreElements()) {
            targetsResource = targetEnumeration.nextElement();
            getLog().info(String.format("Loading targets from: %s", targetsResource.toString()));

            try (InputStream targetsStream = targetsResource.openStream()) {
                targets = GenerateSourcesHelper.loadTargets(targetsStream, targets);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to parse " + targetsFileLocation, e);
            }
        }
    }

    /**
     * Scans the classpath for any profiles.json files and loads all defined
     * {@link Profile} configurations.
     *
     * @throws MojoExecutionException
     */
    public void loadProfiles() throws MojoExecutionException {
        Enumeration<URL> profileEnumeration = null;
        try {
            profileEnumeration = getClass().getClassLoader().getResources(profilesFileLocation);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find profiles", ioe);
        }

        URL profilesResource;
        while (profileEnumeration.hasMoreElements()) {
            profilesResource = profileEnumeration.nextElement();
            getLog().info(String.format("Loading profiles from: %s", profilesResource.toString()));

            try (InputStream profilesStream = profilesResource.openStream()) {
                profiles = GenerateSourcesHelper.loadProfiles(profilesStream, profiles);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to parse " + profilesFileLocation, e);
            }
        }

        for (ExpandedProfile p : profiles.values()) {
            p.dereference(profiles, targets);
        }
    }

    /**
     * Creates a {@link ModelRepositoryConfiguration} utilized by the
     * appropriate {@link ModelInstanceRepository} for metamodel loading. This
     * method primarily extracts the metamodel dependencies specified in the
     * &lt;metadataDependencies&gt; and &lt;targetModelInstances&gt;
     * configurations and enables them to be appropriately referenced through
     * the created {@link ModelRepositoryConfiguration}.
     *
     * @return appropriately configured {@link ModelRepositoryConfiguration}
     * that may be used for metamodel processing.
     * @throws MalformedURLException
     */
    private ModelRepositoryConfiguration createMetadataConfiguration() throws MalformedURLException {
        ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
        config.setArtifactId(project.getArtifactId());
        config.setBasePackage(basePackage);

        List<String> targetedArtifactIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(targetModelInstances)) {
            targetedArtifactIds.add(project.getArtifactId());

        } else {
            targetedArtifactIds = targetModelInstances;

        }

        if ((targetedArtifactIds.size() > 1) || (!targetedArtifactIds.contains(project.getArtifactId()))) {
            LOG.info("Generation targets (" + targetedArtifactIds.size()
                + ") are different from project's local metadata (" + targetedArtifactIds.toString() + ")");
        }

        config.setTargetModelInstances(targetedArtifactIds);
        Map<String, ModelInstanceUrl> metadataUrls = config.getMetamodelInstanceLocations();
        String projectUrl = getLocalMetadataRoot().toURI().toURL().toString();
        metadataUrls.put(project.getArtifactId(), new ModelInstanceUrl(project.getArtifactId(), projectUrl));
        PackageManager.addMapping(project.getArtifactId(), basePackage);

        if (metadataDependencies != null) {
            metadataDependencies.add(project.getArtifactId());

            List<Artifact> artifacts = plugin.getArtifacts();
            for (Artifact a : artifacts) {
                if (metadataDependencies.contains(a.getArtifactId())) {
                    URL url = a.getFile().toURI().toURL();
                    metadataUrls.put(a.getArtifactId(), new ModelInstanceUrl(a.getArtifactId(), url.toString()));
                    PackageManager.addMapping(a.getArtifactId(), url, a.getGroupId());
                    LOG.info("Adding metadataDependency to current set of metadata: " + a.getArtifactId());
                }
            }

        }
        return config;
    }

    /**
     * Helper method that automatically updates appropriate Mojo configurations based on the specified target language,
     * which is configured via {@link #language}. Specifically, this method overrides the various file locations in
     * which generated code is placed to align with the expected file structures required by each language associated
     * DevOps and build tooling.
     */
    protected void updateMojoConfigsBasedOnLanguage() {
        if (isGeneratingPythonProject()) {
            String pythonPackageFolder = getPythonPackageFolderForProject();
            this.mainSourceRoot = new File(project.getBasedir(), String.format("src/%s", pythonPackageFolder));
            this.generatedSourceRoot = new File(project.getBasedir(), String.format("src/%s/generated", pythonPackageFolder));
            this.testSourceRoot = new File(project.getBasedir(), "tests");
            this.generatedTestSourceRoot = new File(project.getBasedir(), "tests");
            this.localTypes = new File(mainSourceRoot, "resources/types.json");
            if (StringUtils.isEmpty(this.basePackage)) {
                this.basePackage = pythonPackageFolder;
            }
        }
    }

    /**
     * Performs complex validation on user-provided configurations to this Mojo, outside of metamodel validation.
     *
     * @throws MojoExecutionException unrecoverable validation error was detected.
     */
    protected void validateMojoConfigs() throws MojoExecutionException {
        MessageTracker messageTracker = MessageTracker.getInstance();
        if (isGeneratingJavaProject()) {
            if (StringUtils.isEmpty(basePackage)) {
                messageTracker.addErrorMessage("<basePackage> must be specified for Java-based projects");
            }
        }

        if (messageTracker.hasErrors()) {
            messageTracker.emitMessages(mavenLoggerDelegate);
            throw new MojoExecutionException("Provided configuration was invalid!");
        }
    }

    /**
     * Handles the specification of an invalid or non-existent generation
     * profile by providing diagnostic error logging and returning the
     * appropriate exception to throw.
     *
     * @param targetProfile invalid profile specified in the fermenter-mda plugin
     *                      declaration.
     * @param allProfiles   all valid {@link Profile}s based on the fermenter-mda plugin
     *                      configuration.
     * @return a {@link MojoExecutionException} to throw and halt the build.
     */
    private Exception handleInvalidProfile(String targetProfile, Collection<ExpandedProfile> allProfiles) {
        StringBuilder sb = new StringBuilder();
        Set<ExpandedProfile> orderedProfiles = new TreeSet<>(allProfiles);
        for (ExpandedProfile profileValue : orderedProfiles) {
            sb.append("\t- ").append(profileValue.getName()).append("\n");
        }

        getLog().error("\n<plugin>\n"
            + "\t<groupId>org.technologybrewery.fermenter</groupId>\n"
            + "\t<artifactId>fermenter-mda</artifactId>\n"
            + "\t...\n"
            + "\t<configuration>\n"
            + "\t\t<profile>" + targetProfile + "</profile>   <-----------  INVALID PROFILE!\n"
            + "\t\t...\n"
            + "\t</configuration>\n"
            + "</plugin>\n"
            + "Profile '" + targetProfile + "' is invalid.  Please choose one of the following valid profiles:\n" + sb.toString());

        return new MojoExecutionException("Invalid profile specified: '" + targetProfile + "'");
    }

    /**
     * Creates a new {@link GenerationContext} object based on the given
     * {@link Target} which captures key configuration details needed to
     * generate the source file(s) modeled by the given {@link Target} and it's
     * {@link Generator}.
     *
     * @param target generation {@link Target} being processed.
     * @return {@link GenerationContext} that can be provided to the given
     * {@link Target}'s {@link Generator} to execute code generation.
     */
    protected GenerationContext createGenerationContext(Target target) {
        GenerationContext context = new GenerationContext(target);
        context.setStatisticsService(statisticsService);
        context.setBasePackage(basePackage);
        context.setProjectDirectory(project.getBasedir());
        context.setGeneratedSourceDirectory(generatedSourceRoot);
        context.setMainSourceDirectory(mainSourceRoot);
        context.setTestSourceDirectory(testSourceRoot);
        context.setGeneratedTestSourceDirectory(generatedTestSourceRoot);
        context.setEngine(engine);
        context.setGroupId(project.getGroupId());
        context.setArtifactId(project.getArtifactId());
        context.setVersion(project.getVersion());
        context.setDescriptiveName(project.getName());
        if (project.getScm() != null) {
            context.setScmUrl(project.getScm().getUrl());
        }
        context.setPropertyVariables(propertyVariables);
        context.setExecutionRootDirectory(new File (session.getExecutionRootDirectory()));

        String rootArtifactId = getRootArtifactId();
        context.setRootArtifactId(rootArtifactId);

        return context;
    }

    /**
     * Boolean value indicating whether this Mojo is being invoked to generate a Java-based project.
     *
     * @return
     */
    protected boolean isGeneratingJavaProject() {
        return "java".equalsIgnoreCase(this.language) && !"habushu".equals(this.getProject().getPackaging());
    }

    /**
     * Boolean value indicating whether this Mojo is being invoked to generate a Python-based project.
     * This may be explicitly specified via the {@link #language} configuration or if it is detected
     * that the project uses a recent version of the Habushu Maven plugin.
     *
     * @return
     */
    protected boolean isGeneratingPythonProject() {
        return "python".equalsIgnoreCase(this.language)
            || ("habushu".equals(this.project.getPackaging()) && !isLegacyHabushuProject());
    }

    /**
     * Boolean value indicating whether this Mojo is being invoked on a project that is a legacy
     * Habushu project.  More specifically, the determination is based on if a version of the
     * {@code org.technologybrewery.habushu:habushu-maven-plugin} with a version less than {@code 2.0.0}
     * is included in the POM's {@code <build>}.
     *
     * @return
     */
    protected boolean isLegacyHabushuProject() {
        Plugin habushuMavenPlugin = this.project.getPlugin("org.bitbucket.cpointe.habushu:habushu-maven-plugin");
        if (habushuMavenPlugin != null) {
            ComparableVersion habushuVersion = new ComparableVersion(habushuMavenPlugin.getVersion());
            return habushuVersion.compareTo(new ComparableVersion("2.0.0")) < 0;
        }
        return false;
    }

    /**
     * Gets the normalized package folder name of the relevant Python project being generated that aligns with
     * PEP-8 naming conventions (dashes may be in published package names, but in the actual package folder hierarchy,
     * dashes should be replaced with underscores).
     *
     * @return
     */
    protected String getPythonPackageFolderForProject() {
        return StringUtils.replace(this.getProject().getArtifactId(), "-", "_");
    }

    /**
     * Retrieves the compile path at which generated Java source files will be placed. <br>
     *
     * <b>Pre-condition:</b> The target project being generated is Java-based
     *
     * @return
     * @throws IOException
     */
    protected String getJavaCompilePathForGeneratedSource() throws IOException {
        return new File(this.generatedSourceRoot, "java").getCanonicalPath();
    }

    /**
     * Retrieves the location within this project from which local metadata definitions will be loaded.
     * If {@link #localMetadataRoot} is not configured, this will default to "{@link #mainSourceRoot}/resources"
     * (i.e. {@code src/main/resources}).
     *
     * @return
     */
    protected File getLocalMetadataRoot() {
        return localMetadataRoot != null ? localMetadataRoot : new File(mainSourceRoot, "resources");
    }

    public Map<String, ExpandedFamily> getFamilies() {
        return families;
    }

    public Map<String, ExpandedProfile> getProfiles() {
        return profiles;
    }

    public Map<String, Target> getTargets() {
        return targets;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public Map<String, String> getPropertyVariables() {
        return propertyVariables;
    }

    public void setPropertyVariables(Map<String, String> propertyVariables) {
        this.propertyVariables = propertyVariables;
    }

    public String getTargetsFileLocation() {
        return targetsFileLocation;
    }

    public void setTargetsFileLocation(String targetsFileLocation) {
        this.targetsFileLocation = targetsFileLocation;
    }

    public String getProfilesFileLocation() {
        return profilesFileLocation;
    }

    public void setProfilesFileLocation(String profilesFileLocation) {
        this.profilesFileLocation = profilesFileLocation;
    }

    public MavenProject getProject() {
        return project;
    }

    protected File getMainSourceRoot() {
        return mainSourceRoot;
    }

    protected File getGeneratedSourceRoot() {
        return generatedSourceRoot;
    }

    public String getRootArtifactId() {
        MavenProject topLevelProject = session.getTopLevelProject();
        String rootArtifactId = topLevelProject.getArtifactId();
        return rootArtifactId;
    }
}

package org.bitbucket.fermenter.mda;

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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.Artifact;
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
import org.bitbucket.fermenter.mda.GenerateSourcesHelper.LoggerDelegate;
import org.bitbucket.fermenter.mda.element.ExpandedProfile;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;

/**
 * Executes the Fermenter MDA process.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class GenerateSourcesMojo extends AbstractMojo {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(GenerateSourcesMojo.class);

    private Map<String, ExpandedProfile> profiles = new HashMap<>();

    private Map<String, Target> targets = new HashMap<>();

    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${plugin}", readonly = true)
    private PluginDescriptor plugin;

    @Parameter(required = true)
    private String profile;

    @Parameter
    private List<String> metadataDependencies;

    @Parameter(required = true)
    private String basePackage;

    /**
     * Represents the artifactIds that will be targeted for generation if the
     * metadataContext equals 'targeted'.
     */
    @Parameter(required = false)
    private List<String> targetModelInstances;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/generated/java")
    private String generatedCompileSourceRoot;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/main")
    private File mainSourceRoot;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/generated")
    private File generatedSourceRoot;
    
    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/test")
    private File testSourceRoot;
    
    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/generated-test")
    private File generatedTestSourceRoot;       

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}")
    private File projectRoot;
    
    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/main/resources/types.json")
    private File localTypes;    

    @Parameter(required = true, readonly = true, defaultValue = "org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository")
    private String metadataRepositoryImpl;
    
    /**
     * List of general properties to pass to the {@link GenerationContext}.
     */
    @Parameter
    private Map<String, String> propertyVariables;

    private VelocityEngine engine;

    /**
     * Creates a {@link LoggerDelegate} implementation for use with
     * {@link GenerateSourcesHelper} that delegates to the {@link Log} that has
     * been injected into this mojo.
     */
    protected LoggerDelegate mavenLoggerDelegate = new LoggerDelegate() {

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
                    this::handleInvalidProfile, mavenLoggerDelegate);
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
        

    }

    /**
     * Performs all setup activities required to load and validate metamodels
     * prior to code generation, including loading generation targets and
     * profiles, automatically adding src/generated/java to the project's list
     * of source directories, and loading/validating metamodels into the
     * appropriate {@link ModelInstaceRepository}.
     * 
     * @throws MojoExecutionException
     *             any unexpected error occurs during metamodel loading and
     *             validation.
     */
    private void setup() throws MojoExecutionException {
        if (metadataDependencies == null) {
            metadataDependencies = new ArrayList<>();
        }

        loadTargets();
        loadProfiles();
        TypeManager.getInstance().loadLocalTypes(localTypes);

        project.addCompileSourceRoot(generatedCompileSourceRoot);

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
     * Scans the classpath for any targets.json files and loads all defined
     * {@link Target} configurations.
     * 
     * @throws MojoExecutionException
     */
    private void loadTargets() throws MojoExecutionException {
        Enumeration<URL> targetEnumeration = null;
        try {
            targetEnumeration = getClass().getClassLoader().getResources("targets.json");
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
                throw new MojoExecutionException("Unable to parse targets.json", e);
            }
        }
    }

    /**
     * Scans the classpath for any profiles.json files and loads all defined
     * {@link Profile} configurations.
     * 
     * @throws MojoExecutionException
     */
    private void loadProfiles() throws MojoExecutionException {
        Enumeration<URL> profileEnumeration = null;
        try {
            profileEnumeration = getClass().getClassLoader().getResources("profiles.json");
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
                throw new MojoExecutionException("Unable to parse profiles.json", e);
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
     *         that may be used for metamodel processing.
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
        String projectUrl = new File(mainSourceRoot, "resources").toURI().toURL().toString();
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
     * Handles the specification of an invalid or non-existent generation
     * profile by providing diagnostic error logging and returning the
     * appropriate exception to throw.
     * 
     * @param targetProfile
     *            invalid profile specified in the fermenter-mda plugin
     *            declaration.
     * @param allProfiles
     *            all valid {@link Profile}s based on the fermenter-mda plugin
     *            configuration.
     * @return a {@link MojoExecutionException} to throw and halt the build.
     */
    private Exception handleInvalidProfile(String targetProfile, Collection<ExpandedProfile> allProfiles) {
        StringBuilder sb = new StringBuilder();
        for (ExpandedProfile profileValue : allProfiles) {
            sb.append("\t- ").append(profileValue.getName()).append("\n");
        }
        
        getLog().error("\n<plugin>\n" 
                        + "\t<groupId>org.bitbucket.askllc.fermenter</groupId>\n"
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
     * @param target
     *            generation {@link Target} being processed.
     * @return {@link GenerationContext} that can be provided to the given
     *         {@link Target}'s {@link Generator} to execute code generation.
     */
    private GenerationContext createGenerationContext(Target target) {
        GenerationContext context = new GenerationContext(target);
        context.setBasePackage(basePackage);
        context.setProjectDirectory(projectRoot);
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
        
        return context;
    }
   
}

package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import org.bitbucket.fermenter.mda.generator.Generator;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.LegacyMetadataConverter;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;

/**
 * Executes the Fermenter MDA process.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
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

    @Parameter(required = true, readonly = true, defaultValue = "org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository")
    private String metadataRepositoryImpl;

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

    public void execute() throws MojoExecutionException {
        GenerateSourcesHelper.suppressKrauseningWarnings();

        try {
            setup();
        } catch (Exception ex) {
            throw new MojoExecutionException("Error setting up generator", ex);
        }

        generateSources();

    }

    private void setup() throws Exception {
        if (metadataDependencies == null) {
            metadataDependencies = new ArrayList<>();
        }

        loadTargets();
        loadProfiles();

        project.addCompileSourceRoot(generatedCompileSourceRoot);

        try {
            initializeMetadata();

            engine = new VelocityEngine();
            engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
            engine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
            engine.init();
        } catch (Exception ex) {
            String errMsg = "Unable to setup code generator";
            getLog().error(errMsg, ex);
            throw new MojoExecutionException(errMsg, ex);
        }
    }

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

    private void initializeMetadata() throws Exception {
        ModelRepositoryConfiguration config = createMetadataConfiguration();

        // first load the legacy repository:
        ModelInstanceRepository legacyRepository = GenerateSourcesHelper.loadLegacyMetadataRepository(config,
                mavenLoggerDelegate);

        // This is a stand-in to prevent NPEs for some optional functionality
        // that we want in the end product,
        // but doesn't matter for the conversion:
        ModelInstanceRepositoryManager.setRepository(new DefaultModelInstanceRepository(config));

        LegacyMetadataConverter converter = new LegacyMetadataConverter();
        converter.convert(project.getArtifactId(), basePackage, mainSourceRoot);

        // then load the new repository:
        ModelInstanceRepository newRepository = GenerateSourcesHelper.loadMetadataRepository(config,
                metadataRepositoryImpl, mavenLoggerDelegate);

        long start = System.currentTimeMillis();
        LOG.info("START: validating legacy and new metadata repository implementation...");

        legacyRepository.validate();
        newRepository.validate();

        long stop = System.currentTimeMillis();
        LOG.info("COMPLETE: validation of legacy and new metadata repository in " + (stop - start) + "ms");

    }

    private ModelRepositoryConfiguration createMetadataConfiguration() throws MalformedURLException {
        ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
        config.setCurrentApplicationName(project.getArtifactId());
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
                    PackageManager.addMapping(a.getArtifactId(), url);
                    LOG.info("Adding metadataDependency to current set of metadata: " + a.getArtifactId());
                }
            }

        }
        return config;
    }

    private void generateSources() throws MojoExecutionException {
        long start = System.currentTimeMillis();
        ExpandedProfile p = profiles.get(profile);

        if (p == null) {
            StringBuilder sb = new StringBuilder();
            for (ExpandedProfile profileValue : profiles.values()) {
                sb.append("\t- ").append(profileValue.getName()).append("\n");
            }
            getLog().error("<plugin>\n" + "\t<groupId>org.bitbucket.askllc.fermenter</groupId>\n"
                    + "\t<artifactId>fermenter-mda</artifactId>\n" + "\t...\n" + "\t<configuration>\n" + "\t\t<profile>"
                    + profile + "</profile>   <-----------  INVALID PROFILE!\n" + "\t\t...\n" + "Profile '" + profile
                    + "' is invalid.  Please choose one of the following valid profiles:\n" + sb.toString());

            throw new MojoExecutionException("Invalid profile specified: '" + profile + "'");
        } else {
            getLog().info("Generating code for profile '" + p.getName() + "'");

        }

        // For each target, instantiate a generator and call generate
        for (Target t : p.getTargets()) {
            getLog().debug("\tExecuting target '" + t.getName() + "'");

            GenerationContext context = new GenerationContext(t);
            context.setBasePackage(basePackage);
            context.setGeneratedSourceDirectory(generatedSourceRoot);
            context.setMainSourceDirectory(mainSourceRoot);
            context.setEngine(engine);
            context.setGroupId(project.getGroupId());
            context.setArtifactId(project.getArtifactId());
            context.setVersion(project.getVersion());

            try {
                Class<?> clazz = Class.forName(t.getGenerator());
                Generator generator = (Generator) clazz.newInstance();
                generator.setMetadataContext(t.getMetadataContext());
                generator.generate(context);
            } catch (Exception ex) {
                throw new MojoExecutionException("Error while generating", ex);
            }
        }

        if (LOG.isInfoEnabled()) {
            long stop = System.currentTimeMillis();
            LOG.info("Generation completed in " + (stop - start) + "ms");
        }
    }

}

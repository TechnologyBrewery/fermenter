package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
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
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.bitbucket.fermenter.mda.element.ExpandedProfile;
import org.bitbucket.fermenter.mda.element.Profile;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.generator.Generator;
import org.bitbucket.fermenter.mda.metamodel.LegacyMetadataConverter;
import org.bitbucket.fermenter.mda.metamodel.MetadataUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Executes the Fermenter MDA process.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateSourcesMojo extends AbstractMojo {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(GenerateSourcesMojo.class);

    private final Map<String, ExpandedProfile> profiles = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Target> targets = new HashMap<>();

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
     * Represents the artifactIds that will be targeted for generation if the metadataContext equals 'targeted'.
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
    private String metadataRespositoryImpl;

    private VelocityEngine engine;

    private MessageTracker messageTracker = MessageTracker.getInstance();

    public void execute() throws MojoExecutionException {
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
            Properties props = new Properties();
            String className = ClasspathResourceLoader.class.getName();
            props.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class", className);
            props.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
            engine.init(props);
        } catch (Exception ex) {
            String errMsg = "Unable to setup code generator";
            getLog().error(errMsg, ex);
            throw new MojoExecutionException(errMsg, ex);
        }
    }

    private void loadTargets() throws MojoExecutionException {
        InputStream stream = null;

        URL resource = null;
        Enumeration<URL> targetEnumeration = null;
        try {
            targetEnumeration = getClass().getClassLoader().getResources("targets.json");
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find targets", ioe);
        }

        while (targetEnumeration.hasMoreElements()) {
            resource = targetEnumeration.nextElement();
            getLog().info("Loading targets from: " + resource.toString());
            try {
                stream = resource.openStream();
                List<Target> loadedTargets = objectMapper.readValue(stream, new TypeReference<List<Target>>() {
                });
                for (Target t : loadedTargets) {
                    targets.put(t.getName(), t);
                }

            } catch (IOException e) {
                throw new MojoExecutionException("Unable to parse target: " + resource.toString(), e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    private void loadProfiles() throws MojoExecutionException {
        InputStream stream = null;

        URL resource = null;
        Enumeration<URL> profileEnumeration = null;
        try {
            profileEnumeration = getClass().getClassLoader().getResources("profiles.json");
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find profiles", ioe);
        }

        while (profileEnumeration.hasMoreElements()) {
            resource = profileEnumeration.nextElement();
            getLog().info("Loading profiles from: " + resource.toString());
            try {
                stream = resource.openStream();
                List<Profile> loadedProfiles = objectMapper.readValue(stream, new TypeReference<List<Profile>>() {
                });
                for (Profile p : loadedProfiles) {
                    profiles.put(p.getName(), new ExpandedProfile(p));
                }

            } catch (IOException e) {
                throw new MojoExecutionException("Unable to parse profile: " + resource.toString(), e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }

        for (ExpandedProfile p : profiles.values()) {
            p.dereference(profiles, targets);
        }
    }

    private void initializeMetadata() throws Exception {
        ModelRepositoryConfiguration config = createMetadataConfiguration();

        // first load the legacy repository:
        ModelInstanceRepository legacyRepository = loadMetadataRepository(config, true);

        LegacyMetadataConverter converter = new LegacyMetadataConverter();
        converter.convert(project.getArtifactId(), basePackage, mainSourceRoot);

        // then load the new repository:
        ModelInstanceRepository newRepository = loadMetadataRepository(config, false);

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
        Map<String, MetadataUrl> metadataUrls = config.getMetamodelInstanceLocations();
        String projectUrl = new File(mainSourceRoot, "resources").toURI().toURL().toString();
        metadataUrls.put(project.getArtifactId(), new MetadataUrl(project.getArtifactId(), projectUrl));
        PackageManager.addMapping(project.getArtifactId(), basePackage);

        if (metadataDependencies != null) {
            metadataDependencies.add(project.getArtifactId());
            StringBuilder buff = new StringBuilder();
            for (Iterator<String> i = metadataDependencies.iterator(); i.hasNext();) {
                buff.append(i.next());
                if (i.hasNext()) {
                    buff.append(";");
                }
            }

            List<Artifact> artifacts = plugin.getArtifacts();
            for (Artifact a : artifacts) {
                if (metadataDependencies.contains(a.getArtifactId())) {
                    URL url = a.getFile().toURI().toURL();
                    metadataUrls.put(a.getArtifactId(), new MetadataUrl(a.getArtifactId(), url.toString()));
                    PackageManager.addMapping(a.getArtifactId(), url);
                    LOG.info("Adding metadataDependency to current set of metadata: " + a.getArtifactId());
                }
            }

        }
        return config;
    }

    private ModelInstanceRepository loadMetadataRepository(ModelRepositoryConfiguration config, boolean isLegacy)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {

        String repositoryType = isLegacy ? "**LEGACY** " : "";
        String repositoryImpl = isLegacy ? "org.bitbucket.fermenter.mda.metadata.MetadataRepository"
                : metadataRespositoryImpl;

        long start = System.currentTimeMillis();
        LOG.info("START: loading " + repositoryType + "metadata repository implementation: " + repositoryImpl + "...");

        ModelInstanceRepository repository;
        Class<?> repoImplClass = Class.forName(repositoryImpl);
        Class<?>[] constructorParamTypes = { ModelRepositoryConfiguration.class };
        Constructor<?> constructor = repoImplClass.getConstructor(constructorParamTypes);
        Object[] params = { config };
        repository = (ModelInstanceRepository) constructor.newInstance(params);

        ModelInstanceRepositoryManager.setRepository(repository);
        repository.load();

        // TODO: move validation back here once the legacy repo is retired. Until then, this can only happen once
        // metadata across
        // both repositories is available:
        // repository.validate(props);

        messageTracker.emitMessages(LOG);
        if (messageTracker.hasErrors()) {
            throw new GenerationException("Errors encountered!");
        }

        long stop = System.currentTimeMillis();
        LOG.info("COMPLETE: " + repositoryType + "metadata repository loading in " + (stop - start) + "ms");

        return repository;
    }

    public void addTarget(Target target) {
        Log log = getLog();
        if (log.isDebugEnabled()) {
            log.debug("\t    + " + target.getName());
        }
        targets.put(target.getName(), target);
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
            context.setBasedir(project.getBasedir());
            context.setEngine(engine);
            context.setProjectName(project.getName());
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

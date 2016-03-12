package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.bitbucket.fermenter.mda.element.Profile;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.Generator;
import org.bitbucket.fermenter.mda.metadata.AbstractMetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.StaticURLResolver;
import org.bitbucket.fermenter.mda.xml.TrackErrorsErrorHandler;
import org.bitbucket.fermenter.mda.xml.XmlUtils;

/**
 * Executes the Fermenter MDA process.
 */
@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateSourcesMojo extends AbstractMojo {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(GenerateSourcesMojo.class);

    private static final String PROFILE = "profiles/profile";
    private static final String PROFILE_INCLUDE = "profiles/profile/include";
    private static final String[] PROFILE_PROPERTIES = new String[] { "name", "extends" };
    private static final String PROFILE_TARGET = "profiles/profile/target";
    private static final String TARGET = "targets/target";
    private static final String[] TARGET_PROPERTIES = new String[] { "name", "generator", "templateName", "outputFile",
            "overwritable", "append" };
    private static final Map<String, Profile> PROFILES;
    private static final Map<String, Target> TARGETS;

    static {
        PROFILES = new HashMap<String, Profile>();
        TARGETS = new HashMap<String, Target>();
    }

    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    private MavenProject project;

    @Parameter(required = true)
    private String profile;

    @Parameter
    private List<String> metadataDependencies;

    @Parameter(required = true)
    private String basePackage;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/generated/java")
    private String generatedCompileSourceRoot;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/main")
    private File mainSourceRoot;

    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}/src/generated")
    private File generatedSourceRoot;
    
    @Parameter(required = true, readonly = true, defaultValue = "org.bitbucket.fermenter.mda.metadata.MetadataRepository")
    private String metadataRespositoryImpl;

    private VelocityEngine engine;

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
            metadataDependencies = new ArrayList<String>();
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
            targetEnumeration = getClass().getClassLoader().getResources("targets.xml");
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find targets", ioe);
        }

        TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(LOG);
        while (targetEnumeration.hasMoreElements()) {
            try {
                resource = (URL) targetEnumeration.nextElement();
                getLog().info("Loading targets from: " + resource.toString());
                stream = resource.openStream();

                Digester digester = XmlUtils.getNewDigester(handler);
                digester.push(this);
                digester.addObjectCreate(TARGET, Target.class);
                digester.addSetProperties(TARGET, TARGET_PROPERTIES, TARGET_PROPERTIES);
                digester.addSetNext(TARGET, "addTarget");
                digester.parse(stream);

            } catch (Exception ex) {
                throw new MojoExecutionException("Unable to parse target: "
                        + ((resource != null) ? resource.toString() : null), ex);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    private void loadProfiles() throws MojoExecutionException {
        InputStream stream = null;

        URL resource = null;
        Enumeration<URL> targetEnumeration = null;
        try {
            targetEnumeration = getClass().getClassLoader().getResources("profiles.xml");
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to find profiles", ioe);
        }

        TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(LOG);
        while (targetEnumeration.hasMoreElements()) {
            try {
                resource = (URL) targetEnumeration.nextElement();
                getLog().info("Loading profiles from: " + resource.toString());
                stream = resource.openStream();

                Digester digester = XmlUtils.getNewDigester(handler);
                digester.push(this);
                digester.addObjectCreate(PROFILE, Profile.class);
                digester.addSetProperties(PROFILE, PROFILE_PROPERTIES, PROFILE_PROPERTIES);
                digester.addObjectCreate(PROFILE_TARGET, Target.class);
                digester.addSetProperties(PROFILE_TARGET, TARGET_PROPERTIES, TARGET_PROPERTIES);
                digester.addSetNext(PROFILE_TARGET, "addTarget", Target.class.getName());
                digester.addObjectCreate(PROFILE_INCLUDE, Profile.class);
                digester.addSetProperties(PROFILE_INCLUDE, PROFILE_PROPERTIES, PROFILE_PROPERTIES);
                digester.addSetNext(PROFILE_INCLUDE, "addInclude", Profile.class.getName());
                digester.addSetNext(PROFILE, "addProfile");

                digester.parse(stream);

            } catch (Exception ex) {
                throw new MojoExecutionException("Unable to parse profiles", ex);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }

        for (Profile p : PROFILES.values()) {
            p.dereferenceProfiles(PROFILES);
        }
    }

    private void initializeMetadata() throws Exception {
        Properties props = new Properties();
        props.setProperty("application.name", project.getArtifactId());
        props.setProperty("metadata.loader", StaticURLResolver.class.getName());

        String projectUrl = new File(mainSourceRoot, "resources").toURI().toURL().toString();
        props.setProperty("metadata." + project.getArtifactId(), projectUrl);
        PackageManager.addMapping(project.getArtifactId(), basePackage);

        if (metadataDependencies != null) {
            metadataDependencies.add(project.getArtifactId());
            StringBuffer buff = new StringBuffer();
            for (Iterator<String> i = metadataDependencies.iterator(); i.hasNext();) {
                buff.append(i.next());
                if (i.hasNext()) {
                    buff.append(";");
                }
            }
            props.setProperty("metadata.locations", buff.toString());

            Set<Artifact> artifacts = project.getArtifacts();
            for (Artifact a : artifacts) {
                if (metadataDependencies.contains(a.getArtifactId())) {
                    URL url = a.getFile().toURI().toURL();
                    props.setProperty("metadata." + a.getArtifactId(), url.toString());
                    PackageManager.addMapping(a.getArtifactId(), url);
                }
            }

        }
        
        long start = System.currentTimeMillis();
        LOG.info("START: initializing metadata repository implementation: " + metadataRespositoryImpl + "...");
        
        Class<?> repoImplClass = Class.forName(metadataRespositoryImpl);
        Class<?>[] constructorParamTypes = {Properties.class};
        Constructor<?> constructor = repoImplClass.getConstructor(constructorParamTypes);
        Object[] params = {props};
        AbstractMetadataRepository repository = (AbstractMetadataRepository)constructor.newInstance(params);
        
        MetadataRepositoryManager.setRepository(repository);
        repository.load(props);       
        repository.validate(props);
        
        long stop = System.currentTimeMillis();
        LOG.info("COMPLETE: metadata repository initialization in " + (stop - start) + "ms");
        
    }

    public void addTarget(Target target) {
        Log log = getLog();
        if (log.isDebugEnabled()) {
            log.debug("\t    + " + target.getName());
        }
        TARGETS.put(target.getName(), target);
    }

    public void addProfile(Profile profile) {
        for (Target profileTarget : profile.getTargets()) {
            Target target = TARGETS.get(profileTarget.getName());
            if (target == null) {
                throw new IllegalArgumentException("No target found for profile '" + profile.getName() + "', target '"
                        + profileTarget.getName() + "'");
            }

            // replace name-only target with the real thing:
            profile.addTarget(target);

        }

        PROFILES.put(profile.getName(), profile);
    }

    private void generateSources() throws MojoExecutionException {
        long start = System.currentTimeMillis();
        Profile p = (Profile) PROFILES.get(profile);

        if (p == null) {
            StringBuffer sb = new StringBuffer(150);
            for (Profile profileValue : PROFILES.values()) {
                sb.append("\t- ").append(profileValue.getName()).append("\n");
            }
            getLog().error(
                    "<plugin>\n" + "\t<groupId>org.bitbucket.fermenter</groupId>\n"
                            + "\t<artifactId>fermenter-mda</artifactId>\n" + "\t...\n" + "\t<configuration>\n"
                            + "\t\t<profile>" + profile + "</profile>   <-----------  INVALID PROFILE!\n" + "\t\t...\n"
                            + "Profile '" + profile
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

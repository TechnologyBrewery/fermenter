package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bitbucket.fermenter.mda.GenerateSourcesHelper.LoggerDelegate.LogLevel;
import org.bitbucket.fermenter.mda.element.ExpandedFamily;
import org.bitbucket.fermenter.mda.element.ExpandedProfile;
import org.bitbucket.fermenter.mda.element.Family;
import org.bitbucket.fermenter.mda.element.Profile;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.generator.Generator;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.util.MessageTracker;
import org.bitbucket.fermenter.mda.util.PriorityMessage;
import org.bitbucket.fermenter.mda.util.PriorityMessageService;
import org.bitbucket.krausening.Krausening;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

/**
 * Utility class that provides common metamodel loading, parsing, and source
 * code generation functionality needed by Maven, Gradle, or other build tool
 * responsible for executing Fermenter.
 */
public final class GenerateSourcesHelper {

    protected static final String METAMODEL_PROPERTIES = "metamodel.properties";
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final PriorityMessageService PRIORITY_MESSAGE_SERVICE = new PriorityMessageService();

    public static LoggerDelegate logger;

    private GenerateSourcesHelper() {

    }

    /**
     * Enables the delegation of logging to build tool specific mechanisms.
     */
    public interface LoggerDelegate {
        public enum LogLevel {
            TRACE, DEBUG, INFO, WARN, ERROR
        }

        void log(LogLevel level, String message);
    }

    /**
     * Loads all {@link Family}s contained within the given
     * {@link InputStream}, which is expected to reference the desired
     * families.json file to load.
     *
     * @param familiesStream
     *            {@link InputStream} referencing families.json file desired to
     *            load.
     * @return {@link Map} containing all loaded {@link ExpandedFamily}s with
     *         their corresponding name as the map key.
     * @throws IOException
     */
    public static Map<String, ExpandedFamily> loadFamilies(InputStream familiesStream,
                                                           Map<String, ExpandedFamily> families) throws IOException {
        List<Family> loadedFamilies = OBJECT_MAPPER.readValue(familiesStream, new TypeReference<List<Family>>() {});
        for (Family f : loadedFamilies) {
            families.put(f.getName(), new ExpandedFamily(f));
        }

        return families;
    }

    /**
     * Loads all {@link Target}s contained within the given {@link InputStream},
     * which references the desired targets.json file to load.
     * 
     * @param targetsStream
     *            {@link InputStream} referencing targets.json file desired to
     *            load.
     * @return {@link Map} containing all loaded {@link Target}s with their
     *         corresponding name as the map key.
     * @throws IOException
     */
    public static Map<String, Target> loadTargets(InputStream targetsStream, Map<String, Target> targets)
            throws IOException {
        List<Target> loadedTargets = OBJECT_MAPPER.readValue(targetsStream, new TypeReference<List<Target>>() {
        });
        for (Target t : loadedTargets) {
            targets.put(t.getName(), t);
        }
        return targets;
    }

    /**
     * Loads all {@link Profile}s contained within the given
     * {@link InputStream}, which is expected to reference the desired
     * profiles.json file to load.
     * 
     * @param profilesStream
     *            {@link InputStream} referencing profiles.json file desired to
     *            load.
     * @return {@link Map} containing all loaded {@link ExpandedProfile}s with
     *         their corresponding name as the map key.
     * @throws IOException
     */
    public static Map<String, ExpandedProfile> loadProfiles(InputStream profilesStream,
            Map<String, ExpandedProfile> profiles) throws IOException {
        List<Profile> loadedProfiles = OBJECT_MAPPER.readValue(profilesStream, new TypeReference<List<Profile>>() {
        });
        for (Profile p : loadedProfiles) {
            profiles.put(p.getName(), new ExpandedProfile(p));
        }

        return profiles;
    }

    /**
     * Loads all metamodels defined by the given
     * {@link ModelRepositoryConfiguration} using the specified repository
     * implementation class. If any errors have been collected by the
     * {@link MessageTracker}, a {@link GenerationException} will be thrown.
     * 
     * @see {@link #validateMetamodelRepository(ModelInstanceRepository, LoggerDelegate)}.
     * 
     * @param config
     *            configures the {@link ModelInstanceRepository} that will be
     *            used to load metamodels.
     * @param modelInstanceRepositoryImplClazz
     *            fully qualified class name of the desired
     *            {@link ModelInstanceRepository} to use to load metamodels
     *            specified by the given {@link ModelRepositoryConfiguration}.
     * @param logger
     *            build tool specific logging implementation to which logging
     *            will be delegated.
     * @return {@link ModelInstanceRepository} of the specified type which has
     *         been loaded with all configured metamodels. <b>NOTE:</b>
     *         Metamodels have <b>*not*</b> yet been validated.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static ModelInstanceRepository loadMetamodelRepository(ModelRepositoryConfiguration config,
            String modelInstanceRepositoryImplClazz, LoggerDelegate logger) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        GenerateSourcesHelper.logger = logger;

        long start = System.currentTimeMillis();
        logger.log(LogLevel.INFO, String.format("START: loading metamodel repository implementation: %s ...",
                modelInstanceRepositoryImplClazz));

        ModelInstanceRepository repository;
        Class<?> repoImplClass = Class.forName(modelInstanceRepositoryImplClazz);
        Class<?>[] constructorParamTypes = { ModelRepositoryConfiguration.class };
        Constructor<?> constructor = repoImplClass.getConstructor(constructorParamTypes);
        Object[] params = { config };
        repository = (ModelInstanceRepository) constructor.newInstance(params);

        ModelInstanceRepositoryManager.setRepository(repository);
        repository.load();

        MessageTracker.getInstance().emitMessages(logger);
        if (MessageTracker.getInstance().hasErrors()) {
            throw new GenerationException("Errors encountered!");
        }

        long stop = System.currentTimeMillis();
        logger.log(LogLevel.INFO,
                String.format("COMPLETE: metamodel repository loading in %d ms", (stop - start)));

        return repository;
    }

    /**
     * Validates all metamodels loaded into the given
     * {@link ModelInstanceRepository} to preemptively identify any model
     * definition errors, such as metamodel schema violations, model references
     * to invalid types or entities, etc. <br>
     * <b>Pre-condition:</b> The provided {@link ModelInstanceRepository} must
     * be correctly initialized via
     * {@link #loadMetamodelRepository(ModelRepositoryConfiguration, String, LoggerDelegate)}
     * prior to invoking this method.
     * 
     * @see {@link #loadMetamodelRepository(ModelRepositoryConfiguration, String, LoggerDelegate)}.
     * @param metamodelRepository
     *            metamodel repository desired for validation which has been
     *            initialized with the relevant metamodel definitions.
     * @param logger
     *            build tool specific logging implementation to which logging
     *            will be delegated.
     */
    public static void validateMetamodelRepository(ModelInstanceRepository metamodelRepository, LoggerDelegate logger) {
        long start = System.currentTimeMillis();
        logger.log(LogLevel.INFO, "START: validating metamodel repository...");

        metamodelRepository.validate();

        long stop = System.currentTimeMillis();
        logger.log(LogLevel.INFO, "COMPLETE: validation of metamodel repository in " + (stop - start) + "ms");
    }
    
    /**
     * Executes code generation on the targets defined within the provided
     * profile, delegating to the appropriate {@link Generator} instances. <br>
     * <b>Pre-condition:</b> It is assumed that all metamodels have been loaded
     * into the appropriate {@link ModelInstanceRepository} instance and
     * validated.
     *
     * @param targetProfile           {@link Profile} defining the desired source code, resources,
     *                                etc. to generate.
     * @param profiles                all valid {@link Profile}s that are available for code
     *                                generation.
     * @param createGenerationContext {@link Function} that creates the appropriate
     *                                {@link GenerationContext} based on a given {@link Target}.
     *                                Enables build tool specific logic for populating the
     *                                {@link GenerationContext}.
     * @param handleInvalidProfile    enables developers to provide build tool specific handling the
     *                                specification of an invalid profile. This {@link Function}
     *                                receives the invalid profile that was specified and all valid
     *                                {@link Profile}s as input parameters, and may optionally
     *                                return an {@link Exception}, which will be thrown if non-null.
     * @param logger                  build tool specific logging implementation to which logging
     *                                will be delegated.
     * @throws Exception an invalid profile was specified or an unexpected error
     *                   occurred during {@link Generator} creation and processing.
     */
    public static void performSourceGeneration(String targetProfile,
                                               Map<String, ExpandedProfile> profiles,
                                               Function<Target, GenerationContext> createGenerationContext,
                                               BiFunction<String, Collection<ExpandedProfile>, Exception> handleInvalidProfile,
                                               LoggerDelegate logger,
                                               File projectDir)
            throws Exception {
        long start = System.currentTimeMillis();
        ExpandedProfile profile = profiles.get(targetProfile);

        // First validate the profile specified by the developer
        if (profile == null) {
            Exception invalidProfileException = handleInvalidProfile.apply(targetProfile, profiles.values());
            if (invalidProfileException != null) {
                throw invalidProfileException;
            }
        } else {
            if (profile.isDeprecated()) {
                //creates the priority message to be replayed at the end of the build
                PriorityMessage priorityMessage = new PriorityMessage();
                priorityMessage.setFilePath(projectDir.toString() + "/pom.xml");

                //default warning message if none is provided
                if (profile.getWarningMessage() == null || profile.getWarningMessage().isBlank()) {
                    String warningMessage = "The profile '" + profile.getName() + "' is deprecated, " +
                        "please replace all references to it.";
                    logger.log(LogLevel.WARN, warningMessage);
                    priorityMessage.setMessage(warningMessage);
                } else {
                    logger.log(LogLevel.WARN, profile.getWarningMessage());
                    priorityMessage.setMessage(profile.getWarningMessage());
                }

                PRIORITY_MESSAGE_SERVICE.addPriorityMessage(priorityMessage);
            }

            logger.log(LogLevel.INFO, "Generating code for profile '" + profile.getName() + "'");

            // For each target, instantiate a generator and call generate
            for (Target target : profile.getTargets()) {
                logger.log(LogLevel.DEBUG, "\tExecuting target '" + target.getName() + "'");
                GenerationContext context = createGenerationContext.apply(target);
                Class<?> clazz = Class.forName(target.getGenerator());
                Generator generator = (Generator) clazz.getDeclaredConstructor().newInstance();
                generator.setMetadataContext(target.getMetadataContext());
                generator.generate(context);
            }

            long stop = System.currentTimeMillis();
            logger.log(LogLevel.INFO, "Generation completed in " + (stop - start) + "ms");
        }

    }
    
    /**
     * We don't have great fine-grained control of logging inside the plugin, so
     * default some Krausening values when they aren't specified so warnings
     * don't pollute the Maven output.
     */
    public static void suppressKrauseningWarnings() {
        if (System.getProperty(Krausening.BASE_LOCATION) == null) {
            File tempKrauseningLocation = Files.createTempDir();
            File tempBaseLocation = new File(tempKrauseningLocation, "base");
            tempBaseLocation.mkdir();
            File tempExtLocation = new File(tempKrauseningLocation, "ext");
            tempExtLocation.mkdir();
            try {
                Properties p = new Properties();
                String comments = "default file to suppress warnings";
                p.store(new FileWriter(new File(tempBaseLocation, METAMODEL_PROPERTIES)), comments);
                p.store(new FileWriter(new File(tempExtLocation, METAMODEL_PROPERTIES)), comments);

            } catch (IOException e) {
                // do nothing, this is just to suppress some warnings...
            }

            System.setProperty(Krausening.BASE_LOCATION, tempBaseLocation.getAbsolutePath());
            System.setProperty(Krausening.EXTENSIONS_LOCATION, tempExtLocation.getAbsolutePath());
        }

        if (System.getProperty(Krausening.KRAUSENING_PASSWORD) == null) {
            System.setProperty(Krausening.KRAUSENING_PASSWORD, "stub");
        }
    }
    
    public static void cleanUp() {
        PackageManager.cleanUp();
        TypeManager.cleanUp();
        MessageTracker.cleanUp();
    }
    
}

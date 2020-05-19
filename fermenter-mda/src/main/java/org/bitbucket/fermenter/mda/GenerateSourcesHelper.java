package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bitbucket.fermenter.mda.GenerateSourcesHelper.LoggerDelegate.LogLevel;
import org.bitbucket.fermenter.mda.element.ExpandedProfile;
import org.bitbucket.fermenter.mda.element.Profile;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.util.MessageTracker;
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

    private GenerateSourcesHelper() {

    }

    /**
     * Enables the delegation of logging to build tool specific mechanisms.
     */
    public static abstract class LoggerDelegate {
        public static enum LogLevel {
            TRACE, DEBUG, INFO, WARN, ERROR
        }

        abstract public void log(LogLevel level, String message);
    }

    /**
     * Loads all {@link Target}s contained within the given {@link InputStream},
     * which references the desired targets.json file to load.
     * 
     * @param targetsStream
     *            {@link InputStream} referencing targets.json file desired to
     *            load.
     * @param logger
     *            build tool specific logging implementation to which logging
     *            will be delegated.
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
     * @param logger
     *            build tool specific logging implementation to which logging
     *            will be delegated.
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
     * {@link ModelRepositoryConfiguration} using the legacy repository
     * implementation. <b>NOTE:</b> This method will eventually be deprecated
     * and removed.
     * 
     * @param config
     *            configures the {@link ModelInstanceRepository} that will be
     *            used to load metamodels.
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
    public static ModelInstanceRepository loadLegacyMetadataRepository(ModelRepositoryConfiguration config,
            LoggerDelegate logger) throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        return loadMetadataRepository(config, true, MetadataRepository.class.getCanonicalName(), logger);
    }

    /**
     * Loads all metamodels defined by the given
     * {@link ModelRepositoryConfiguration} using the specified repository
     * implementation.
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
    public static ModelInstanceRepository loadMetadataRepository(ModelRepositoryConfiguration config,
            String modelInstanceRepositoryImplClazz, LoggerDelegate logger) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return loadMetadataRepository(config, false, modelInstanceRepositoryImplClazz, logger);
    }

    /**
     * Loads all metamodels defined by the given
     * {@link ModelRepositoryConfiguration} using the specified repository
     * implementation class. If any errors have been collected by the
     * {@link MessageTracker}, a {@link GenerationException} will be thrown.
     * 
     * @param config
     *            configures the {@link ModelInstanceRepository} that will be
     *            used to load metamodels.
     * @param isLegacy
     *            indicates whether the specified
     *            {@link ModelInstanceRepository} is the legacy metamodel
     *            loading mechanism; this parameter will be eventually
     *            deprecated.
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
    private static ModelInstanceRepository loadMetadataRepository(ModelRepositoryConfiguration config, boolean isLegacy,
            String modelInstanceRepositoryImplClazz, LoggerDelegate logger) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        String legacyIndicator = isLegacy ? "**LEGACY** " : "";

        long start = System.currentTimeMillis();
        logger.log(LogLevel.INFO, String.format("START: loading %s metadata repository implementation: %s ...",
                legacyIndicator, modelInstanceRepositoryImplClazz));

        ModelInstanceRepository repository;
        Class<?> repoImplClass = Class.forName(modelInstanceRepositoryImplClazz);
        Class<?>[] constructorParamTypes = { ModelRepositoryConfiguration.class };
        Constructor<?> constructor = repoImplClass.getConstructor(constructorParamTypes);
        Object[] params = { config };
        repository = (ModelInstanceRepository) constructor.newInstance(params);

        ModelInstanceRepositoryManager.setRepository(repository);
        repository.load();

        // TODO: move validation back here once the legacy repo is retired.
        // Until then, this can only happen once metadata across both
        // repositories is available:
        // repository.validate(props);

        MessageTracker.getInstance().emitMessages(logger);
        if (MessageTracker.getInstance().hasErrors()) {
            throw new GenerationException("Errors encountered!");
        }

        long stop = System.currentTimeMillis();
        logger.log(LogLevel.INFO,
                String.format("COMPLETE: %s metadata repository loading in %d ms", legacyIndicator, (stop - start)));

        return repository;
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
}

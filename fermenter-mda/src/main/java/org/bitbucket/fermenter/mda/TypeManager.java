package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.fermenter.mda.element.Type;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Responsible for maintaining mappings from logical type names to implementation types.
 */
public class TypeManager {

    private static final String UNDEFINED_TYPE = "<undefined type>";
    private static final Logger logger = LoggerFactory.getLogger(TypeManager.class);
    private static TypeManager instance = new TypeManager();

    private Map<String, Type> types = new HashMap<>();

    /**
     * Returns the type manager instance.
     * 
     * @return type manager
     */
    public static TypeManager getInstance() {
        return instance;
    }

    private TypeManager() {
        load();
    }

    /**
     * Load in the metadata from the associated file
     *
     */
    protected void load() {
        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources("types.json");
            URL typesResource;
            while (urls.hasMoreElements()) {
                typesResource = urls.nextElement();
                logger.info("Loading types from: {}", typesResource);
                processTypesFile(typesResource);
            }

        } catch (IOException e) {
            throw new GenerationException("Could not load types.json!", e);
        }
    }

    /**
     * Allows local types to be loaded into the TypeManager.
     * 
     * @param localTypes
     *            file with local types
     */
    public void loadLocalTypes(File localTypes) {
        try {
            logger.info("Checking for local types at: {}...", localTypes.getCanonicalFile());

            if (localTypes.exists()) {
                URL localTypeUrl = localTypes.toURI().toURL();
                logger.info("Loading types from: {}", localTypeUrl);
                processTypesFile(localTypeUrl);
            }

        } catch (IOException e) {
            throw new GenerationException("Could not load local types!", e);
        }
    }

    private void processTypesFile(URL typesResource) {
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        try (InputStream typesStream = typesResource.openStream()) {

            List<Type> loadedTypes = objectMapper.readValue(typesStream, new TypeReference<List<Type>>() {
            });
            for (Type type : loadedTypes) {
                types.put(type.getName(), type);
            }
        } catch (IOException e) {
            throw new GenerationException("Unable to parse types.json", e);
        }
    }

    protected Map<String, Type> getTypeMap() {
        return types;
    }

    /**
     * Returns the fully qualified implementation for a given type.
     * 
     * @param name
     *            name of the type to lookup
     * @return the fully qualified implementation name or <undefined type> so it's easier to debug in generated files
     *         when missing. A error will also be emitted to the log when <undefined type> is used.
     */
    public static String getFullyQualifiedType(String name) {
        String fullyQualifiedType = null;
        Type type = getInstance().getTypeMap().get(name);
        if (type == null) {
            logger.error("Type {} could not be found during a request for fullyQualifiedImplementation!", name);
            fullyQualifiedType = UNDEFINED_TYPE;
        } else {
            fullyQualifiedType = type.getFullyQualifiedImplementation();
        }
        return fullyQualifiedType;
    }

    /**
     * Returns the short implementation for a given type.
     * 
     * @param name
     *            name of the type to lookup
     * @return the short implementation name or <undefined type> so it's easier to debug in generated files when
     *         missing. A error will also be emitted to the log when <undefined type> is used.
     */
    public static String getShortType(String name) {
        String shortType = null;
        Type type = getInstance().getTypeMap().get(name);
        if (type == null) {
            logger.error("Type {} could not be found during a request for shortImplementation!", name);
            shortType = UNDEFINED_TYPE;
        } else {
            shortType = type.getShortImplementation();
        }
        return shortType;
    }
}

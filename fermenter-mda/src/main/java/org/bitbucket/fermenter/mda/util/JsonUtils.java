package org.bitbucket.fermenter.mda.util;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.element.ValidatedElement;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

/**
 * Contains json utilities for Fermenter.
 */
public final class JsonUtils {

    private static final Log LOG = LogFactory.getLog(JsonUtils.class);

    private static ObjectMapper objectMapper = getObjectMapper();

    private JsonUtils() {
        // prevent instantiation of final class with all static methods
    }

    /**
     * Read a Json file and validate it based on the pass type.
     * 
     * @param jsonFile
     *            file to read
     * @param type
     *            type to validate against
     * @return instance of the type or a {@link GenerationException}
     */
    public static <T extends ValidatedElement> T readAndValidateJson(File jsonFile, Class<T> type) {
        try {
            T instance = objectMapper.readValue(jsonFile, type);
            boolean valid = isValid(objectMapper.readTree(jsonFile), instance, jsonFile);
            if (!valid) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(objectMapper.writeValueAsString(instance));
                }
                throw new GenerationException(jsonFile.getName() + " contained validation errors!");
            }
            return instance;

        } catch (Exception e) {
            throw new GenerationException("Problem reading json file: " + jsonFile, e);
        }
    }

    private static <T extends ValidatedElement> boolean isValid(JsonNode jsonInstance, T instance, File jsonFile)
            throws Exception {
        ProcessingReport report = null;

        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
                .setDefaultVersion(SchemaVersion.DRAFTV4).freeze();
        JsonValidator validator = JsonSchemaFactory.newBuilder().setValidationConfiguration(cfg).freeze()
                .getValidator();
        URL targetSchemaUrl = instance.getJsonSchemaUrl();
        JsonNode targetSchemaAsJsonNode = objectMapper.readTree(targetSchemaUrl);

        report = validator.validate(targetSchemaAsJsonNode, jsonInstance);

        if (!report.isSuccess()) {
            for (ProcessingMessage processingMessage : report) {
                LOG.error(" " + jsonFile.getName() + " contains the following error:\n" + processingMessage);

            }
        }

        return report.isSuccess();
    }
    
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            
            SimpleModule module = new SimpleModule();
            module.addAbstractTypeMapping(Enum.class, EnumElement.class);
            
            ObjectMapper localMapper = new ObjectMapper();
            localMapper.registerModule(module);
            
            objectMapper = localMapper;
        }
        
        return objectMapper;
    }

}

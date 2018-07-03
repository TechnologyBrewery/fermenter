package org.bitbucket.fermenter.mda.util;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.element.ValidatedElement;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.FieldElement;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.OperationElement;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;
import org.bitbucket.fermenter.mda.metamodel.element.ParameterElement;
import org.bitbucket.fermenter.mda.metamodel.element.Parent;
import org.bitbucket.fermenter.mda.metamodel.element.ParentElement;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.ReferenceElement;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;
import org.bitbucket.fermenter.mda.metamodel.element.RelationElement;
import org.bitbucket.fermenter.mda.metamodel.element.Return;
import org.bitbucket.fermenter.mda.metamodel.element.ReturnElement;
import org.bitbucket.fermenter.mda.metamodel.element.Type;
import org.bitbucket.fermenter.mda.metamodel.element.TypeElement;

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
     * Read a Json stream and validate it based on the pass type.
     * 
     * @param jsonUrl
     *            stream to read
     * @param class1
     *            type to validate against
     * @return instance of the type or a {@link GenerationException}
     */
    public static <T extends ValidatedElement> T readAndValidateJsonByUrl(URL jsonUrl, Class<?> type) {
        try {
            T instance = objectMapper.readValue(jsonUrl, (Class<T>)type);            
            boolean valid = isValid(objectMapper.readTree(jsonUrl), instance, new File(jsonUrl.getFile()));
            if (!valid) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(objectMapper.writeValueAsString(instance));
                }
                throw new GenerationException(jsonUrl.toExternalForm() + " contained validation errors!");
            }
            return instance;

        } catch (Exception e) {
            throw new GenerationException("Problem reading json file: " + jsonUrl.toExternalForm(), e);
        }
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

    /**
     * Returns an {@link ObjectMapper} that has configurations for fermenter metamodel interfaces.
     * 
     * @return ObjectMapper singleton
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {

            SimpleModule module = new SimpleModule();
            module.addAbstractTypeMapping(Enum.class, EnumElement.class);
            module.addAbstractTypeMapping(Operation.class, OperationElement.class);
            module.addAbstractTypeMapping(Return.class, ReturnElement.class);
            module.addAbstractTypeMapping(Parameter.class, ParameterElement.class);
            module.addAbstractTypeMapping(Parent.class, ParentElement.class);
            module.addAbstractTypeMapping(Field.class, FieldElement.class);
            module.addAbstractTypeMapping(Type.class, TypeElement.class);
            module.addAbstractTypeMapping(Reference.class, ReferenceElement.class);
            module.addAbstractTypeMapping(Relation.class, RelationElement.class);

            ObjectMapper localMapper = new ObjectMapper();
            localMapper.registerModule(module);

            objectMapper = localMapper;
        }

        return objectMapper;
    }

}

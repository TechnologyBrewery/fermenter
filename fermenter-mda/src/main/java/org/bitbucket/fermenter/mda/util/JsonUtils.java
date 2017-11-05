package org.bitbucket.fermenter.mda.util;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.element.Target;
import org.bitbucket.fermenter.mda.generator.GenerationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils() {
		//prevent instantiation of final class with all static methods
	}
	
	/**
	 * Read a Json file and validate it based on the pass type.
	 * @param jsonFile file to read
	 * @param type type to validate against
	 * @return instance of the type or a {@link GenerationException}
	 */
	public static <T> T readAndValidateJson(File jsonFile, Class<T> type) {
		try {
			T instance = objectMapper.readValue(jsonFile, type);			
			boolean valid = isValid(objectMapper.readTree(jsonFile), type, jsonFile);
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
	
	private static <T> boolean isValid(JsonNode jsonInstance, Class<T> type, File jsonFile) throws Exception {
        ProcessingReport report = null;
        
    	    final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
    	            .setDefaultVersion(SchemaVersion.DRAFTV4).freeze();
    	    JsonValidator validator = JsonSchemaFactory.newBuilder()
    	            .setValidationConfiguration(cfg).freeze().getValidator();
    	    //TODO: will refactor as more types are added:
    		Enumeration<URL> targetSchema = Target.class.getClassLoader().getResources("fermenter-2-target-schema.json");
    		URL targetSchemaUrl = targetSchema.nextElement();
    		JsonNode targetSchemaAsJsonNode = objectMapper.readTree(targetSchemaUrl);

        report = validator.validate(targetSchemaAsJsonNode, jsonInstance);
        
        //TODO: see if we can extend the framework to gain more fine-grained access to reporting information
        //to improve the clarity of reported erros in the context of Fermenter:
        if (!report.isSuccess()) {
            for (ProcessingMessage processingMessage : report) {
                LOG.error(" " + jsonFile.getName() + " contains the following error:\n" + processingMessage);
                
            }
        }
		
		return report.isSuccess();
	}
	
}

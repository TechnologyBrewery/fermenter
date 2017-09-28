package org.bitbucket.fermenter.mda.util;

import java.io.File;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.Enumeration;

import org.bitbucket.fermenter.mda.generator.GenerationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * Contains json utilities for Fermenter.
 */
public final class JsonUtils {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils() {
		//prevent instantiation of final class with all static methods
	}
	
	public static <T> T readAndValidateJson(File jsonFile, Class<T> type) {
		try {
			T instance = objectMapper.readValue(jsonFile, type);
			isValid(objectMapper.readTree(jsonFile), type);
			return instance;
			
		} catch (Exception e) {
			throw new GenerationException("Problem reading json file: " + jsonFile, e);
		}
	}
	
	private static <T> boolean isValid(JsonNode jsonInstance, Class<T> type) throws Exception {
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		Enumeration<URL> targetSchema = Target.class.getClassLoader().getResources("fermenter-2-target-schema.json");
		URL targetSchemaUrl = targetSchema.nextElement();
		JsonNode targetSchemaAsJsonNode = objectMapper.readTree(targetSchemaUrl);

		JsonSchema schema = factory.getJsonSchema(targetSchemaAsJsonNode);
		ProcessingReport report = schema.validate(jsonInstance);
		if (!report.isSuccess()) {
			//TODO: harden
            for (ProcessingMessage processingMessage : report) {
                throw new ProcessingException(processingMessage);
            }
        }
		
		return report.isSuccess();
	}
	
}

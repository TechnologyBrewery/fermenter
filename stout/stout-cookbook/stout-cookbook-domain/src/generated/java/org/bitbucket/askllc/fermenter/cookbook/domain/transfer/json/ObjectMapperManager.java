package org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitbucket.fermenter.stout.messages.DefaultMessage;
import org.bitbucket.fermenter.stout.messages.DefaultMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.json.MessageDeserializer;
import org.bitbucket.fermenter.stout.messages.json.MessageSerializer;
import org.bitbucket.fermenter.stout.messages.json.MessagesMixIn;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json.*;

/**
 * Manages and customizes the Jackson {@link ObjectMapper} instance for this project.
 */
public final class ObjectMapperManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(ObjectMapperManager.class);

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		configureObjectMapper();
	}

	private ObjectMapperManager() {
		// prevent instantiation
	}

	/**
	 * Adds in any custom modules desired for this project's {@link ObjectMapper}.
	 */
	private static void configureObjectMapper() {
		SimpleModule module = new SimpleModule();
		module.setMixInAnnotation(ValidationExampleChildBO.class, ValidationExampleChildMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationExampleChildBO=ValidationExampleChildMixIn");
		module.setMixInAnnotation(ValidationExampleBO.class, ValidationExampleMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationExampleBO=ValidationExampleMixIn");
		module.setMixInAnnotation(ValidationReferencedObjectBO.class, ValidationReferencedObjectMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationReferencedObjectBO=ValidationReferencedObjectMixIn");
		module.setMixInAnnotation(ValidationReferenceExampleBO.class, ValidationReferenceExampleMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationReferenceExampleBO=ValidationReferenceExampleMixIn");
		module.setMixInAnnotation(SimpleDomainBO.class, SimpleDomainMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: SimpleDomainBO=SimpleDomainMixIn");

		module.addAbstractTypeMapping(Messages.class, DefaultMessages.class);
		LOGGER.debug("Configured Jackson of Interface/Concrete class mapping: Messages->DefaultMessages");
		module.setMixInAnnotation(Messages.class, MessagesMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: Messages=MessagesMixIn");
		module.addSerializer(Message.class, new MessageSerializer(ObjectMapperManager.class));
		LOGGER.debug("Configured Jackson custom serializer: Message->MessageSerializer");
		module.addDeserializer(Message.class, new MessageDeserializer());
		LOGGER.debug("Configured Jackson custom deserializer: Message->MessageDeserializer");

		objectMapper.registerModule(module);
		objectMapper.registerModule(new Hibernate5Module());
	}

	/**
	 * Returns the thread-safe, singleton instance of {@link ObjectMapper} for this project.
	 * 
	 * @return object mapper
	 */
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}

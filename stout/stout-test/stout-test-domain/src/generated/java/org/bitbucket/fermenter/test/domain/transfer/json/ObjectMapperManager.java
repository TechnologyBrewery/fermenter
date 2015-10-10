package org.bitbucket.fermenter.test.domain.transfer.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitbucket.fermenter.messages.DefaultMessage;
import org.bitbucket.fermenter.messages.DefaultMessages;
import org.bitbucket.fermenter.messages.InjectableMessages;
import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.Messages;
import org.bitbucket.fermenter.messages.json.MessageDeserializer;
import org.bitbucket.fermenter.messages.json.MessageSerializer;
import org.bitbucket.fermenter.messages.json.MessagesMixIn;
import org.bitbucket.fermenter.messages.json.InjectableMessagesMixIn;

import org.bitbucket.fermenter.test.domain.transfer.*;

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
		SimpleModule module = new SimpleModule("${groupId}:${artifactId}:${version}", new Version(1, 9, 2, ""));
		module.setMixInAnnotation(ValidationExampleChild.class, ValidationExampleChildMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationExampleChild=ValidationExampleChildMixIn");
		module.setMixInAnnotation(ValidationExample.class, ValidationExampleMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationExample=ValidationExampleMixIn");
		module.setMixInAnnotation(ValidationReferencedObject.class, ValidationReferencedObjectMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationReferencedObject=ValidationReferencedObjectMixIn");
		module.setMixInAnnotation(ValidationReferenceExample.class, ValidationReferenceExampleMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: ValidationReferenceExample=ValidationReferenceExampleMixIn");
		module.setMixInAnnotation(SimpleDomain.class, SimpleDomainMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: SimpleDomain=SimpleDomainMixIn");

		module.addAbstractTypeMapping(Messages.class, DefaultMessages.class);
		LOGGER.debug("Configured Jackson of Interface/Concrete class mapping: Messages->DefaultMessages");
		module.setMixInAnnotation(Messages.class, MessagesMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: Messages=MessagesMixIn");
		module.setMixInAnnotation(InjectableMessages.class, InjectableMessagesMixIn.class);
		LOGGER.debug("Configured Jackson of Class/MixIn combination: InjectableMessages=InjectableMessagesMixIn");
		module.addSerializer(Message.class, new MessageSerializer(ObjectMapperManager.class));
		LOGGER.debug("Configured Jackson custom serializer: Message->MessageSerializer");
		module.addDeserializer(Message.class, new MessageDeserializer());
		LOGGER.debug("Configured Jackson custom deserializer: Message->MessageDeserializer");

		objectMapper.registerModule(module);
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

package com.ask.test.domain.transfer.json;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ask.test.domain.transfer.SimpleDomain;

/**
 * Manages and customizes the Jackson {@link ObjectMapper} instance for this project.
 */
@Singleton
public class ObjectMapperManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(ObjectMapperManager.class);
	
	private ObjectMapper objectMapper;
	
	@PostConstruct
	private void init() {
		objectMapper = new ObjectMapper();
		configureObjectMapper();
	}
	
	/**
	 * Adds in any custom modules desired for this project's {@link ObjectMapper}.
	 */
	private void configureObjectMapper() {
		SimpleModule module = new SimpleModule("project-name", new Version(1, 9, 2, ""));
		module.setMixInAnnotation(SimpleDomain.class, SimpleDomainMixIn.class);
		LOGGER.debug("Configurd Jackson to use the following Class/MixIn combination: SimpleDomain=SimpleDomainMixIn");
		//... repeat for each domain
		
		objectMapper.registerModule(module);
		
	}
	
	/**
	 * Returns the thread-safe, singleton instance of {@link ObjectMapper} for this project.
	 * @return object mapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
}

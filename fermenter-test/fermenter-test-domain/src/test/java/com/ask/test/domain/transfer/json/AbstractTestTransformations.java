package com.ask.test.domain.transfer.json;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.tigris.atlas.messages.DefaultMessage;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Severity;

public class AbstractTestTransformations {
	
	protected static ObjectMapper objectMapper = ObjectMapperManager.getObjectMapper();
	
	@Test
	public void testObjectMapperExists() {
		assertNotNull(objectMapper);
	}

	protected Message createRandomMessage(Severity severity) {
		Message message = new DefaultMessage();
		message.setKey(RandomStringUtils.randomAlphabetic(10));
		message.setSeverity(severity);
		
		for (int i = 0 ; i < RandomUtils.nextInt(3); i++) {
			message.addInsert(new Date(RandomUtils.nextLong()));
		}
		
		for (int i = 0 ; i < RandomUtils.nextInt(3); i++) {
			message.addProperty(RandomStringUtils.random(25));
		}
		
		return message;
	}
	
}

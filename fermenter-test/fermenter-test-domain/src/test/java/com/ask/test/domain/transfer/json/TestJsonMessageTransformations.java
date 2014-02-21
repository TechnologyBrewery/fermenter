package com.ask.test.domain.transfer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tigris.atlas.messages.DefaultMessage;
import org.tigris.atlas.messages.DefaultMessages;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.messages.Severity;

public class TestJsonMessageTransformations {

	private static ObjectMapper objectMapper;
	
	@BeforeClass
	public static void init() {
		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		objectMapper = container.instance().select(ObjectMapper.class).get();
	}
	
	@Test
	public void testObjectMapperExists() {
		assertNotNull(objectMapper);
	}
	
	@Test
	public void testTransformationOfMessageKey() throws Exception {
		final String key = RandomStringUtils.randomAlphabetic(10);
		Message message = new DefaultMessage();		
		message.setKey(key);
		
		String json = objectMapper.writeValueAsString(message);
		assertNotNull(json);
		assertTrue(json.contains(key));
		
		Message rehydratedMessage = objectMapper.readValue(json, Message.class);
		assertNotNull(rehydratedMessage);
		assertEquals(key, rehydratedMessage.getKey());

	}
	
	@Test
	public void testTransformationOfMessageSeverity() throws Exception {
		Message message = new DefaultMessage();
		message.setSeverity(Severity.INFO);
		
		String json = objectMapper.writeValueAsString(message);
		assertNotNull(json);
		assertTrue(json.contains(Severity.INFO.name()));
		
		Message rehydratedMessage = objectMapper.readValue(json, Message.class);
		assertNotNull(rehydratedMessage);
		assertEquals(Severity.INFO, rehydratedMessage.getSeverity());

	}	
	
	@Test
	public void testTransformationOfMessageInserts() throws Exception {
		Message message = new DefaultMessage();
		final String insert = RandomStringUtils.randomAlphabetic(10);
		message.addInsert(insert);
		
		String json = objectMapper.writeValueAsString(message);
		assertNotNull(json);
		assertTrue(json.contains(insert));
		
		Message rehydratedMessage = objectMapper.readValue(json, Message.class);
		assertNotNull(rehydratedMessage);
		assertEquals(insert, rehydratedMessage.getInserts().iterator().next());

	}
	
	@Test
	public void testTransformationOfMessageProperties() throws Exception {
		Message message = new DefaultMessage();
		final String property = RandomStringUtils.randomAlphabetic(10);
		message.addProperty(property);
		
		String json = objectMapper.writeValueAsString(message);
		assertNotNull(json);
		assertTrue(json.contains(property));
		
		Message rehydratedMessage = objectMapper.readValue(json, Message.class);
		assertNotNull(rehydratedMessage);
		assertEquals(property, rehydratedMessage.getProperties().iterator().next());

	}
	
	@Test
	public void testTransformationOfEmptyMessages() throws Exception {
		Messages messages = new DefaultMessages();
		
		String json = objectMapper.writeValueAsString(messages);
		assertNotNull(json);
		
		Messages rehydratedMessages = objectMapper.readValue(json, Messages.class);
		assertNotNull(rehydratedMessages);
		assertEquals(0, rehydratedMessages.getAllMessages().size());

	}
	
	@Test
	public void testTransformationOfMessagesWithError() throws Exception {
		Messages messages = new DefaultMessages();
		Message error = createRandomMessage(Severity.ERROR);
		messages.addMessage(error);
		
		String json = objectMapper.writeValueAsString(messages);
		assertNotNull(json);
		
		Messages rehydratedMessages = objectMapper.readValue(json, Messages.class);
		assertNotNull(rehydratedMessages);
		assertEquals(1, rehydratedMessages.getAllMessages().size());
		assertEquals(error, rehydratedMessages.getAllMessages().iterator().next());

	}
	
	@Test
	public void testTransformationOfMessagesWithInfo() throws Exception {
		Messages messages = new DefaultMessages();
		Message info = createRandomMessage(Severity.INFO);
		messages.addMessage(info);
		
		String json = objectMapper.writeValueAsString(messages);
		assertNotNull(json);
		
		Messages rehydratedMessages = objectMapper.readValue(json, Messages.class);
		assertNotNull(rehydratedMessages);
		assertEquals(1, rehydratedMessages.getAllMessages().size());
		assertEquals(info, rehydratedMessages.getAllMessages().iterator().next());

	}
	
	@Test
	public void testTransformationOfMessagesWithErrorAndInfo() throws Exception {
		Messages messages = new DefaultMessages();
		Message error = createRandomMessage(Severity.ERROR);
		messages.addMessage(error);
		Message info = createRandomMessage(Severity.INFO);
		messages.addMessage(info);		
		
		String json = objectMapper.writeValueAsString(messages);
		assertNotNull(json);
		
		Messages rehydratedMessages = objectMapper.readValue(json, Messages.class);
		assertNotNull(rehydratedMessages);
		assertEquals(2, rehydratedMessages.getAllMessages().size());
		assertEquals(error, rehydratedMessages.getErrorMessages().iterator().next());
		assertEquals(info, rehydratedMessages.getInformationalMessages().iterator().next());

	}	
	
	private Message createRandomMessage(Severity severity) {
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

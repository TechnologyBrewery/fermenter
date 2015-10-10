package org.bitbucket.fermenter.test.domain.transfer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.fermenter.messages.DefaultMessage;
import org.bitbucket.fermenter.messages.DefaultMessages;
import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.Messages;
import org.bitbucket.fermenter.messages.Severity;
import org.junit.Test;

public class TestJsonMessageTransformations extends AbstractTestTransformations {

	@Test
	public void testTransformationOfMessageKey() throws Exception {
		final String key = getRandomValidMessageKey();
		Message message = new DefaultMessage();
		message.setKey(key);
		message.setSeverity(Severity.INFO);
		
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
		message.setKey(getRandomValidMessageKey());
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
		message.setKey(getRandomValidMessageKey());
		message.setSeverity(Severity.INFO);
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
		message.setKey(getRandomValidMessageKey());
		message.setSeverity(Severity.ERROR);
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

}

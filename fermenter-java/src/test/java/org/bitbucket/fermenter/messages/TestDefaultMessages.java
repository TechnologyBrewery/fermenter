package org.bitbucket.fermenter.messages;

import org.bitbucket.fermenter.messages.DefaultMessage;
import org.bitbucket.fermenter.messages.DefaultMessages;
import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.Messages;
import org.bitbucket.fermenter.messages.Severity;


public class TestDefaultMessages extends AbstractMessagesTestCase {

	private Messages messagesWithErrors1;
	private Messages messagesWithInfos1;
	private Messages mixedMessages;
	
	public void setUp() throws Exception {
		super.setUp();
		
		Message msg1 = new DefaultMessage();
		msg1.setKey(key1);
		msg1.addProperty("foo");
		msg1.setSeverity(Severity.ERROR);
		Message msg2 = new DefaultMessage();
		msg2.addProperty("bar");
		msg2.setSeverity(Severity.ERROR);
		msg2.setKey(key1);
		Message msg3 = new DefaultMessage();
		msg3.addProperty("foo");
		msg3.addProperty("bar");
		msg3.addProperty("foobar");
		msg3.setSeverity(Severity.ERROR);
		msg3.setKey(key1);
		Message msg4 = new DefaultMessage();
		msg4.setSeverity(Severity.ERROR);
		msg4.setKey(key1);
		
		messagesWithErrors1 = new DefaultMessages();
		messagesWithErrors1.addMessage(msg1);
		messagesWithErrors1.addMessage(msg2);
		messagesWithErrors1.addMessage(msg3);
		messagesWithErrors1.addMessage(msg4);
		
		Message msg5 = new DefaultMessage();
		msg5.setKey(key1);
		msg5.addProperty("foo");
		msg5.setSeverity(Severity.INFO);
		Message msg6 = new DefaultMessage();
		msg6.setKey(key1);
		msg6.addProperty("foo");
		msg6.addProperty("bar");
		msg6.setSeverity(Severity.INFO);
		Message msg7 = new DefaultMessage();
		msg7.setKey(key1);
		msg7.setSeverity(Severity.INFO);
		
		messagesWithInfos1 = new DefaultMessages();
		messagesWithInfos1.addMessage(msg5);
		messagesWithInfos1.addMessage(msg6);
		messagesWithInfos1.addMessage(msg7);
		
		mixedMessages = new DefaultMessages();
		mixedMessages.addMessage(msg1);
		mixedMessages.addMessage(msg3);
		mixedMessages.addMessage(msg4);
		mixedMessages.addMessage(msg6);
		mixedMessages.addMessage(msg7);
	}
	
	public void testGetErrorMessageCount() {
		assertEquals(4, messagesWithErrors1.getErrorMessageCount());
		assertEquals(2, messagesWithErrors1.getErrorMessageCount("foo"));
		assertEquals(2, messagesWithErrors1.getErrorMessageCount("bar"));
		assertEquals(1, messagesWithErrors1.getErrorMessageCount("foobar"));
		assertEquals(0, messagesWithErrors1.getErrorMessageCount("barfoo"));
	}
	
	public void testHasErrorMessages() {
		assertTrue(messagesWithErrors1.hasErrorMessages());
		assertTrue(messagesWithErrors1.hasErrorMessages("foo"));
		assertTrue(messagesWithErrors1.hasErrorMessages("bar"));
		assertTrue(messagesWithErrors1.hasErrorMessages("foobar"));
		assertFalse(messagesWithErrors1.hasErrorMessages("barfoo"));
		
		Messages messages = new DefaultMessages();
		assertFalse(messages.hasErrorMessages());
		assertFalse(messages.hasErrorMessages("foo"));
		Message message = new DefaultMessage();
		message.setKey(key1);
		message.setSeverity(Severity.ERROR);
		messages.addMessage(message);
		assertTrue(messages.hasErrorMessages());
		assertFalse(messages.hasErrorMessages("foo"));
	}
	
	public void testGetInformationalMessageCount() {
		assertEquals(3, messagesWithInfos1.getInformationalMessageCount());
		assertEquals(2, messagesWithInfos1.getInformationalMessageCount("foo"));
		assertEquals(1, messagesWithInfos1.getInformationalMessageCount("bar"));
		assertEquals(0, messagesWithInfos1.getInformationalMessageCount("foobar"));
	}
	
	public void testHasInformationalMessages() {
		assertTrue(messagesWithInfos1.hasInformationalMessages());
		assertTrue(messagesWithInfos1.hasInformationalMessages("foo"));
		assertTrue(messagesWithInfos1.hasInformationalMessages("bar"));
		assertFalse(messagesWithInfos1.hasInformationalMessages("foobar"));
		
		Messages messages = new DefaultMessages();
		assertFalse(messages.hasInformationalMessages());
		assertFalse(messages.hasInformationalMessages("foo"));
		Message message = new DefaultMessage();
		message.setKey(key1);
		message.setSeverity(Severity.INFO);
		messages.addMessage(message);
		assertTrue(messages.hasInformationalMessages());
		assertFalse(messages.hasInformationalMessages("foo"));
	}
	
	public void testMixedMessages() {
		assertTrue(mixedMessages.hasErrorMessages());
		assertTrue(mixedMessages.hasInformationalMessages());
		assertTrue(mixedMessages.hasErrorMessages("foo"));
		assertTrue(mixedMessages.hasInformationalMessages("bar"));
		
		assertEquals(3, mixedMessages.getErrorMessageCount());
		assertEquals(2, mixedMessages.getErrorMessageCount("foo"));
		assertEquals(1, mixedMessages.getErrorMessageCount("foobar"));
		
		assertEquals(2, mixedMessages.getInformationalMessageCount());
		assertEquals(1, mixedMessages.getInformationalMessageCount("foo"));
		assertEquals(1, mixedMessages.getInformationalMessageCount("bar"));
		assertEquals(0, mixedMessages.getInformationalMessageCount("foobar"));
	}
	
	public void testGetErrorMessages() {
		assertFalse(messagesWithErrors1.getErrorMessages().isEmpty());
		assertEquals(4, messagesWithErrors1.getErrorMessages().size());
		assertFalse(messagesWithErrors1.getErrorMessages("foo").isEmpty());
		assertEquals(2, messagesWithErrors1.getErrorMessages("foo").size());
		assertFalse(messagesWithErrors1.getErrorMessages("bar").isEmpty());
		assertEquals(2, messagesWithErrors1.getErrorMessages("bar").size());
		assertFalse(messagesWithErrors1.getErrorMessages("foobar").isEmpty());
		assertEquals(1, messagesWithErrors1.getErrorMessages("foobar").size());
		assertTrue(messagesWithErrors1.getErrorMessages("barfoo").isEmpty());
		assertEquals(0, messagesWithErrors1.getErrorMessages("barfoo").size());
	}
	
	public void testGetInformationalMessages() {
		assertFalse(messagesWithInfos1.getInformationalMessages().isEmpty());
		assertEquals(3, messagesWithInfos1.getInformationalMessages().size());
		assertFalse(messagesWithInfos1.getInformationalMessages("foo").isEmpty());
		assertEquals(2, messagesWithInfos1.getInformationalMessages("foo").size());
		assertFalse(messagesWithInfos1.getInformationalMessages("bar").isEmpty());
		assertEquals(1, messagesWithInfos1.getInformationalMessages("bar").size());
		assertTrue(messagesWithInfos1.getInformationalMessages("foobar").isEmpty());
		assertEquals(0, messagesWithInfos1.getInformationalMessages("foobar").size());
	}
	
	public void testContainsMessage1() {
		Message message = createMessage(Severity.INFO, key1, new String [] {});
		Messages messages = new DefaultMessages();
		
		assertFalse(messages.getErrorMessages().contains(message));
		assertFalse(messages.getInformationalMessages().contains(message));
		
		messages.addMessage(message);
		
		assertFalse(messages.getErrorMessages().contains(message));
		assertTrue(messages.getInformationalMessages().contains(message));
		
		assertFalse(messages.getErrorMessages(property1).contains(message));
		assertFalse(messages.getInformationalMessages(property1).contains(message));	
	}
	
	public void testContainsMessage2() {
		Message message = createMessage(Severity.ERROR, key1, new String [] {property1});
		Messages messages = new DefaultMessages();
		
		assertFalse(messages.getErrorMessages().contains(message));
		assertFalse(messages.getInformationalMessages().contains(message));
		
		messages.addMessage(message);
		
		assertTrue(messages.getErrorMessages().contains(message));
		assertFalse(messages.getInformationalMessages().contains(message));
		
		assertTrue(messages.getErrorMessages(property1).contains(message));
		assertFalse(messages.getInformationalMessages(property1).contains(message));
		
		assertFalse(messages.getErrorMessages(property2).contains(message));
		assertFalse(messages.getInformationalMessages(property2).contains(message));	
	}
	
	public void testContainsMessage3() {
		Message message = createMessage(Severity.ERROR, key1, new String [] {property1, property2});
		Messages messages = new DefaultMessages();
		
		assertFalse(messages.getErrorMessages().contains(message));
		assertFalse(messages.getInformationalMessages().contains(message));
		
		messages.addMessage(message);
		
		assertTrue(messages.getErrorMessages().contains(message));
		assertFalse(messages.getInformationalMessages().contains(message));
		
		assertTrue(messages.getErrorMessages(property1).contains(message));
		assertFalse(messages.getInformationalMessages(property1).contains(message));
		
		assertTrue(messages.getErrorMessages(property2).contains(message));
		assertFalse(messages.getInformationalMessages(property2).contains(message));
		
		assertFalse(messages.getErrorMessages(property3).contains(message));
		assertFalse(messages.getInformationalMessages(property3).contains(message));
		
		Message sameMsg = createMessage(Severity.ERROR, key1, new String [] {property2, property1});
		
		assertTrue(messages.getErrorMessages(property2).contains(sameMsg));
		assertFalse(messages.getInformationalMessages(property2).contains(sameMsg));
		
		Message diffMsg = createMessage(Severity.ERROR, key1, new String [] {property2, property1, property3});
		
		assertFalse(messages.getErrorMessages(property2).contains(diffMsg));
		assertFalse(messages.getInformationalMessages(property2).contains(diffMsg));
	}

}

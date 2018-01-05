package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.messages.DefaultMessages;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.MessagesSet;
import org.bitbucket.fermenter.stout.messages.Severity;


public class TestMessagesSet extends AbstractMessagesTestCase {

	private MessagesSet set;
	
	private String key3 = "3";
	private String key4 = "4";
	private String key5 = "5";
	private String key6 = "6";
	
	public void setUp() throws Exception {
		super.setUp();
		
		set = new MessagesSet();
		
		Messages messages1 = new DefaultMessages();
		messages1.addMessage(createMessage(Severity.ERROR, key1, new String []{}));
		messages1.addMessage(createMessage(Severity.ERROR, key1, new String []{"foo"}));
		messages1.addMessage(createMessage(Severity.ERROR, key1, new String []{"bar", "foo"}));
		messages1.addMessage(createMessage(Severity.ERROR, key2, new String []{}));
		Messages messages2 = new DefaultMessages();
		messages1.addMessage(createMessage(Severity.ERROR, key3, new String []{}));
		messages1.addMessage(createMessage(Severity.ERROR, key1, new String []{"bar", "foobar"}));
		messages1.addMessage(createMessage(Severity.INFO, key4, new String []{}));
		messages1.addMessage(createMessage(Severity.INFO, key2, new String []{"foo"}));
		messages1.addMessage(createMessage(Severity.INFO, key3, new String []{"foo"}));
		Messages messages3 = new DefaultMessages();
		messages1.addMessage(createMessage(Severity.INFO, key5, new String []{}));
		messages1.addMessage(createMessage(Severity.INFO, key1, new String []{"barfoo", "foo"}));
		messages1.addMessage(createMessage(Severity.INFO, key1, new String []{"bar"}));
		messages1.addMessage(createMessage(Severity.INFO, key6, new String []{}));
		
		set.addMessages(messages1);
		set.addMessages(messages2);
		set.addMessages(messages3);
	}
	
	public void testGetErrorMessageCount() {
		assertEquals(6, set.getErrorMessageCount());
		assertEquals(4, set.getErrorMessageCount("foo"));
		assertEquals(3, set.getErrorMessageCount("bar"));
		assertEquals(1, set.getErrorMessageCount("foobar"));
		assertEquals(0, set.getErrorMessageCount("barfoo"));
	}
	
	public void testGetInformationalMessageCount() {
		assertEquals(7, set.getInformationalMessageCount());
		assertEquals(4, set.getInformationalMessageCount("foo"));
		assertEquals(2, set.getInformationalMessageCount("bar"));
		assertEquals(1, set.getInformationalMessageCount("barfoo"));
		assertEquals(0, set.getInformationalMessageCount("foobar"));
	}
	
	public void testHasErrorMessages() {
		assertTrue(set.hasErrorMessages());
		assertTrue(set.hasErrorMessages("foo"));
		assertTrue(set.hasErrorMessages("bar"));
		assertTrue(set.hasErrorMessages("foobar"));
		assertFalse(set.hasErrorMessages("barfoo"));
	}
	
	public void testHasInformationalMessages() {
		assertTrue(set.hasInformationalMessages());
		assertTrue(set.hasInformationalMessages("foo"));
		assertTrue(set.hasInformationalMessages("bar"));
		assertTrue(set.hasInformationalMessages("barfoo"));
		assertFalse(set.hasInformationalMessages("foobar"));
	}
	
	public void testGetErrorMessages() {
		assertFalse(set.getErrorMessages().isEmpty());
		assertFalse(set.getErrorMessages("foo").isEmpty());
		assertFalse(set.getErrorMessages("bar").isEmpty());
		assertFalse(set.getErrorMessages("foobar").isEmpty());
		assertTrue(set.getErrorMessages("barfoo").isEmpty());
		
		assertEquals(6, set.getErrorMessages().size());
		assertEquals(4, set.getErrorMessages("foo").size());
		assertEquals(3, set.getErrorMessages("bar").size());
		assertEquals(1, set.getErrorMessages("foobar").size());
		assertEquals(0, set.getErrorMessages("barfoo").size());
	}
	
	public void testGetInformationalMessages() {
		assertFalse(set.getInformationalMessages().isEmpty());
		assertFalse(set.getInformationalMessages("foo").isEmpty());
		assertFalse(set.getInformationalMessages("bar").isEmpty());
		assertFalse(set.getInformationalMessages("barfoo").isEmpty());
		assertTrue(set.getInformationalMessages("foobar").isEmpty());
		
		assertEquals(7, set.getInformationalMessages().size());
		assertEquals(4, set.getInformationalMessages("foo").size());
		assertEquals(2, set.getInformationalMessages("bar").size());
		assertEquals(1, set.getInformationalMessages("barfoo").size());
		assertEquals(0, set.getInformationalMessages("foobar").size());
	}

}

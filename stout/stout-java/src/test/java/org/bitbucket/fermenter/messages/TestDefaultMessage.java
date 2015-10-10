package org.bitbucket.fermenter.messages;

import org.bitbucket.fermenter.messages.DefaultMessage;
import org.bitbucket.fermenter.messages.Severity;

import junit.framework.TestCase;

public class TestDefaultMessage extends TestCase {
	
	public void testGetSetKey() {
		DefaultMessage message = new DefaultMessage();
		assertNull(message.getKey());
		message.setKey("foo");
		assertEquals("foo", message.getKey());
	}
	
	public void testGetSetSeverity() {
		DefaultMessage message = new DefaultMessage();
		assertNull(message.getSeverity());
		message.setSeverity(Severity.ERROR);
		assertEquals(Severity.ERROR, message.getSeverity());
		message.setSeverity(Severity.INFO);
		assertEquals(Severity.INFO, message.getSeverity());
	}
	
	public void testAddInsert() {
		DefaultMessage message = new DefaultMessage();
		assertEquals(0, message.getInserts().size());
		assertTrue(message.getInserts().isEmpty());
		message.addInsert("foo");
		assertEquals(1, message.getInserts().size());
		assertFalse(message.getInserts().isEmpty());
		message.addInsert("foo");
		assertEquals(2, message.getInserts().size());
		assertFalse(message.getInserts().isEmpty());
	}
	
	public void testAddProperty() {
		DefaultMessage message = new DefaultMessage();
		assertEquals(0, message.getProperties().size());
		assertTrue(message.getProperties().isEmpty());
		message.addProperty("foo");
		assertEquals(1, message.getProperties().size());
		assertFalse(message.getProperties().isEmpty());
		// Properties is a set
		message.addProperty("foo");
		assertEquals(1, message.getProperties().size());
		assertFalse(message.getProperties().isEmpty());
		message.addProperty("bar");
		assertEquals(2, message.getProperties().size());
		assertFalse(message.getProperties().isEmpty());
	}
	
}

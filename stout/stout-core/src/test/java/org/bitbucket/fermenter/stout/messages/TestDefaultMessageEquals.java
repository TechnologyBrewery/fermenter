package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.messages.DefaultMessage;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Severity;


public class TestDefaultMessageEquals extends AbstractMessagesTestCase {
	
	public void testEquality1() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}
	
	public void testEquality2() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property1});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}
	
	public void testEquality3() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property3, property1});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}

	public void testEquality4() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}
	
	public void testEquality5() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {property1});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {property1});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}
	
	public void testEquality6() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {property3, property1});
		
		assertEquals(message1, message2);
		assertEquals(message1, message1);
	}
	
	public void testInequality1() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {});
		Message message2 = createMessage(Severity.ERROR, key2, new String [] {});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testInequality2() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testInequality3() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property3});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testInequality4() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property3, property2});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testInequality5() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property1, property3, property2});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testInequality6() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {});
		
		assertFalse(message1.equals(message2));
		assertEquals(message1, message1);
	}
	
	public void testEdgeCases() {
		// Test for these, although in theory they shouldn't arise
		Message message1 = new DefaultMessage();
		Message message2 = new DefaultMessage();
		
		assertFalse(message1.equals(message2));
		assertTrue(message1.equals(message1));
		
		message1.setKey("foo");
		assertFalse(message1.equals(message2));
		assertTrue(message1.equals(message1));
		
		message2.setKey("foo");
		message2.setSeverity(Severity.INFO);
		assertFalse(message1.equals(message2));
		assertTrue(message2.equals(message2));
	}
	
}

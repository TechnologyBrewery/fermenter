package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.messages.DefaultMessage;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Severity;


public class TestDefaultMessageHashCode extends AbstractMessagesTestCase {

	public void testSameHashCode1() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testSameHashCode2() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property2});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property2});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testSameHashCode3() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property3, property1});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testSameHashCode4() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testSameHashCode5() {
		Message message1 = createMessage(Severity.INFO, key2, new String [] {property2});
		Message message2 = createMessage(Severity.INFO, key2, new String [] {property2});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testSameHashCode6() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {property1, property3});
		Message message2 = createMessage(Severity.INFO, key1, new String [] {property3, property1});
		
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	public void testHashCodeConsistency1() {
		Message message1 = new DefaultMessage();
		assertEquals(new Integer(message1.hashCode()), new Integer(message1.hashCode()));
	}
	
	public void testHashCodeConsistency2() {
		Message message1 = createMessage(Severity.INFO, key2, new String [] {});
		assertEquals(new Integer(message1.hashCode()), new Integer(message1.hashCode()));
	}
	
	public void testHashCodeConsistency3() {
		Message message1 = createMessage(Severity.INFO, key2, new String [] {property1});
		assertEquals(new Integer(message1.hashCode()), new Integer(message1.hashCode()));
	}
	
	public void testHashCodeConsistency4() {
		Message message1 = createMessage(Severity.INFO, key2, new String [] {property2, property3});
		assertEquals(new Integer(message1.hashCode()), new Integer(message1.hashCode()));
	}
	
	public void testDifferentHashCodes1() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testDifferentHashCodes2() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {});
		Message message2 = createMessage(Severity.ERROR, key2, new String [] {});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testDifferentHashCodes3() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testDifferentHashCodes4() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property1, property2});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testDifferentHashCodes5() {
		Message message1 = createMessage(Severity.ERROR, key1, new String [] {property1, property2, property3});
		Message message2 = createMessage(Severity.ERROR, key1, new String [] {property1, property2});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testDifferentHashCodes6() {
		Message message1 = createMessage(Severity.INFO, key1, new String [] {property1});
		Message message2 = createMessage(Severity.ERROR, key2, new String [] {property1, property2});
		
		assertFalse(new Integer(message1.hashCode()).equals(new Integer(message2.hashCode())));
	}
	
	public void testEdgeCases() {
		// These are unlikely to arise, but check them anyway
		Message message1 = new DefaultMessage();
		Message message2 = new DefaultMessage();
		
		assertFalse(message1.hashCode() == message2.hashCode());
		assertTrue(message1.hashCode() == message1.hashCode());
		assertTrue(message2.hashCode() == message2.hashCode());
		
		message1.setSeverity(Severity.INFO);
		
		assertFalse(message1.hashCode() == message2.hashCode());
		assertTrue(message1.hashCode() == message1.hashCode());
		assertTrue(message2.hashCode() == message2.hashCode());
		
		message2.setKey("foo");
		
		assertFalse(message1.hashCode() == message2.hashCode());
		assertTrue(message1.hashCode() == message1.hashCode());
		assertTrue(message2.hashCode() == message2.hashCode());
		
		message1.setKey("foo");
		message1.addProperty("bar");
		message2.setSeverity(Severity.INFO);
		
		assertFalse(message1.hashCode() == message2.hashCode());
		assertTrue(message1.hashCode() == message1.hashCode());
		assertTrue(message2.hashCode() == message2.hashCode());
		
		message2.addProperty("bar");
		
		assertTrue(message1.hashCode() == message2.hashCode());
		assertTrue(message1.hashCode() == message1.hashCode());
		assertTrue(message2.hashCode() == message2.hashCode());
	}
	
}

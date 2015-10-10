package org.bitbucket.fermenter.stout.test.domain.bizobj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestBOEqualsAndHashCode {

	private SimpleDomainBO one;
	private SimpleDomainBO two;
	
	@Before
	public void setUp() throws Exception {
		one = BusinessObjectFactory.createSimpleDomainBO();
		two = BusinessObjectFactory.createSimpleDomainBO();
	}
	
	@Test
	public void testEqualityWithNull() {
		assertFalse("Instance should not be equals to null", one.equals(null));
	}
	
	@Test
	public void testTwoNewObjectsUnequals() {
		assertFalse("Two empty instances should NOT be equal", one.equals(two));
	}
	
	@Test
	public void testEmptyObjectSelfEquality() {
		assertTrue("An object should always be equal to itself", one.equals(one));
	}
	
	@Test
	public void testTwoObjectsNotEqual1() {
		one.setId("foo");
		two.setId("bar");
		
		assertFalse("Instances are NOT equal", one.equals(two));
	}
	
	@Test
	public void testTwoObjectsNotEqual2() {
		one.setId("foo");
		
		assertFalse("Instances are NOT equal", one.equals(two));
	}
	
	@Test
	public void testTwoObjectsNotEqual3() {
		two.setId("bar");
		
		assertFalse("Instances are NOT equal", one.equals(two));
	}
	
	@Test
	public void testTwoObjectsEqual() {
		one.setId("foo");
		two.setId("foo");
		
		assertTrue("Instances should be equal", one.equals(two));
	}
	
	@Test
	public void testDifferentHashCodes1() {
		assertFalse("Two instances should have different hash codes", new Integer(one.hashCode()).equals(new Integer(two.hashCode())));
	}
	
	@Test
	public void testDifferentHashCodes2() {
		one.setId("foo");
		assertFalse("Two instances should have different hash codes", new Integer(one.hashCode()).equals(new Integer(two.hashCode())));
	}
	
	@Test
	public void testDifferentHashCodes3() {
		two.setId("bar");
		assertFalse("Two instances should have different hash codes", new Integer(one.hashCode()).equals(new Integer(two.hashCode())));
	}
	
	@Test
	public void testDifferentHashCodes4() {
		one.setId("foo");
		two.setId("bar");
		assertFalse("Two instances should have different hash codes", new Integer(one.hashCode()).equals(new Integer(two.hashCode())));
	}
	
	@Test
	public void testSameHashCodes() {
		one.setId("foo");
		two.setId("foo");
		
		assertEquals("Should have the same hash codes", new Integer(one.hashCode()), new Integer(two.hashCode()));
	}
	
	@Test
	public void testHashCodeConsistency1() {
		one.setId("foo");

		assertEquals("An object should consistently return the same hash code", new Integer(one.hashCode()), new Integer(one.hashCode()));
	}
	
	@Test
	public void testHashCodeConsistency2() {
		assertEquals("An object should consistently return the same hash code", new Integer(one.hashCode()), new Integer(one.hashCode()));
	}
	
	@Test
	public void testHashSetContains1() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		set.add(one);
		assertTrue("Set does contain this object", set.contains(one));
	}
	
	@Test
	public void testHashSetContains2() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		set.add(one);
		assertTrue("Set does contain this object", set.contains(one));
	}
	
	@Test
	public void testHashSetContains3() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		two.setId("bar");
		set.add(one);
		assertTrue("Set does contain this object", set.contains(one));
		assertFalse("Set does not contain this object", set.contains(two));
	}
	
	@Test
	public void testHashSetContains4() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		two.setId("foo");
		set.add(one);
		assertTrue("Set does contain this object", set.contains(one));
		assertTrue("Set does contain this object", set.contains(two));
	}
	
	@Test
	public void testHashSetRemove1() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		set.add(one);
		assertTrue("Should have been able to remove this object - it's in the set", set.remove(one));
	}
	
	@Test
	public void testHashSetRemove2() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		set.add(one);
		assertTrue("Should have been able to remove this object - it's in the set", set.remove(one));
	}
	
	@Test
	public void testHashSetRemove3() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		two.setId("foo");
		set.add(one);
		assertTrue("Should have been able to remove this object - it's in the set", set.remove(two));
	}
	
	@Test
	public void testHashSetRemove4() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		one.setId("foo");
		two.setId("bar");
		set.add(one);
		assertFalse("Should not have been able to remove this object", set.remove(two));
		assertTrue(set.remove(one));
	}
	
	@Test
	public void testHashSetRemove5() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		set.add(one);
		assertFalse("Should not have been able to remove this object", set.remove(two));
	}
	
	@Test
	public void testHashSetRemove6() {
		Set<SimpleDomainBO> set = new HashSet<SimpleDomainBO>();
		set.add(one);
		SimpleDomainBO s = (SimpleDomainBO) set.iterator().next();
		assertTrue("Should have been able to remove this object - it's in the set", set.remove(s));
	}

}

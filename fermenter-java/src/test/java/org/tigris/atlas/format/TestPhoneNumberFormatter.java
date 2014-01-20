package org.tigris.atlas.format;

import org.tigris.atlas.format.PhoneNumberFormatter;

import junit.framework.TestCase;

public class TestPhoneNumberFormatter extends TestCase {
	
	public void testFormat1() {
		String pn = PhoneNumberFormatter.format( "(555)555-5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat2() {
		String pn = PhoneNumberFormatter.format( "(555) 555-5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat3() {
		String pn = PhoneNumberFormatter.format( "(555)555.5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat4() {
		String pn = PhoneNumberFormatter.format( "(555) 555.5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat5() {
		String pn = PhoneNumberFormatter.format( "555-555-5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat6() {
		String pn = PhoneNumberFormatter.format( "555.555.5555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
	public void testFormat7() {
		String pn = PhoneNumberFormatter.format( "5555555555" );
		
		assertEquals( "555-555-5555", pn );
	}
	
}

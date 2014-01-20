package org.tigris.atlas.format;

import org.tigris.atlas.format.SsnFormatter;

import junit.framework.TestCase;

public class TestSsnFormatter extends TestCase {
	
	public void testNineDigitFormat() {
		String ssn = SsnFormatter.format( "123456789" );
		
		assertEquals( "123-45-6789", ssn );
	}
	
	public void testElevenDigitFormat() {
		String ssn = SsnFormatter.format( "123-45-6789" );
		
		assertEquals( "123-45-6789", ssn );
	}
	
	public void testTenDigitFormatLeadingDash() {
		try {
			SsnFormatter.format( "123-456789" );
			fail();
		}
		catch(IllegalArgumentException e) {
			
		}		
		
	}
	
	public void testTenDigitFormatTrailingDash() {
		try {
			SsnFormatter.format( "12345-6789" );
			fail();
		}
		catch(IllegalArgumentException e) {
			
		}		
		
	}
	
	public void testEightDigitFormat() {
		try {
			SsnFormatter.format( "12345678" );
			fail();
		}
		catch(IllegalArgumentException e) {
			
		}		
	}
	
	public void testTwelveDigitFormat() {
		try {
			SsnFormatter.format( "123456789012" );
			fail();
		}
		catch(IllegalArgumentException e) {
			
		}		
	}
		
}

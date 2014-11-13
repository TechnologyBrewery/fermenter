package org.tigris.atlas.format;

import org.apache.commons.lang3.StringUtils;

public class PhoneNumberFormatter {

	public static String format(String phoneNumber) {
		if( StringUtils.isBlank( phoneNumber ) ) {
			return null;
		}
		
		StringBuffer formattedNumber = new StringBuffer();
		int j = 0;
		char c;
		for(int i=0; i<phoneNumber.length(); i++) {
			c = phoneNumber.charAt( i );
			if( Character.isDigit( c ) ) {
				formattedNumber.append( c );
				j++;
				if( j==3 || j==7 ) {
					formattedNumber.append( '-' );
					j++;
				}
			}
		}
		
		return formattedNumber.toString();
	}
	
}

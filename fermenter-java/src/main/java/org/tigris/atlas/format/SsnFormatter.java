package org.tigris.atlas.format;

import org.apache.commons.lang3.StringUtils;

public class SsnFormatter {

	public static String format(String ssn) {
		if( StringUtils.isBlank( ssn ) ) {
			return null;
		}
		
		if( ssn.length() == 9 ) {
			StringBuffer formattedSsn = new StringBuffer();
			formattedSsn.append( ssn.substring( 0, 3 ) );
			formattedSsn.append( '-' );
			formattedSsn.append( ssn.substring( 3, 5 ) );
			formattedSsn.append( '-' );
			formattedSsn.append( ssn.substring( 5, 9 ) );
			return formattedSsn.toString();
		}
		else if( ssn.length() == 11 ){
			return ssn;
		}
		else {
			throw new IllegalArgumentException( "Cannot format SSN: " + ssn );
		}
	}

}

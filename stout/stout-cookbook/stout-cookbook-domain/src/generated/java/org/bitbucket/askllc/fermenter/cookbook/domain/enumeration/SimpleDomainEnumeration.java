package org.bitbucket.askllc.fermenter.cookbook.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * Enumeration SimpleDomainEnumeration.
 * 
 * Generated Code - DO NOT MODIFY
 * 
 */
public enum SimpleDomainEnumeration {

	FIRST, SECOND, THIRD, FOURTH;

	/**
	 * Provides a case-insensitive version of valueOf(), which can be helpful when the enumeration value is a string
	 * coming from another technology (e.g., json transported from a non-Java user interface).
	 * 
	 * @param value
	 *            The value that should represent an enumerated value
	 * @return The enumerated value that matches the value
	 */
	public static SimpleDomainEnumeration valueOfIgnoresCase(String value) {
		String upperCaseValue = (StringUtils.isNotBlank(value)) ? value.toUpperCase().replace(" ", "_") : null;
		return (upperCaseValue != null) ? SimpleDomainEnumeration.valueOf(upperCaseValue) : null;

	}

}

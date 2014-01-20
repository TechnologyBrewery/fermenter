package org.tigris.atlas.messages;

import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration class for message severities.  There is a severity enumerated
 * value available for each possible severity that a message could have.
 * Severity instances may be obtained using the static <code>getSeverity</code>
 * method, passing in one of the constant strings provided by this class.
 */
public class Severity extends Enum {

	/** String representing an 'Error' severity. */
	public static final String ERROR = "Error";
	/** String representing an 'Informational' severity. */
	public static final String INFORMATIONAL = "Informational";
	
	private static final Severity SEVERITY_ERROR = new Severity(ERROR);
	private static final Severity SEVERITY_INFORMATIONAL = new Severity(INFORMATIONAL);
	
	/** Constructor is private - use <code>getSeverity</code> to obtain instances. */
	private Severity(String severity) {
		super(severity);
	}
	
	/**
	 * Return a severity enumeration value for a given severity.
	 *
	 * @param severity The string representation of the severity
	 * @return The severity enumeration value
	 */
	public static Severity getSeverity(String severity) {
		return (Severity) getEnum(Severity.class, severity);
	}
	
	/**
	 * Get a map containing all severities.
	 *
	 * @return A map of severity enumeration types keyed by the string
	 *         representation of each severity
	 */
	public static Map getEnumMap() {
		return getEnumMap(Severity.class);
	}

}

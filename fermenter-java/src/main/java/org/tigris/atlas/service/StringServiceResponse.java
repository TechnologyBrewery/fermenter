package org.tigris.atlas.service;

public class StringServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String getString() {
		return (String) getValue();
	}
	
	public final void setString(String value) {
		setValue(value);
	}
	
}

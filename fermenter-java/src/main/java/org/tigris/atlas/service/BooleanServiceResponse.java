package org.tigris.atlas.service;

public class BooleanServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Boolean getBoolean() {
		return (Boolean) getValue();
	}
	
	public final void setBoolean(Boolean value) {
		setValue(value);
	}
	
}

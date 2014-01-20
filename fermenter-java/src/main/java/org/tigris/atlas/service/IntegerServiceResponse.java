package org.tigris.atlas.service;

public class IntegerServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Integer getInteger() {
		return (Integer) getValue();
	}
	
	public final void setInteger(Integer value) {
		setValue(value);
	}
	
}

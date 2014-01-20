package org.tigris.atlas.service;

public class LongServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Long getLong() {
		return (Long) getValue();
	}
	
	public final void setLong(Long value) {
		setValue(value);
	}
	
}

package org.tigris.atlas.service;

public class ValueServiceResponse extends ServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object value;

	public final Object getValue() {
		return value;
	}

	public final void setValue(Object value) {
		this.value = value;
	}

}

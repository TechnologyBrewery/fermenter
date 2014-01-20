package org.tigris.atlas.service;

import java.sql.Timestamp;

public class TimestampServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Timestamp getTimestamp() {
		return (Timestamp) getValue();
	}
	
	public final void setTimestamp(Timestamp value) {
		setValue(value);
	}
	
}

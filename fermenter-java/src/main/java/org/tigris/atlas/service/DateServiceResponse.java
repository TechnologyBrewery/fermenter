package org.tigris.atlas.service;

import java.util.Date;

public class DateServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Date getDate() {
		return (Date) getValue();
	}
	
	public final void setDate(Date value) {
		setValue(value);
	}
	
}

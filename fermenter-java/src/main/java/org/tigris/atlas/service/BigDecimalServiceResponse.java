package org.tigris.atlas.service;

import java.math.BigDecimal;

public class BigDecimalServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final BigDecimal getBigDecimal() {
		return (BigDecimal) getValue();
	}
	
	public final void setBigDecimal(BigDecimal value) {
		setValue(value);
	}
	
}

package org.tigris.atlas.service;

import java.sql.Blob;

public class BlobServiceResponse extends ValueServiceResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final Blob getBlob() {
		return (Blob) getValue();
	}
	
	public final void setBlob(Blob value) {
		setValue(value);
	}
	
}

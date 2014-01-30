package org.tigris.atlas;

/**
 * Generic exception for throwing runtime exceptions within atlas.
 */
public class AtlasException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Atlas runtime exception.
	 * @param message useful information
	 * @param cause the encountered exception
	 */
	public AtlasException(String message, Throwable cause) {
		super(message, cause);
	}

	
	
}

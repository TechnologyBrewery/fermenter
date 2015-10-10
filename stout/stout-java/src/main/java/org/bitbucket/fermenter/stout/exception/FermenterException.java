package org.bitbucket.fermenter.stout.exception;

/**
 * Generic exception for throwing runtime exceptions within Fermenter.
 */
public class FermenterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Fermenter runtime exception.
	 * @param message useful information
	 * @param cause the encountered exception
	 */
	public FermenterException(String message, Throwable cause) {
		super(message, cause);
	}

	
	
}

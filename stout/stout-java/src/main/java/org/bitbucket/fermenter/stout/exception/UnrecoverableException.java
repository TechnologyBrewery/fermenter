package org.bitbucket.fermenter.stout.exception;

/**
 * An exception to denote cases from which there is no ability to recover.
 */
public class UnrecoverableException extends RuntimeException {

	private static final long serialVersionUID = -4923273764539689604L;

	/**
	 * {@inheritDoc}
	 */
	public UnrecoverableException() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public UnrecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public UnrecoverableException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public UnrecoverableException(Throwable cause) {
		super(cause);
	}

	
	
}

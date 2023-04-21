package org.technologybrewery.fermenter.stout.exception;

/**
 * An exception to denote cases from which there is an ability to recover and Fermenter to continue logic/control flow.
 */
public class RecoverableException extends FermenterException {

	private static final long serialVersionUID = -4923273764539689604L;

	/**
	 * {@inheritDoc}
	 */
	public RecoverableException() {
	}

	/**
	 * {@inheritDoc}
	 */
	public RecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public RecoverableException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public RecoverableException(Throwable cause) {
		super(cause);
	}
}

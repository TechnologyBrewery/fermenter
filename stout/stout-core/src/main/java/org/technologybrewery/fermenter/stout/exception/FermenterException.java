package org.technologybrewery.fermenter.stout.exception;

/**
 * This Exception denotes an Exception that has been thrown by the Fermenter architecture. All Exceptions part of the
 * Fermenter architecture should extend this class.
 */
public class FermenterException extends RuntimeException {

    private static final long serialVersionUID = -4923273764539689604L;

    /**
     * {@inheritDoc}
     */
    public FermenterException() {
    }

    /**
     * {@inheritDoc}
     */
    public FermenterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public FermenterException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public FermenterException(Throwable cause) {
        super(cause);
    }
}

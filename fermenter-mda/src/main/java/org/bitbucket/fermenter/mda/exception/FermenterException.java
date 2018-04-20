package org.bitbucket.fermenter.mda.exception;

/**
 * Specific exceptions relating to the generation process.
 */
public class FermenterException extends RuntimeException {

    private static final long serialVersionUID = 3211891635492782984L;

    /**
     * {@inheritDoc}
     */
    public FermenterException() {
        super();
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

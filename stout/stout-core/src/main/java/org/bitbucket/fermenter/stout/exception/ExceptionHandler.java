package org.bitbucket.fermenter.stout.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

/**
 * The type Exception handler.
 */
public class ExceptionHandler implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    String defaultErrorMessage = "Exception Handler caught exception of type";


    /*  Recoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    /**
     * Handle illegal access exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleIllegalAccessException(IllegalAccessException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new RecoverableException(fermenterException);
    }

    /**
     * Handle index out of bounds exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleIndexOutOfBoundsException(IndexOutOfBoundsException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new RecoverableException(fermenterException);
    }

    /**
     * Handle number format exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleNumberFormatException(NumberFormatException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new RecoverableException(fermenterException);
    }

    /**
     * Handle parse exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleParseException(ParseException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new RecoverableException(fermenterException);
    }

    /**
     * Handle string index out of bounds exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new RecoverableException(fermenterException);
    }


    /*  Unrecoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    /**
     * Handle class cast exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleClassCastException(ClassCastException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle illegal argument exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleIllegalArgumentException(IllegalArgumentException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle illegal state exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleIllegalStateException(IllegalStateException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle invocation target exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleInvocationTargetException(InvocationTargetException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle no such method exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleNoSuchMethodException(NoSuchMethodException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle null pointer exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleNullPointerException(NullPointerException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle runtime exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleRuntimeException(RuntimeException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * Handle unsupported operation exception.
     *
     * @param fermenterException the fermenter exception
     */
    protected final void handleUnsupportedOperationException(UnsupportedOperationException fermenterException) {
	    LOGGER.error(defaultErrorMessage + fermenterException.getClass(), fermenterException);

        throw new UnrecoverableException(fermenterException);
    }

    /**
     * This method will handle the given exception accordingly and allow Fermenter to decide how to handle given
     * exceptions. If the given exception does not have a handler written, it will throw an UnrecoverableException to
     * indicate that Fermenter cannot continue control flow and logic.
     *
     * @param givenException the given exception
     */
    public final void handleException(Exception givenException) {

        Throwable fermenterException = getRootCauseOfGivenException(givenException);

        try {

            // Log exception as warning that fermenter has caught an exception
            ExceptionHandler.LOGGER.warn("Fermenter exception handler caught exception", fermenterException);

            // The method that will handle the exception
            String exceptionHandlerMethodName = "handle" + fermenterException.getClass();

            // Use reflection to get the correct method/exception handler
            Method exceptionHandler = ExceptionHandler.class.getMethod(exceptionHandlerMethodName, ExceptionHandler
                    .class);

            // Invoke the exception handler method with the given exception passed as in as an argument
            exceptionHandler.invoke(this, fermenterException);

            /* The reflection will fail if there is no method to handle the exception, should be caught
             * and handled as an unrecoverable exception.
             */
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException unrecognizedException) {

            // This should be a FATAL error, but logger does not have this level. Fermenter cannot return control flow.
            ExceptionHandler.LOGGER.error("Fermenter exception handler cannot handle exception of type " + fermenterException.getClass(),
                                          unrecognizedException);

            /* If the handler receives an exception that we haven't written a case for, it should throw an
             * UnrecoverableException to cause fermenter to stop.
             */
            throw new UnrecoverableException("Exception handler does not recognize this exception",
                                             unrecognizedException);
        }
    }

    /**
     * This method will recursively get the cause of a given exception until it has found the root cause. This should be
     * avoided for exceptions which have very long stacks.
     *
     * @param givenException This is the exception that should be searched for the root cause/exception/throwable.
     * @return Throwable root cause of given exception
     */
    private Throwable getRootCauseOfGivenException(Throwable givenException) {

        /* If the exception was thrown by another exception, search recursively for the root exception that
         * originally threw it
         */
        if (givenException.getCause() != null) {
            return getRootCauseOfGivenException(givenException);
        }

        // If the given exception does not have a cause, it is the root exception and should be returned
        return givenException;
    }
}

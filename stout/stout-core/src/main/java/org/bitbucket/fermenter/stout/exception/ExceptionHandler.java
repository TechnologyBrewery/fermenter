package org.bitbucket.fermenter.stout.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

/**
 * The type ExceptionHandler class will handle exceptions across the fermenter framework accordingly. For example,
 * when some code generates an IndexOutBoundsException, it will be given to the exception handler and the handler will
 * decide how to continue. If the exception is unrecoverable, it will throw an Unrecoverable Exception to indicate that
 * Fermenter cannot continue. If control can be returned, a RecoverableException will be thrown and the user will need
 * to decide how to correct Fermenter's state.
 */
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	String defaultErrorMessage = "Exception Handler caught exception of type";


    /* Internal exception handlers below should ONLY BE USED BY THIS CLASS. They are passed a root cause, which is the
     * Exception where the error originated from. The throwing cause is where the error propagated up to and was caught.
     * The throwing cause will be the TOP of the Exception Stack.
     */


    /*  Recoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    void handleException(IllegalAccessException rootCause, Exception throwingCause) {
	    logAndThrowRecoverableException(rootCause, throwingCause);
    }

    void handleException(IndexOutOfBoundsException rootCause, Exception throwingCause) {
	    logAndThrowRecoverableException(rootCause, throwingCause);
    }

    void handleException(NumberFormatException rootCause, Exception throwingCause) {
	    logAndThrowRecoverableException(rootCause, throwingCause);
    }

    void handleException(ParseException rootCause, Exception throwingCause) {
	    logAndThrowRecoverableException(rootCause, throwingCause);
    }

    void handleException(StringIndexOutOfBoundsException rootCause, Exception throwingCause) {
	    logAndThrowRecoverableException(rootCause, throwingCause);
    }


    /*  Unrecoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    void handleException(ClassCastException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(IllegalArgumentException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(IllegalStateException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(InvocationTargetException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(NoSuchMethodException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(NullPointerException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(RuntimeException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    void handleException(UnsupportedOperationException rootCause, Exception throwingCause) {
	    logAndThrowUnrecoverableException(rootCause, throwingCause);
    }

    /**
     * This method will handle the given exception accordingly and allow Fermenter to decide how to handle given
     * exceptions. If the given exception does not have a handler written, it will throw an UnrecoverableException to
     * indicate that Fermenter cannot continue control flow and logic.
     *
     * @param givenException the given exception
     */
    public void handleException(Exception givenException) {

        Throwable rootCause = this.getRootCauseOfGivenException(givenException);

        try {

            // Log exception as warning that fermenter has caught an exception
	        LOGGER.warn("Fermenter exception handler caught exception", rootCause);


            // Use reflection to get the correct method/exception handler
            Method exceptionHandler = ExceptionHandler.class.getMethod("handleException", Exception.class);

            // Invoke the exception handler method with the given exception passed as in as an argument
            exceptionHandler.invoke(this, rootCause, givenException);



        /* The reflection will fail if there is no method to handle the exception, should be caught
         * and handled as an unrecoverable exception. */
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException
                unrecognizedException) {

            String error = "Fermenter exception handler cannot handle exception of type " + givenException.getClass();

            // This should be a FATAL error, but logger does not have this level. Fermenter cannot return control flow.
	        LOGGER.error(error, unrecognizedException);

            /* If the handler receives an exception that we haven't written a case for, it should throw an
             * UnrecoverableException to cause fermenter to stop.
             */
            throw new UnrecoverableException("Exception handler does not recognize this exception",
                                             unrecognizedException);
        }
    }



    /* Class Helper Methods */

	/**
	 * This helper method will accept a root cause (Of type exception) which is the actual problem and a throwing
	 * cause, which is error that was last thrown (should be the top of the exception stack).
	 * @param rootCause An Exception which is the at the root of an Exception stack - where the error originates from
	 * @param throwingCause An Exeption which is at the top of an Exception stack - where the error bubbled up to
     */
	private void logAndThrowRecoverableException(Exception rootCause, Exception throwingCause){

		String errorMessage = this.defaultErrorMessage + rootCause.getClass();

        // Since FATAL level is not available and error is being used for unrecoverable exceptions, we'll use warn here.
		LOGGER.warn(errorMessage, throwingCause);

        throw new RecoverableException(throwingCause);
    }

	/**
	 * This helper method will accept a root cause (Of type exception) which is the actual problem and a throwing
	 * cause, which is error that was last thrown (should be the top of the exception stack).
	 * @param rootCause An Exception which is the at the root of an Exception stack - where the error originates from
	 * @param throwingCause An Exeption which is at the top of an Exception stack - where the error bubbled up to
	 */
    private void logAndThrowUnrecoverableException(Exception rootCause, Exception throwingCause){

	    String errorMessage = this.defaultErrorMessage + rootCause.getClass();

        // Should really be FATAL level setting, since that isn't available, we will use error
	    LOGGER.error(errorMessage, throwingCause);

	    throw new RecoverableException(throwingCause);
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
            return this.getRootCauseOfGivenException(givenException);
        }

        // If the given exception does not have a cause, it is the root exception and should be returned
        return givenException;
    }
}

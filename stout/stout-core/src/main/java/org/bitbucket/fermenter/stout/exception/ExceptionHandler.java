package org.bitbucket.fermenter.stout.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The ExceptionHandler class will handle exceptions across the Fermenter architecture accordingly. For example, when
 * some code generates an IndexOutBoundsException, it should be given to the exception handler and the handler will
 * decide how to continue. If the exception is unrecoverable, it will throw an Unrecoverable Exception to indicate that
 * Fermenter cannot continue. If control can be returned, a RecoverableException will be thrown and the user will need
 * to decide how to correct Fermenter's state.
 */
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	String defaultErrorMessage = "Exception Handler caught exception of ";

    /**
     * This method will handle the given exception accordingly and allow Fermenter to decide how to handle given
     * exception. If the given exception does not have a handler written, it will throw an UnrecoverableException to
     * indicate that Fermenter cannot continue control flow and logic.
     *
     * @param givenException the given exception
     */
    public void handleException(Exception givenException) {

        Exception rootException = this.getRootExceptionOfGivenException(givenException);

        Class[] exceptionHandlerArgumentTypes = new Class[]{rootException.getClass()};
        Object[] exceptionHandlerArguments = new Object[]{rootException};
        Method exceptionHandlerMethod;

        try {
            exceptionHandlerMethod =
                    ExceptionHandler.class.getDeclaredMethod("handleException", exceptionHandlerArgumentTypes);

        } catch(NoSuchMethodException noSuchMethodException){
            throw noValidExceptionHandlerMethodFound(givenException, noSuchMethodException);
        }

        FermenterException exceptionHandlerAction;

        try {
            assert exceptionHandlerMethod != null;
            exceptionHandlerAction =
                    (FermenterException) exceptionHandlerMethod.invoke(this, exceptionHandlerArguments);

        } catch (IllegalAccessException | InvocationTargetException | NullPointerException |
                SecurityException unrecognizedException) {

            String exceptionMessage = "";

            /* The method handlers will either throw recoverable or unrecoverable, these will be caught by the
             * try catch block and need to be passed through. */
            if((unrecognizedException.getClass() == InvocationTargetException.class)){

                exceptionMessage = "Exception handler failed to handle given exception of type " +
                        givenException.getClass();

            } else if (unrecognizedException.getClass() == IllegalAccessException.class) {
                exceptionMessage =
                        "Exception handler class cannot access exception handler method for exception type " +
                                givenException.getClass();

            } else if (unrecognizedException.getClass() == NullPointerException.class) {
                exceptionMessage =
                        "Exception handler must be passed an exception with a type - NULL is not valid";

            } else if (unrecognizedException.getClass() == SecurityException.class) {
                exceptionMessage =
                        "Exception handler has encountered a SecurityException and cannot recover workflow logic";

            } else {
                exceptionMessage = "Unknown problem with exception handler";

            }

            /* This should be logged as a FATAL error, but the  logger does not have this level. Fermenter cannot
             * return control flow if one of these errors has occurred. */
            throw logAndGetUnrecoverableException(unrecognizedException, exceptionMessage);
        }

        if (exceptionHandlerAction != null){
            throw exceptionHandlerAction;
        }
    }




    /* Internal exception handlers below should ONLY BE USED BY THIS CLASS. They are passed a root cause, which is the
     * Exception where the problem originated from. This should NOT be the exception at the top of the of the stack.
     */


    /*  Recoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    private FermenterException handleException(java.lang.IndexOutOfBoundsException rootException) {
	    return logAndGetRecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.NumberFormatException rootException) {
	    return logAndGetRecoverableException(rootException);
    }

    private FermenterException handleException(java.text.ParseException rootException) {
	    return logAndGetRecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.StringIndexOutOfBoundsException rootException) {
	    return logAndGetRecoverableException(rootException);
    }



    /*  Unrecoverable Exceptions
     *  Exceptions should be added lexicographically
     */

    private FermenterException handleException(java.lang.ClassCastException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.IllegalAccessException rootException) {
        return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.IllegalArgumentException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.IllegalStateException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.reflect.InvocationTargetException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.NoSuchMethodException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.NullPointerException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.RuntimeException rootException) {
	    return logAndGetUnrecoverableException(rootException);
    }

    private FermenterException handleException(java.lang.UnsupportedOperationException rootException) {
        return logAndGetUnrecoverableException(rootException) ;
    }



    /* Class Helper Methods */


    /**
     * This method should be called when there isn't a method written for a particular exception case that needs to e
     * handled. This does NOT throw the exception, it will return an Unrecoverable Exception and allow the caller to
     * decide what to do.
     * @param rootException
     * @param handlerException
     * @return UnrecoverableException
     */
    private UnrecoverableException noValidExceptionHandlerMethodFound(Exception rootException, Exception handlerException){

        String errorMessage = "Exception handler class " + rootException.getClass() +
                " does not have an exception handler implemented";

        LOGGER.error(errorMessage, handlerException);

        return new UnrecoverableException(rootException);
    }


    /**
     * This will add a warning to the log for the exception handler logger and then return a RecoverableException. This
     * method DOES NOT throw the exception; it allows the caller to decide how to proceed.
     * @param rootException
     * @return
     */
	private RecoverableException logAndGetRecoverableException(Exception rootException){

		String errorMessage = defaultErrorMessage + rootException.getClass();

        // Since FATAL level is not available and error is being used for unrecoverable exception, we'll use warn here.
		LOGGER.warn(errorMessage);

        return new RecoverableException(errorMessage, rootException);
    }


    /**
     * This will add a warning to the log for the exception handler logger and then return a RecoverableException. This
     * method DOES NOT throw the exception; it allows the caller to decide how to proceed.
     * @param rootException
     * @return
     */
    private RecoverableException logAndGetRecoverableException(Exception rootException, String errorMessage){

        // Since FATAL level is not available and error is being used for unrecoverable exception, we'll use warn here.
        LOGGER.warn(errorMessage);

        return new RecoverableException(errorMessage, rootException);
    }


    /**
     * This will add a warning to the log for the exception handler logger and then return an UnrecoverableException.
     * This method DOES NOT throw the exception; it allows the caller to decide how to proceed.
     * @param rootException
     * @return UnrecoverableException
     */
    private UnrecoverableException logAndGetUnrecoverableException(Exception rootException){

	    String errorMessage = defaultErrorMessage + rootException.getClass();

        // Should really be FATAL level setting, since that isn't available, we will use error
	    LOGGER.error(errorMessage);

	    return new UnrecoverableException(errorMessage, rootException);
    }


    /**
     * This will add a warning to the log for the exception handler logger and then return an UnrecoverableException.
     * This method DOES NOT throw the exception; it allows the caller to decide how to proceed.
     * @param rootException
     * @return UnrecoverableException
     */
    private UnrecoverableException logAndGetUnrecoverableException(Exception rootException, String errorMessage){

        // Should really be FATAL level setting, since that isn't available, we will use error
        LOGGER.error(errorMessage);

        return new UnrecoverableException(errorMessage, rootException);
    }


    /**
     * This method will recursively get the cause of a given exception until it has found the root cause. This should
     * be avoided for exception which have very long stacks, as they can cause a stack overflow or the program to run
     * out of memory.
     * @param parentException This is the exception that should be searched for the root cause/exception/throwable.
     * @return Throwable root cause of given exception
     */
    private Exception getRootExceptionOfGivenException(Exception parentException) {

        /* If the exception was thrown by another exception, search recursively for the root exception that
         * originally threw it */
        if (parentException.getCause() != null) {
            return this.getRootExceptionOfGivenException(parentException);
        }

        // If the given exception does not have a cause, it is the root exception and should be returned
        return parentException;
    }
}

package org.bitbucket.fermenter.stout.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public final class ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);


	/*  Recoverable Exceptions
	 *  Exceptions should be added lexicographically
	 */

	protected final void handleIllegalAccessException(IllegalAccessException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new RecoverableException(fermenterException);
	}

	protected final void handleIndexOutOfBoundsException(IndexOutOfBoundsException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new RecoverableException(fermenterException);
	}

	protected final void handleNumberFormatException(NumberFormatException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new RecoverableException(fermenterException);
	}

	protected final void handleParseException(ParseException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);
		throw new RecoverableException(fermenterException);
	}

	protected final void handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new RecoverableException(fermenterException);
	}


	/*  Unrecoverable Exceptions
	 *  Exceptions should be added lexicographically
	 */

	protected final void handleClassCastException(ClassCastException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleIllegalArgumentException(IllegalArgumentException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleIllegalStateException(IllegalStateException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleInvocationTargetException(InvocationTargetException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleNoSuchMethodException(NoSuchMethodException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleNullPointerException(NullPointerException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleRuntimeException(RuntimeException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	protected final void handleUnsupportedOperationException(UnsupportedOperationException fermenterException){
		LOGGER.error("Exception Handler caught exception of type" + fermenterException.getClass(), fermenterException);

		throw new UnrecoverableException(fermenterException);
	}

	public final void handleException(Exception givenException){

		Throwable fermenterException = getRootCauseOfGivenException(givenException);

		try {

			String exceptionHandlerMethodName = "handle" + fermenterException.getClass();

			// Log exception as debug
			LOGGER.warn("Fermenter exception handler caught exception", fermenterException);

			// Use reflection to call correct method
			ExceptionHandler.class.getMethod(exceptionHandlerMethodName, fermenterException);

			/* The reflection will fail if there is no method to handle the exception, should be caught
			 * and handled as an unrecoverable exception.
			 */
		} catch(NoSuchMethodException | NullPointerException unrecognizedException){

			LOGGER.error("Fermenter exception handler cannot handle exception of type " + fermenterException.getClass(),unrecognizedException);

			/* If the handler receives an exception that we haven't written a case for, it should throw an
			 * UnrecoverableException to cause fermenter to stop.
			 */
			throw new UnrecoverableException("Exception handler does not recognize this exception", unrecognizedException);
		}
	}

	private final Throwable getRootCauseOfGivenException(Throwable givenException){

		/* If the exception was thrown by another exception, search recursively for the root exception that
 		 * originally threw it
		 */
		if(givenException.getCause() != null){
			return getRootCauseOfGivenException(givenException);
		}

		// If the given exception does not have a cause, it is the root exception and should be returned
		return givenException;
	}
}

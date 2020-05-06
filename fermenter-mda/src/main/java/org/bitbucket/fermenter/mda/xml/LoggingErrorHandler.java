package org.bitbucket.fermenter.mda.xml;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An <tt>ErrorHandler</tt> instance that records parsing events to a logger.
 * This Commons Logging logger can be specified or defaulted the logger of 
 * this class. 
 */
public class LoggingErrorHandler implements ErrorHandler {
	
	private Log log = LogFactory.getLog(LoggingErrorHandler.class);
	private String fileName;
	
	/**
	 * New instance to log parser events
	 */
	public LoggingErrorHandler() {		
	}
	
	/**
	 * Creates a new instance that will use the passed logger to record
	 * parser errors.
	 * @param logInstance
	 */
	public LoggingErrorHandler(Log logInstance) {
		if (log != null) {
			this.log = logInstance;
		}
	}
	
	/**
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException spe) throws SAXException {
		if (log.isErrorEnabled()) {
			String msg = createLogMessage(spe);
			log.error(msg);	
		}
		
	}
		
	/**
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException spe) throws SAXException {
		if (log.isFatalEnabled()) {
			String msg = createLogMessage(spe);
			log.fatal(msg, spe);
		}
		
	}

	/**
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException spe) throws SAXException {
		if (log.isWarnEnabled()) {
			String msg = createLogMessage(spe);
			log.warn(msg);
		}
		
	}	
	
	/**
	 * Allows for the optional setting of file names to allow for more useful 
	 * debug messages.  If this is set, then the error handler is not thread
	 * safe.
	 * @param fileName The name of the file to use in output messages.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected String createLogMessage(SAXParseException spe) {
		int lineNumber = spe.getLineNumber();
		int columnNumber = spe.getColumnNumber();
		String message = spe.getMessage();		
		
		StringBuffer sb = new StringBuffer(150);
		sb.append("A parsing error occurred ");
		if (!StringUtils.isBlank(fileName)) {
			sb.append("in file '").append(fileName).append("' ");
		}
		sb.append("on line '").append(lineNumber).
			append("' and column number '").append(columnNumber).
			append("' - ").append(message);
		
		return sb.toString();
	}

}

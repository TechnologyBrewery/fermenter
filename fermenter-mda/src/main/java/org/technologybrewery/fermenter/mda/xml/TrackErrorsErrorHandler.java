package org.technologybrewery.fermenter.mda.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TrackErrorsErrorHandler extends LoggingErrorHandler {
	
	private boolean errorsOccurred = false;
	
	public TrackErrorsErrorHandler(Log log) {
		super(log);
	}

	public void error(SAXParseException spe) throws SAXException {
		super.error(spe);
		setErrorsOccurred();
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		super.fatalError(spe);
		setErrorsOccurred();
	}
	
	private void setErrorsOccurred() {
		errorsOccurred = true;
	}
	
	public boolean haveErrorsOccurred() {
		return errorsOccurred;
	}

}

package org.technologybrewery.fermenter.stout;

import org.technologybrewery.fermenter.stout.messages.AbstractMessageManagerAwareProcessor;
import org.technologybrewery.fermenter.stout.messages.CoreMessages;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.Messages;
import org.technologybrewery.fermenter.stout.messages.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockBatchProcessor extends AbstractMessageManagerAwareProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(MockBatchProcessor.class);
	
	private Messages batchJobMessages = new Messages();

	public void runBatchStepThatProducesAndError() {				
		Message errorMessage = new Message(CoreMessages.UNKNOWN_EXCEPTION_OCCURRED, Severity.ERROR);
		MessageManager.addMessage(errorMessage);
		
		logger.error("Error encountered!");
			
	}
	
	public void runBatchStepThatSucceeds() {
		logger.debug("Processed successfully");
			
	}
	
	public void moveToBatchJobMessageTrackingAndClearForNextBatchItem() {
		this.migrateToOverallMessagesAndClear(batchJobMessages);
	}
	
}

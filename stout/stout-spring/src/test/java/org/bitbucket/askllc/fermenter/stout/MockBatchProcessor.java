package org.bitbucket.askllc.fermenter.stout;

import org.bitbucket.fermenter.stout.messages.AbstractMessageManagerAwareProcessor;
import org.bitbucket.fermenter.stout.messages.CoreMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.Severity;
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

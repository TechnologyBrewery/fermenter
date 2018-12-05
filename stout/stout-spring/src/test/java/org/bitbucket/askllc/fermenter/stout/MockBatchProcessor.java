package org.bitbucket.askllc.fermenter.stout;

import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareProcessor;
import org.bitbucket.fermenter.stout.messages.DefaultMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockBatchProcessor extends AbstractMsgMgrAwareProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(MockBatchProcessor.class);
	
	private Messages batchJobMessages = new DefaultMessages();

	public void runBatchStepThatProducesAndError() {				
		String[] properties = {};
		Object[] inserts = {};
		Message errorMessage = MessageUtils.createErrorMessage("bad.batch.step", properties, inserts);
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

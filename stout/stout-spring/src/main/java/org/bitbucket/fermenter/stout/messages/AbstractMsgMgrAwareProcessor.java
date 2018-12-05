package org.bitbucket.fermenter.stout.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Similar to {@link AbstractMsgMgrAwareService}, but for use in scenarios like
 * a batch processor where you want to be able to control messages per record or
 * per batch (or whatever your scenario is).
 */
public abstract class AbstractMsgMgrAwareProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMsgMgrAwareProcessor.class);

	protected final void initMsgMgr(Messages messages) {
		if (messages != null) {
			MessageManager.initialize(messages);
		}
	}

	protected final void migrateToOverallMessagesAndClear(Messages overallMessages) {
		Messages messages = MessageManager.getMessages();
		String messageSummary = "Encountered " + messages.getErrorMessageCount() + " error and "
				+ messages.getInformationalMessageCount() + " informational messages!";
		if (messages.hasErrorMessages()) {
			logger.error(messageSummary);

		} else {
			logger.info(messageSummary);

		}
		
		overallMessages.addMessages(messages);
		
		MessageManager.cleanup();
		
	}

}

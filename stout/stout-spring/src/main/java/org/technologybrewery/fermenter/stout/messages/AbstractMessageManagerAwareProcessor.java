package org.technologybrewery.fermenter.stout.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Similar to {@link AbstractMessageManagerAwareService}, but for use in scenarios like
 * a batch processor where you want to be able to control messages per record or
 * per batch (or whatever your scenario is).
 */
public abstract class AbstractMessageManagerAwareProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMessageManagerAwareProcessor.class);

	protected final void initMsgMgr(Messages messages) {
		if (messages != null) {
			MessageManager.initialize(messages);
		}
	}

	protected final void migrateToOverallMessagesAndClear(Messages overallMessages) {
		Messages messages = MessageManager.getMessages();
		String messageSummary = "Encountered " + messages.getErrorCount() + " error and "
				+ messages.getInfoCount() + " informational messages!";
		if (messages.hasErrors()) {
			logger.error(messageSummary);

		} else {
			logger.info(messageSummary);

		}
		
		overallMessages.addMessages(messages);
		
		MessageManager.cleanup();
		
	}

}

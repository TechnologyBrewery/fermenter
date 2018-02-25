package org.bitbucket.fermenter.stout.test;

import static org.junit.Assert.assertFalse;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageTestUtils {

    private static final Logger logger = LoggerFactory.getLogger(MessageTestUtils.class);
    
    private MessageTestUtils() {
        //private constructor to prevent instantiation of all static class
    }

    public static void assertNoErrorMessages() {
        Messages messages = MessageManager.getMessages();
        boolean hasErrorMessages = messages.hasErrorMessages();
        if (hasErrorMessages) {
            for (Message message : messages.getErrorMessages()) {
                logger.error(
                        MessageUtils.getSummaryMessage(message.getKey(), message.getInserts(), MessageUtils.class));
            }
        }
        assertFalse(hasErrorMessages);
    }

}

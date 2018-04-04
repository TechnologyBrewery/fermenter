package org.bitbucket.fermenter.stout.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.Severity;
import org.bitbucket.fermenter.stout.service.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful utilities methods when working with Fermenter objects in a unit/integration test scenario.
 */
public final class MessageTestUtils {

    private static final Logger logger = LoggerFactory.getLogger(MessageTestUtils.class);

    private MessageTestUtils() {
        // private constructor to prevent instantiation of all static class
    }

    /**
     * Asserts that no messages have been encountered in the current {@link MessageManager}.
     */
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

    /**
     * Logs an errors associated with the passed messages.
     * 
     * @param contentInformation
     *            contextual information about the message being logged
     * @param messages
     *            the message being logged
     * @param clazz
     *            the associated class for message lookup
     * 
     */
    public static void logErrors(String contentInformation, Messages messages, Class<?> clazz) {
        if ((messages != null) && (messages.hasErrorMessages())) {
            for (Message message : messages.getErrorMessages()) {
                logger.error("Encountered the following error when working with: {}\n\t{}", contentInformation,
                        MessageUtils.getSummaryMessage(message.getKey(), message.getInserts(), clazz));
            }
        }
    }

    /**
     * Utility method that tests whether a response has any error messages, logging any error messages found.
     * 
     * @param contentInformation
     *            contextual information about the message being logged
     * @param response
     *            the response with the messages that will be checked
     * @param clazz
     *            the associated class for message lookup
     */
    public static void assertNoErrorMessages(String contentInformation, ServiceResponse response, Class<?> clazz) {
        if (response != null) {
            Messages messages = response.getMessages();
            boolean hasErrorMessages = messages.hasErrorMessages();
            if (hasErrorMessages) {
                MessageTestUtils.logErrors(contentInformation, messages, clazz);
            }
            assertFalse("Encountered error messages unexpectedly", hasErrorMessages);
        }
    }

    /**
     * Utility method that tests whether a response has any error messages (without the contextual background), and
     * logging message key and inserts for an error messages.
     * 
     * @param response
     *            the response instance to check for errors
     */
    public static void assertNoErrorMessages(ServiceResponse response) {
        if (response != null) {
            Messages messages = response.getMessages();
            boolean hasErrorMessages = messages.hasErrorMessages();
            if (hasErrorMessages) {
                for (Message message : messages.getErrorMessages()) {
                    logger.error(message.getKey(), message.getInserts());
                }
            }
            assertFalse("Encountered error messages unexpectedly", hasErrorMessages);
        }
    }

    /**
     * Checks that the expected number of error messages were found.
     * 
     * @param response
     *            the response instance to check for errors
     * @param expectedNumErrorMessages
     *            the expected number of error messages
     */
    public static void assertErrorMessagesInResponse(ServiceResponse response, int expectedNumErrorMessages) {
        assertNotNull("Service response wrapper was unexpectedly null", response);
        Messages messages = response.getMessages();
        assertNotNull("Messages object on service response wrapper was unexpected null", messages);
        assertEquals("An unexpected number of error messages were found", expectedNumErrorMessages,
                messages.getErrorMessageCount());
    }

    /**
     * Asserts that an error message with the key expected was returned.
     * 
     * @param key
     *            the message key
     * @param messages
     *            the messages from the response
     */
    public static void validateErrorMessage(String key, Messages messages) {
        validateMessage(key, messages, Severity.ERROR);
    }

    public static void validateInfoMessage(String key, Messages messages) {
        validateMessage(key, messages, Severity.INFO);
    }

    /**
     * Asserts that a message with the key and severity expected is returned.
     * 
     * @param key
     *            the message key
     * @param messages
     *            the messages from the response
     * @param severity
     *            the severity of the message
     */
    public static void validateMessage(String key, Messages messages, Severity severity) {
        assertNotNull(messages);
        assertEquals(1, messages.getAllMessages().size());
        Message message = messages.getAllMessages().iterator().next();
        assertEquals(key, message.getKey());
        assertEquals(severity, message.getSeverity());
    }

}

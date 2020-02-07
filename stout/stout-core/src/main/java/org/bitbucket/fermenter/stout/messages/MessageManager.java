package org.bitbucket.fermenter.stout.messages;

import org.slf4j.Logger;

/**
 * {@link MessageManager} provides a thin layer over {@link Messages} that are
 * stored in {@link ThreadLocal}. This class is used in conjunction with
 * {@link ThreadLocalMessageManagementInterceptor} in JEE applications and
 * {@link AbstractMsgMgrAwareService} in servlet applications in order to enable
 * business logic to easily propagate {@link Messages} back to the business
 * service layer.
 */
public final class MessageManager {

    private static final ThreadLocal<Messages> MESSAGES = ThreadLocal.withInitial(DefaultMessages::new);

    // This one we want to be per thread, so we create one set of unmanaged
    // default messages per thread.
    private static final ThreadLocal<Boolean> createdLocally = new ThreadLocal<>();

    private MessageManager() {

    }

    static boolean isCreatedLocally() {
        return createdLocally.get() == Boolean.TRUE;
    }

    static void setIsCreatedLocally() {
        createdLocally.set(Boolean.TRUE);
    }

    static void resetIsCreatedLocally() {
        createdLocally.remove();
    }

    static void initialize(Messages messages) {
        MESSAGES.set(messages);
    }

    static void cleanup() {
        MESSAGES.remove();
    }

    public static Messages getMessages() {
        return MESSAGES.get();
    }

    public static boolean hasErrorMessages() {
        return MESSAGES.get().hasErrorMessages();
    }

    public static void addMessage(Message message) {
        MESSAGES.get().addMessage(message);
    }

    public static void addMessages(Messages messages) {
        MESSAGES.get().addMessages(messages);
    }

    /**
     * Logs all messages in the message manager based on the settings for the
     * passed logger. Errors at error level, informational messages and info
     * level.
     * 
     * @param logger
     *            logger instance to use
     * @param classpathRoot
     *            classpath anchor used to lookup the appropriate messages.xml
     *            file
     */
    public static void logMessages(Logger logger, Class<?> classpathRoot) {
        Messages messages = getMessages();
        if (logger.isErrorEnabled() && messages.hasErrorMessages()) {
            StringBuilder logOutput = new StringBuilder();
            logOutput.append("Encountered " + messages.getErrorMessageCount() + " errors:");
            for (Message message : getMessages().getErrorMessages()) {
                logOutput.append("\n\t" + MessageUtils.getSummaryMessage(message, classpathRoot));
            }

            logger.error(logOutput.toString());
        }

        if (logger.isInfoEnabled() && messages.hasInformationalMessages()) {
            StringBuilder logOutput = new StringBuilder();
            logOutput.append("Encountered " + messages.getErrorMessageCount() + " informational messages:");
            for (Message message : getMessages().getInformationalMessages()) {
                logOutput.append("\n\t" + MessageUtils.getSummaryMessage(message, classpathRoot));
            }

            logger.info(logOutput.toString());
        }

    }
}

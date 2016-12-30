package org.bitbucket.fermenter.stout.messages;

/**
 * {@link MessageManager} provides a thin layer over {@link Messages} that are stored in {@link ThreadLocal}. This class
 * is used in conjunction with {@link ThreadLocalMessageManagementInterceptor} in JEE applications and
 * {@link AbstractMsgMgrAwareService} in servlet applications in order to enable business logic to easily propagate
 * {@link Messages} back to the business service layer.
 */
public final class MessageManager {

	private static final ThreadLocal<Messages> MESSAGES = new ThreadLocal<Messages>() {
		protected Messages initialValue() {
			return new DefaultMessages();
		};
	};

	private MessageManager() {

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
}

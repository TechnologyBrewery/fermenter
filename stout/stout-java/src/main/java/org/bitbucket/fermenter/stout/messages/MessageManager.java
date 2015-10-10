package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.bizobj.BusinessObject;
import org.bitbucket.fermenter.stout.bizobj.TransferObjectAssembler;
import org.bitbucket.fermenter.stout.transfer.TransferObject;

/**
 * {@link MessageManager} provides a thin layer over {@link Messages} that are stored in {@link ThreadLocal}. This class
 * is used in conjunction with {@link ThreadLocalMessageManagementInterceptor} in order to allow for non-container
 * managed beans, such as generated {@link BusinessObject}s, {@link TransferObject}s, and
 * {@link TransferObjectAssembler}s, to easily propagate {@link Messages} back to the business service layer.
 */
public final class MessageManager {

	private static final ThreadLocal<Messages> MESSAGES = new ThreadLocal<Messages>();

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

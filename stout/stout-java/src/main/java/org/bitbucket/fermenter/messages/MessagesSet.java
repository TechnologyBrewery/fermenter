package org.bitbucket.fermenter.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Convenience class used to aggregate messages contained in multiple <code>
 * Messages</code> instances into one object (which also implements the <code>
 * Messages</code> insterface. This class may be used to treat messages from
 * different sources (e.g. from a parent and a child object), as a single
 * collection of messages.
 * 
 * @see org.bitbucket.fermenter.messages.Messages
 */
public final class MessagesSet implements Messages {

	private static final long serialVersionUID = -2608875594853456395L;

	private Set<Messages> messages;

	/**
	 * Create a new instance.
	 */
	public MessagesSet() {
		super();

		messages = new HashSet<Messages>();
	}

	/**
	 * Add a message to the set. <strong>N.B.:</strong> This method is not
	 * implemented, as messages should not be added to the set directly, but
	 * rather to the underlying <code>Messages</code> instance(s).
	 * 
	 * @throws UnsupportedOperationException
	 *             This method is not implemented
	 */
	public void addMessage(Message message) {
		String errMsg = "Do not add messages directly to a MessagesSet; add"
				+ " them to the underlying Messages instance(s)";
		throw new UnsupportedOperationException(errMsg);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addMessages(Messages msgs) {
		messages.add(msgs);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getErrorMessageCount() {
		return getAllErrorMessages().size();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getErrorMessageCount(String property) {
		return getAllErrorMessages(property).size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getErrorMessages() {
		return getAllErrorMessages();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getErrorMessages(String property) {
		return getAllErrorMessages(property);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInformationalMessageCount() {
		return getAllInformationalMessages().size();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInformationalMessageCount(String property) {
		return getAllInformationalMessages(property).size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getInformationalMessages() {
		return getAllInformationalMessages();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getInformationalMessages(String property) {
		return getAllInformationalMessages(property);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasErrorMessages() {
		return getErrorMessageCount() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasErrorMessages(String property) {
		return getErrorMessageCount(property) > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasInformationalMessages() {
		return getInformationalMessageCount() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasInformationalMessages(String property) {
		return getInformationalMessageCount(property) > 0;
	}

	private Collection<Message> getAllErrorMessages() {
		Collection<Message> errors = new ArrayList<Message>();
		for (Messages msgs : messages) {
			errors.addAll(msgs.getErrorMessages());
		}
		return errors;
	}

	private Collection<Message> getAllErrorMessages(String property) {
		Collection<Message> errors = new ArrayList<Message>();
		for (Messages msgs : messages) {
			errors.addAll(msgs.getErrorMessages(property));
		}
		return errors;
	}

	private Collection<Message> getAllInformationalMessages() {
		Collection<Message> infos = new ArrayList<Message>();
		for (Messages msgs : messages) {
			infos.addAll(msgs.getInformationalMessages());
		}
		return infos;
	}

	private Collection<Message> getAllInformationalMessages(String property) {
		Collection<Message> infos = new ArrayList<Message>();
		for (Messages msgs : messages) {
			infos.addAll(msgs.getInformationalMessages(property));
		}
		return infos;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getAllMessages() {
		Collection<Message> allMessages = new ArrayList<Message>();
		for (Messages msgs : messages) {
			allMessages.addAll(msgs.getAllMessages());
		}
		return allMessages;
	}

}

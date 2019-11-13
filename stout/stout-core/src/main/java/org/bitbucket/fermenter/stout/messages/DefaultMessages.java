package org.bitbucket.fermenter.stout.messages;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link Messages} interface.
 */
public class DefaultMessages implements Messages {

	private static final long serialVersionUID = 7211778261488081187L;

	// TODO: replace this with two sets, one for errors, one for infos for simplicity:
	private EnumMap<Severity, Set<Message>> messages;

	/**
	 * Create a new instance.
	 */
	public DefaultMessages() {
		super();

		messages = new EnumMap<>(Severity.class);
		for (Severity s : Severity.values()) {
			messages.put(s, new HashSet<>());

		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addMessage(Message message) {
		if (message == null) {
			throw new IllegalArgumentException("Message must be non-null");
		}

		if (StringUtils.isBlank(message.getKey()) || message.getSeverity() == null) {
			throw new IllegalArgumentException("Severity and key must both be non-null");
		}

		Collection<Message> msgs = messages.get(message.getSeverity());
		msgs.add(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getErrorMessageCount() {
		return getMessageCount(Severity.ERROR);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getErrorMessageCount(String property) {
		return getMessageCount(Severity.ERROR, property);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getErrorMessages() {
		return messages.get(Severity.ERROR);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getErrorMessages(String property) {
		return getMessagesList(Severity.ERROR, property);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInformationalMessageCount() {
		return getMessageCount(Severity.INFO);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInformationalMessageCount(String property) {
		return getMessageCount(Severity.INFO, property);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getInformationalMessages() {
		return messages.get(Severity.INFO);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getInformationalMessages(String property) {
		return getMessagesList(Severity.INFO, property);
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

	private Collection<Message> getMessagesList(Severity s, String property) {
		Collection<Message> msgs = messages.get(s);
		Collection<Message> returnList = new HashSet<>();
		for (Message msg : msgs) {
			String msgKey = msg.getKey();
			if (msg.getProperties().contains(property) || msgKey.equals(property)) {
				returnList.add(msg);
			}
		}
		return Collections.unmodifiableCollection(returnList);
	}

	private int getMessageCount(Severity s) {
		return messages.get(s).size();
	}

	private int getMessageCount(Severity s, String property) {
		Collection<Message> msgs = messages.get(s);
		int count = 0;
		for (Message msg : msgs) {
			String msgKey = msg.getKey();
			if (msgKey.equals(property) || msg.getProperties().contains(property)) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * Add an entire list of messages to this message list.
	 * 
	 * @param messages The messages to add
	 */
	public void addMessages(Messages messages) {
		for (Message m : messages.getErrorMessages()) {
			addMessage(m);
		}

		for (Message m : messages.getInformationalMessages()) {
			addMessage(m);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Message> getAllMessages() {
		Collection<Message> allMessages = new HashSet<>();
		for (Set<Message> messageSet : messages.values()) {
			allMessages.addAll(messageSet);
		}

		return allMessages;
	}

	/**
	 * {@inheritDoc}
	 */
	void setAllMessages(Collection<Message> messages) {
		for (Message message : messages) {
			addMessage(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getErrorMessageCount()).append(" errors, ");
        sb.append(getInformationalMessageCount()).append(" infos");
        return sb.toString();
    }

}

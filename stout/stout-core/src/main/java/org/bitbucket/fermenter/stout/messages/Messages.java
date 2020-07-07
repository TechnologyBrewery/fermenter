package org.bitbucket.fermenter.stout.messages;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Container for messages. Any object needing to hold on to {@link Message}
 * instances can use a {@link Messages} object to manage messages. A
 * {@link Messages} instance can provide messages based on their severities, or
 * by the properties with which they are associated. In addition, convenience
 * methods are provided to answer whether the {@link Messages} contains certain
 * messages, and/or how many of such messages there are.
 */
public class Messages implements Iterable<Message> {
	
	private Set<Message> errorMessages = new HashSet<>();
	private Set<Message> infoMessages = new HashSet<>();

	/**
	 * Get all messages, regardless of severity.
	 * 
	 * @return A non-null collection of messages regardless of severity
	 */
	public Collection<Message> getAllMessages() {
		return CollectionUtils.union(errorMessages, infoMessages);
	}
	
	void addAllMessages(Collection<Message> messages) {
	    if (messages != null) {
    	    for (Message message : messages) {
    	        addMessage(message);
    	    }
	    }
	}

	/**
	 * Add a message to this collection of messages.
	 * 
	 * @param message The message to add
	 */
	public void addMessage(Message message) {
		if (message == null) {
			throw new IllegalArgumentException("Message must be non-null");
		}

		if (StringUtils.isBlank(message.getMetaMessage().toString()) || message.getSeverity() == null) {
			throw new IllegalArgumentException("Message severity and name must both be non-null\n\t" + message);
		}

		if (Severity.ERROR.equals(message.getSeverity())) {
			errorMessages.add(message);

		} else {
			infoMessages.add(message);

		}
	}

	/**
	 * Answer how many messages with a severity of 'Error' are present.
	 * 
	 * @return The number of 'Error' messages
	 */
	public int getErrorCount() {
		return errorMessages.size();
	}

	/**
	 * Get all messages with a severity of 'Error' that are present.
	 * 
	 * @return A non-null collection of 'Error' messages
	 */
	public Collection<Message> getErrors() {
		return errorMessages;
	}

	/**
	 * Answer how many messages with a severity of 'Informational' are present.
	 * 
	 * @return The number of 'Informational' messages
	 */
	public int getInfoCount() {
		return infoMessages.size();
	}

	/**
	 * Get all messages with a severity of 'Informational' that are present.
	 * 
	 * @return A non-modifiable, non-null collection of 'Informational' messages
	 */
	public Collection<Message> getInfos() {
		return infoMessages;
	}

	/**
	 * Answer whether any messages with a severity of 'Error' are present.
	 * 
	 * @return True if any 'Error' messages are present, false otherwise
	 */
	public boolean hasErrors() {
		return !errorMessages.isEmpty();
	}

	/**
	 * Answer whether any messages with a severity of 'Informational' are present.
	 * 
	 * @return True if any 'Informational' messages are present, false otherwise
	 */
	public boolean hasInfos() {
		return !infoMessages.isEmpty();
	}

	/**
	 * Add an entire list of messages to this message list .
	 * 
	 * @param messages
	 */
	public void addMessages(Messages messages) {
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
		sb.append(getErrorCount()).append(" errors, ");
		sb.append(getInfoCount()).append(" infos");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Message> iterator() {
		return getAllMessages().iterator();
	}

}

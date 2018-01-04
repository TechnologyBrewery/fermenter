package org.bitbucket.fermenter.stout.messages;

import java.io.Serializable;
import java.util.Collection;

/**
 * Container for messages.  Any object needing to hold on to {@link Message} instances can use a 
 * {@link Messages} object to manage messages. A {@link Messages} instance can provide messages 
 * based on their severities, or by the properties with which they are associated.  In  addition, 
 * convenience methods are provided to answer whether the {@link Messages} contains certain messages, 
 * and/or how many of such messages there are.
 * 
 * @see org.bitbucket.fermenter.stout.messages.DefaultMessages
 * @see org.bitbucket.fermenter.stout.messages.Severity
 */
public interface Messages extends Serializable {

	public static final String REQ_ATTR_ERROR_MSGS = "ErrorMessages";
	public static final String REQ_ATTR_INFO_MSGS = "InfoMessages";
	
	/**
	 * Get all messages, regardless of severity.
	 * @return A non-null collection of 'Informational' messages
	 */
	Collection<Message> getAllMessages();
	
	/**
	 * Add a message to this collection of messages.
	 * @param message The message to add
	 */
	void addMessage(Message message);
	
	/**
	 * Answer how many messages with a severity of 'Error' are present.
	 * @return The number of 'Error' messages
	 */
	int getErrorMessageCount();
	
	/**
	 * Answer how many messages with a severity of 'Error' are present that
	 * are associated with a given property name.
	 * @param property The name of the property
	 * @return The number of 'Error' messages associated with the given property
	 */
	int getErrorMessageCount(String property);
	
	/**
	 * Get all messages with a severity of 'Error' that are present
	 * @return A non-modifiable, non-null collection of 'Error' messages
	 */
	Collection<Message> getErrorMessages();
	
	/**
	 * Get all messages with a severity of 'Error' that are associated with a
	 * given property.
	 * @param property The name of the property
	 * @return A non-modifiable, non-null collection of all 'Error' messages
	 *         associated with the given property
	 */
	Collection<Message> getErrorMessages(String property);
	
	/**
	 * Answer how many messages with a severity of 'Informational' are present.
	 * @return The number of 'Informational' messages
	 */
	int getInformationalMessageCount();
	
	/**
	 * Answer how many messages with a severity of 'Informational' are present
	 * that are associated with a given property name.
	 * @param property The name of the property
	 * @return The number of 'Informational' messages associated with the given
	 *         property
	 */
	int getInformationalMessageCount(String property);
	
	/**
	 * Get all messages with a severity of 'Informational' that are present.
	 * @return A non-modifiable, non-null collection of 'Informational' messages
	 */
	Collection<Message> getInformationalMessages();
	
	/**
	 * Get all messages with a severity of 'Informational' that are associated
	 * with a given property.
	 * @param property The name of the property
	 * @return A non-modifiable, non-null collection of all 'Informational'
	 *         messages associated with the given property
	 */
	Collection<Message> getInformationalMessages(String property);
	
	/**
	 * Answer whether any messages with a severity of 'Error' are present.
	 * @return True if any 'Error' messages are present, false otherwise
	 */
	boolean hasErrorMessages();
	
	/** 
	 * Answer whether any messages with a severity of 'Error' are present for a
	 * given property.
	 * @param property The property name
	 * @return True if any 'Error' messages are present for the given property,
	 *         false otherwise
	 */
	boolean hasErrorMessages(String property);
	
	/**
	 * Answer whether any messages with a severity of 'Informational' are present.
	 * @return True if any 'Informational' messages are present, false otherwise
	 */
	boolean hasInformationalMessages();
	
	/** 
	 * Answer whether any messages with a severity of 'Informational' are present for a
	 * given property.
	 * @param property The property name
	 * @return True if any 'Informational' messages are present for the given
	 *         property, false otherwise
	 */
	boolean hasInformationalMessages(String property);
	
	/**
	 * Add an entire list of messages to this message list .
	 * @param messages
	 */
	void addMessages(Messages messages);
	
}

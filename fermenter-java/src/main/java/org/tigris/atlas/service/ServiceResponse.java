package org.tigris.atlas.service;

import java.io.Serializable;

import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.messages.Messages;

/**
 * Base class for service responses.  Allows the ability to associate messages at the service level.
 */
public abstract class ServiceResponse implements Serializable {

	private static final long serialVersionUID = -2752831251132027520L;
	
	protected Messages messages;
	
	protected ServiceResponse() {
		
	}
	
	protected ServiceResponse(Messages messages) {
		this.messages = messages;
	}

	/**
	 * Returns messages for this response.
	 * @return messages
	 */
	public final Messages getMessages() {
		if (messages == null) {
    		messages = MessageFactory.createMessages();
		}
		return messages;
	}
	
	/**
	 * Indicates whether errors exist within the messages on this response (vice information or warning messages).
	 * @return true is has error messages
	 */
	public boolean hasErrors() {
		return getMessages().hasErrorMessages();
	}
	
}

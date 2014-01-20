package org.tigris.atlas.service;

import java.io.Serializable;

import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.messages.Messages;

/**
 * Base class for service responses.  Allows the ability to associate an error list at the service level.
 * 
 * @author Steve Andrews
 *
 */
public abstract class ServiceResponse implements Serializable {

	protected Messages messages;

	public final Messages getMessages() {
		if (messages == null) {
    		messages = MessageFactory.createMessages();
		}
		return messages;
	}
	
	public boolean hasErrors() {
		return getMessages().hasErrorMessages();
	}
	
}

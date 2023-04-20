package org.technologybrewery.fermenter.stout.messages.json;

import java.util.Collection;

import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the json contract for {@link Messages}.
 */
public abstract class MessagesMixIn {

	@JsonProperty("messages")
	abstract Collection<Message> getAllMessages();
	
	@JsonProperty("messages")
	abstract void addAllMessages(Collection<Message> messages);
	
	@JsonIgnore
	abstract int getErrorCount();

	@JsonIgnore
	abstract Collection<Message> getErrors();

	@JsonIgnore
	abstract int getInfoCount();

	@JsonIgnore
	abstract Collection<Message> getInfos();

}

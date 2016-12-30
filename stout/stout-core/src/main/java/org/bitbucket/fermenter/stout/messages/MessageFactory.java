package org.bitbucket.fermenter.stout.messages;

public class MessageFactory {

	public static Message createMessage() {
		return new DefaultMessage();
	}

	public static Messages createMessages() {
		return new DefaultMessages();
	}

}

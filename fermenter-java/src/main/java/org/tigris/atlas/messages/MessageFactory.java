package org.tigris.atlas.messages;

public class MessageFactory {
	
	public static Message createMessage() {
		return new DefaultMessage();
	}
	
	public static Messages createMessages() {
		return new DefaultMessages();
	}
	
	public static MessagesHandler createMessagesHandler() {
		return new DefaultMessagesHandler();
	}

}

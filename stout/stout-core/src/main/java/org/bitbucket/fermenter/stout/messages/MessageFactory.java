package org.bitbucket.fermenter.stout.messages;

public final class MessageFactory{
	
	private MessageFactory() {
		//Do not allow all static class to be instantiated 
	}

	public static Message createMessage() {
		return new DefaultMessage();
	}

	public static Messages createMessages() {
		return new DefaultMessages();
	}

}

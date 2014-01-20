package org.tigris.atlas.transfer;


import java.io.Serializable;

import org.tigris.atlas.messages.DefaultMessages;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.messages.MessagesSet;

public abstract class AbstractTransferComponent implements Serializable {

	private Messages messages;

	public Messages getMessages() {
		if (messages == null) {
			messages = new DefaultMessages();
		}

		return messages;
	}

	public abstract Messages getAllMessages();

	protected abstract void gatherMessages(MessagesSet set);

}

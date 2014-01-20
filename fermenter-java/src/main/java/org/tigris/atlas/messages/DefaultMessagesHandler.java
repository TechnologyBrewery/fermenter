package org.tigris.atlas.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Default message handler implementation - handles messages by making
 * them accesible from the http request.
 */
public class DefaultMessagesHandler implements MessagesHandler {

	/**
	 * Handle a messages object by making the messages available in the request.
	 * This will format any messages and make them available (as lists) as attributes
	 * keyed by <code>Messages.REQ_ATTR_ERROR_MSGS</code> and
	 * <code>Messages.REQ_ATTR_INFO_MSGS</code>.
	 */
	public void handleMessages(Messages messages, Class clazz, HttpServletRequest request) {
		if (messages.hasErrorMessages()) {
			List errorMsgs = new ArrayList();
			for (Iterator i = messages.getErrorMessages().iterator(); i.hasNext();) {
				Message msg = (Message) i.next();
				String formattedMsg = MessageUtils.getSummaryMessage(msg.getKey(), msg.getInserts(), clazz, request.getLocale());
				errorMsgs.add(formattedMsg);
			}
			
			request.setAttribute(Messages.REQ_ATTR_ERROR_MSGS, errorMsgs);
		}
		
		if (messages.hasInformationalMessages()) {
			List infoMsgs = new ArrayList();
			for (Iterator i = messages.getInformationalMessages().iterator(); i.hasNext();) {
				Message msg = (Message) i.next();
				String formattedMsg = MessageUtils.getSummaryMessage(msg.getKey(), msg.getInserts(), clazz, request.getLocale());
				infoMsgs.add(formattedMsg);
				
			}
			
			request.setAttribute(Messages.REQ_ATTR_INFO_MSGS, infoMsgs);
		}
	}

}

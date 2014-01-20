package org.tigris.atlas.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Convenience class used to aggregate messages contained in multiple <code>
 * Messages</code> instances into one object (which also implements the <code>
 * Messages</code> insterface.  This class may be used to treat messages from
 * different sources (e.g. from a parent and a child object), as a single
 * collection of messages.
 * 
 * @see org.tigris.atlas.messages.Messages
 */
public final class MessagesSet implements Messages {

	private Set messages;

	/**
	 * Create a new instance.
	 */
	public MessagesSet() {
		super();
		
		messages = new HashSet();
	}
	
	/**
	 * Add a message to the set.  <strong>N.B.:</strong> This method is not
	 * implemented, as messages should not be added to the set directly, but
	 * rather to the underlying <code>Messages</code> instance(s).
	 *
	 * @throws UnsupportedOperationException This method is not implemented
	 */
	public void addMessage(Message message) {
		String errMsg = "Do not add messages directly to a MessagesSet; add"
			            + " them to the underlying Messages instance(s)";
		throw new UnsupportedOperationException(errMsg);
	}
	
	/**
	 * Add a <code>Messages</code> instance to the collection of <code>
	 * Messages</code> maintained by this set.  Any messages contained within
	 * or added to the parameter object will be included in the set of messages
	 * operated on by the messages set.
	 *
	 * @param msgs The <code>Messages</code> instance to add to this set
	 */
	public void addMessages(Messages msgs) {
		messages.add(msgs);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessageCount()
	 */
	public int getErrorMessageCount() {
		return getAllErrorMessages().size();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessageCount(java.lang.String)
	 */
	public int getErrorMessageCount(String property) {
		return getAllErrorMessages(property).size();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessages()
	 */
	public Collection getErrorMessages() {
		return getAllErrorMessages();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessages(java.lang.String)
	 */
	public Collection getErrorMessages(String property) {
		return getAllErrorMessages(property);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessageCount()
	 */
	public int getInformationalMessageCount() {
		return getAllInformationalMessages().size();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessageCount(java.lang.String)
	 */
	public int getInformationalMessageCount(String property) {
		return getAllInformationalMessages(property).size();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessages()
	 */
	public Collection getInformationalMessages() {
		return getAllInformationalMessages();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessages(java.lang.String)
	 */
	public Collection getInformationalMessages(String property) {
		return getAllInformationalMessages(property);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#hasErrorMessages()
	 */
	public boolean hasErrorMessages() {
		return getErrorMessageCount() > 0;
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#hasErrorMessages(java.lang.String)
	 */
	public boolean hasErrorMessages(String property) {
		return getErrorMessageCount(property) > 0;
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#hasInformationalMessages()
	 */
	public boolean hasInformationalMessages() {
		return getInformationalMessageCount() > 0;
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#hasInformationalMessages(java.lang.String)
	 */
	public boolean hasInformationalMessages(String property) {
		return getInformationalMessageCount(property) > 0;
	}
	
	private Collection getAllErrorMessages() {
		Collection errors = new ArrayList();
		for (Iterator i = messages.iterator(); i.hasNext();) {
			Messages msgs = (Messages) i.next();
			errors.addAll(msgs.getErrorMessages());
		}
		return errors;
	}
	
	private Collection getAllErrorMessages(String property) {
		Collection errors = new ArrayList();
		for (Iterator i = messages.iterator(); i.hasNext();) {
			Messages msgs = (Messages) i.next();
			errors.addAll(msgs.getErrorMessages(property));
		}
		return errors;
	}
	
	private Collection getAllInformationalMessages() {
		Collection errors = new ArrayList();
		for (Iterator i = messages.iterator(); i.hasNext();) {
			Messages msgs = (Messages) i.next();
			errors.addAll(msgs.getInformationalMessages());
		}
		return errors;
	}
	
	private Collection getAllInformationalMessages(String property) {
		Collection errors = new ArrayList();
		for (Iterator i = messages.iterator(); i.hasNext();) {
			Messages msgs = (Messages) i.next();
			errors.addAll(msgs.getInformationalMessages(property));
		}
		return errors;
	}

}

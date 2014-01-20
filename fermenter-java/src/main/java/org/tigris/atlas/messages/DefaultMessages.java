package org.tigris.atlas.messages;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Default implementation of the <code>Messages</code> interface.  Note that
 * adding errors to this class using the <code>addMessages</code> method does
 * not have any effect on the transaction - if signalling a rollback is desired,
 * a <code>ServiceMessage</code> instance should be used.
 * 
 * @see org.tigris.atlas.messages.Messages
 * @see org.tigris.atlas.servce.spring.ServiceMessages
 */
public class DefaultMessages implements Messages {

	private Map messages;
	
	/**
	 * Create a new instance.
	 */
	public DefaultMessages() {
		super();
		
		messages = new HashMap();
		for (Iterator i = Severity.getEnumMap().values().iterator(); i.hasNext();) {
			Severity s = (Severity) i.next();
			messages.put(s, new HashSet());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#addMessage(org.tigris.atlas.messages.Message)
	 */
	public void addMessage(Message message) {
		if( message==null ) {
			throw new IllegalArgumentException( "Message must be non-null" );
		}
		
		if (StringUtils.isBlank(message.getKey()) || message.getSeverity() == null) {
			throw new IllegalArgumentException("Severity and key must both be non-null");
		}
		
		Collection msgs = getMessagesList(message.getSeverity());
		msgs.add(message);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessageCount()
	 */
	public int getErrorMessageCount() {
		return getMessageCount(Severity.getSeverity(Severity.ERROR));
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessageCount(java.lang.String)
	 */
	public int getErrorMessageCount(String property) {
		return getMessageCount(Severity.getSeverity(Severity.ERROR), property);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessages()
	 */
	public Collection getErrorMessages() {
		return getMessagesList(Severity.getSeverity(Severity.ERROR));
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getErrorMessages(java.lang.String)
	 */
	public Collection getErrorMessages(String property) {
		return getMessagesList(Severity.getSeverity(Severity.ERROR), property);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessageCount()
	 */
	public int getInformationalMessageCount() {
		Collection msgs = getMessagesList(Severity.getSeverity(Severity.INFORMATIONAL));
		return msgs.size();
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessageCount(java.lang.String)
	 */
	public int getInformationalMessageCount(String property) {
		return getMessageCount(Severity.getSeverity(Severity.INFORMATIONAL), property);
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessages()
	 */
	public Collection getInformationalMessages() {
		return getMessagesList(Severity.getSeverity(Severity.INFORMATIONAL));
	}

	/* (non-Javadoc)
	 * @see org.tigris.atlas.messages.Messages#getInformationalMessages(java.lang.String)
	 */
	public Collection getInformationalMessages(String property) {
		return getMessagesList(Severity.getSeverity(Severity.INFORMATIONAL), property);
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
	
	private Collection getMessagesList(Severity s) {
		return (Collection) messages.get(s);
	}
	
	private Collection getMessagesList(Severity s, String property) {
		Collection msgs = getMessagesList(s);
		Collection returnList = new HashSet();
		for (Iterator i = msgs.iterator(); i.hasNext();) {
			Message msg = (Message) i.next();
			if (msg.getProperties().contains(property)) {
				returnList.add(msg);
			}
		}
		return Collections.unmodifiableCollection(returnList);
	}
	
	private int getMessageCount(Severity s) {
		return getMessagesList(s).size();
	}
	
	private int getMessageCount(Severity s, String property) {
		Collection msgs = getMessagesList(s);
		int count = 0;
		for (Iterator i = msgs.iterator(); i.hasNext();) {
			Message msg = (Message) i.next();
			if (msg.getProperties().contains(property)) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * Add an entire list of messages to this message list 
	 * @param messages
	 */
	public void addMessages(Messages messages) {		
		Iterator m = messages.getErrorMessages().iterator();
		while( m.hasNext() ) {
			addMessage( (Message) m.next() );
		}
		m = messages.getInformationalMessages().iterator();
		while( m.hasNext() ) {
			addMessage( (Message) m.next() );
		}
	}
	
}

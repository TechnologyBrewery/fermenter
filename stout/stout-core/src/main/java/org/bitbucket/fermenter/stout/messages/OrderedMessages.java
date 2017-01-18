package org.bitbucket.fermenter.stout.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class OrderedMessages extends DefaultMessages {

	private String [] orderedProperties;
	
	public OrderedMessages(String [] properties) {
		this();
		
		if (properties == null || properties.length == 0) {
			throw new IllegalArgumentException("List of properties must be non-empty");
		}
		
		orderedProperties = new String [properties.length];
		for (int i=0; i<properties.length; i++) {
			orderedProperties[i] = properties[i];
		}
	}
	
	private OrderedMessages() {
		super();
	}
	
	public Collection getErrorMessages() {
		return sortMessages(super.getErrorMessages());
	}

	public Collection getInformationalMessages() {
		return sortMessages(super.getInformationalMessages());
	}

	private Collection sortMessages(Collection messages) {
		List sortedMessages = new ArrayList();
		// Add messages according to property order
		for (int i=0; i<orderedProperties.length; i++) {
			String property = (orderedProperties[i]);
			for (Iterator j = messages.iterator(); j.hasNext();) {
				Message msg = (Message) j.next();
				if (msg.getProperties().contains(property) && !sortedMessages.contains(msg)) {
					sortedMessages.add(msg);
				}
			}
		}

		// There may be messages that haven't been added
		for (Iterator i = messages.iterator(); i.hasNext();) {
			Message msg = (Message) i.next();
			if (!sortedMessages.contains(msg)) {
				sortedMessages.add(msg);
			}
		}
		
		return sortedMessages;
	}

}
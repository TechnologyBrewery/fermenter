package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageFactory;
import org.bitbucket.fermenter.stout.messages.Severity;

import junit.framework.TestCase;

public abstract class AbstractMessagesTestCase extends TestCase {

	protected String key1 = "foo";
	protected String key2 = "bar";
	
	protected String property1 = "propertyFoo";
	protected String property2 = "propertyBar";
	protected String property3 = "propertyFoobar";
	
	protected Message createMessage(Severity s, String key, String [] properties) {
		Message msg = MessageFactory.createMessage();
		msg.setKey(key);
		msg.setSeverity(s);
		for (int i=0; i<properties.length; i++) {
			msg.addProperty(properties[i]);
		}
		return msg;
	}
	
}

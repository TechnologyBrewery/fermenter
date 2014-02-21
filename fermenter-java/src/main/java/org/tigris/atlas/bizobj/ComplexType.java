package org.tigris.atlas.bizobj;

import java.math.BigDecimal;

import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.messages.MessageUtils;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.messages.MessagesSet;
import org.tigris.atlas.messages.Severity;

public abstract class ComplexType {

	private Messages messages;

	public Messages getMessages() {
		if (messages == null) {
			messages = MessageFactory.createMessages();
		}

		return messages;
	}

	/**
	 * 
	 * @param key
	 *            Code of the error to add
	 */
	public void addError(String key) {
		addError(key, new Object[] {});
	}

	/**
	 * @param key
	 *            Code of the error to add
	 * @param inserts
	 *            Values to insert into error text
	 */
	public void addError(String key, Object[] inserts) {
		addError(key, new String[] {}, inserts);
	}

	public void addError(String property, String key, Object[] inserts) {
		String[] properties = null;
		if (property != null) {
			properties = new String[] { property };
		}
		addError(key, properties, inserts);
	}

	protected void addError(String key, String[] properties, Object[] inserts) {
		addMessage(key, properties, inserts, Severity.ERROR);
	}

	/**
	 * 
	 * @param key
	 *            Code of the Informational to add
	 */
	public void addInformational(String key) {
		addInformational(key, new Object[] {});
	}

	/**
	 * @param key
	 *            Code of the Informational to add
	 * @param inserts
	 *            Values to insert into Informational text
	 */
	public void addInformational(String key, Object[] inserts) {
		addInformational(key, new String[] {}, inserts);
	}

	public void addInformational(String property, String key, Object[] inserts) {
		String[] properties = null;
		if (property != null) {
			properties = new String[] { property };
		}
		addInformational(key, properties, inserts);
	}

	protected void addInformational(String key, String[] properties, Object[] inserts) {
		addMessage(key, properties, inserts, Severity.INFO);
	}

	protected void addMessage(String key, String[] properties, Object[] inserts, Severity severity) {
		Message message = MessageFactory.createMessage();
		message.setSeverity(severity);
		message.setKey(key);

		int propertiesLength = (properties != null) ? properties.length : 0;
		for (int i = 0; i < propertiesLength; i++) {
			message.addProperty(properties[i]);
		}

		int insertsLength = (inserts != null) ? inserts.length : 0;
		for (int i = 0; i < insertsLength; i++) {
			message.addInsert(inserts[i]);
		}

		getMessages().addMessage(message);
	}

	protected void fieldValidation() {

	}

	protected void compositeValidation() {

	}

	protected void complexValidation() {

	}

	protected void complexValidationOnChildren() {

	}

	protected void complexValidationOnComposites() {

	}

	protected void referenceValidation() {

	}

	public abstract Messages getAllMessages();

	protected abstract void gatherMessages(MessagesSet set);

	protected void validateScale(String property, BigDecimal value, int scale, String label) {
		if (value != null && value.scale() > scale) {
			Message message = MessageUtils.createErrorMessage("invalid.scale", new String[] { property }, new Object[] {
					label, new Integer(scale) });
			getMessages().addMessage(message);
		}
	}

}

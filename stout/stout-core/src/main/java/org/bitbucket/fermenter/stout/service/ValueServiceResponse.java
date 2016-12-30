package org.bitbucket.fermenter.stout.service;

import org.bitbucket.fermenter.stout.messages.Messages;

/**
 * Wraps a value with {@link ServiceResponse} envelope information (e.g., first class support for messages).
 * 
 * @param <T>
 *            The value to wrap
 */
public class ValueServiceResponse<T> extends ServiceResponse {

	private static final long serialVersionUID = -6728447224313998458L;

	private T value;

	/**
	 * Default instance - no way to set messages in this scenario.
	 */
	public ValueServiceResponse() {
	}

	public ValueServiceResponse(T value) {
		setValue(value);
	}

	/**
	 * Creates a new response with the passed values.
	 * 
	 * @param value
	 *            value to wrap
	 * @param messages
	 *            associated messages
	 */
	public ValueServiceResponse(T value, Messages messages) {
		super(messages);
		setValue(value);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}

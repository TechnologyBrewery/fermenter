package org.bitbucket.fermenter.service;

import org.bitbucket.fermenter.messages.Messages;

/**
 * A response that only contains messages.
 */
public class VoidServiceResponse extends ServiceResponse {

	private static final long serialVersionUID = -9121329446101695536L;
	
	/**
	 * Instance without messages.
	 */
	public VoidServiceResponse() {
		super();
	}
	
	/**
	 * Instance with messages.
	 * @param messages
	 */
	public VoidServiceResponse(Messages messages) {
		super(messages);
	}

}

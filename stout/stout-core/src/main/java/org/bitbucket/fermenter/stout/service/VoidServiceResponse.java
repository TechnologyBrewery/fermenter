package org.bitbucket.fermenter.stout.service;

import org.bitbucket.fermenter.stout.messages.Messages;

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

package org.tigris.atlas.service;

import org.tigris.atlas.messages.Messages;

/**
 * A response that only contains messages.
 */
public class VoidServiceResponse extends ServiceResponse {

	private static final long serialVersionUID = -9121329446101695536L;
	
	protected VoidServiceResponse(Messages messages) {
		super(messages);
	}

}

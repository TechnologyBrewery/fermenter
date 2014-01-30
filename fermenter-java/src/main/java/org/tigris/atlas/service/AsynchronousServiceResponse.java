package org.tigris.atlas.service;

import org.tigris.atlas.messages.Messages;

/**
 * The response to asynchronous requests.
 */
public class AsynchronousServiceResponse extends ServiceResponse {

	private static final long serialVersionUID = -2509571671627773756L;
	
	public AsynchronousServiceResponse() {
		super();
	}
	
	protected AsynchronousServiceResponse(Messages messages) {
		super(messages);
	}


}

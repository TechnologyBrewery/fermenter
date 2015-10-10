package org.bitbucket.fermenter.stout.service.json;

import org.bitbucket.fermenter.stout.messages.Messages;
import org.codehaus.jackson.annotate.JsonUnwrapped;

public abstract class ServiceResponseMixIn {

	@JsonUnwrapped
	protected Messages messages;

}

package org.bitbucket.fermenter.service.json;

import org.bitbucket.fermenter.messages.Messages;
import org.codehaus.jackson.annotate.JsonUnwrapped;

public abstract class ServiceResponseMixIn {

	@JsonUnwrapped
	protected Messages messages;

}

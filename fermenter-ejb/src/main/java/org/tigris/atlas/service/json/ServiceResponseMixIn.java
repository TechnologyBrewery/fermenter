package org.tigris.atlas.service.json;

import org.codehaus.jackson.annotate.JsonUnwrapped;
import org.tigris.atlas.messages.Messages;

public abstract class ServiceResponseMixIn {

	@JsonUnwrapped
	protected Messages messages;

}

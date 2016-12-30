package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.service.ServiceResponse;

public abstract class AbstractMsgMgrAwareService {

	protected final void initMsgMgr(Messages messages) {
		if (messages != null) {
			MessageManager.initialize(messages);
		}
	}

	protected final ServiceResponse addAllMsgMgrToResponse(ServiceResponse response) {
		for (Message msg : MessageManager.getMessages().getAllMessages()) {
			response.getMessages().addMessage(msg);
		}
		MessageManager.cleanup();
		return response;
	}
}

package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.HeartbeatService;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageFactory;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.Severity;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the Heartbeat service.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.HeartbeatService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class HeartbeatServiceImpl extends HeartbeatBaseServiceImpl implements HeartbeatService {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void heartbeatImpl() {
        Message message = MessageFactory.createMessage();
        message.setSeverity(Severity.INFO);
        message.setKey("heartbeat");
        // nothing to do here, this is just to check that the server and service responds
        // primarily implemented to support testing a get/supports call with a void response
        MessageManager.addMessage(message);
    }

}
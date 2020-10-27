package org.bitbucket.fermenter.stout.messages;

import org.bitbucket.fermenter.stout.service.ServiceResponse;
import org.springframework.stereotype.Service;

@Service
public class MessageManagerAwareService {

    public void initializeMessageManager(Messages messages) {
        if (messages != null) {
            MessageManager.initialize(messages);
        }
    }

    public ServiceResponse addAllMessagesToResponse(ServiceResponse response) {
        Messages messages = MessageManager.getMessages();
        response.getMessages().addMessages(messages);
        MessageManager.cleanup();
        return response;
    }

    public void addAllMessages(ServiceResponse response) {
        MessageManager.addMessages(response.getMessages());
    }

    public void addAllMessages(Messages messages) {
        MessageManager.addMessages(messages);
    }

}

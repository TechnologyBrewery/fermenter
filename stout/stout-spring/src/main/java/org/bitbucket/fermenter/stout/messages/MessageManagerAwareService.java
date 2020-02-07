package org.bitbucket.fermenter.stout.messages;

import java.util.Collection;

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
        Collection<Message> message = messages.getAllMessages();
        for (Message msg : message) {
            response.getMessages().addMessage(msg);
        }
        MessageManager.cleanup();
        return response;
    }

}

package org.technologybrewery.fermenter.stout.service;

import org.technologybrewery.fermenter.stout.messages.AbstractMessageManagerAwareService;
import org.technologybrewery.fermenter.stout.messages.CoreMessages;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.ServiceMessages;
import org.technologybrewery.fermenter.stout.messages.Severity;

/**
 * Common functionality for Entity Maintenance Services.
 */
public abstract class AbstractEntityMaintenanceService extends AbstractMessageManagerAwareService {

    protected void addInvalidKeyErrorMessage(Object keyValue) {
        Message message = new Message(CoreMessages.INVALID_ENTITY_KEY, Severity.ERROR);
        message.addInsert("keyName", keyValue);
        MessageManager.addMessage(message);
    }
    
    protected void addBulkOperationErrorMessage(String operation, Object keyList) {
        Message message = new Message(ServiceMessages.BULK_OPERATION_FAILED, Severity.ERROR);
        message.addInsert("operation", operation);
        message.addInsert("keyList", keyList);
        MessageManager.addMessage(message);
    }    

}

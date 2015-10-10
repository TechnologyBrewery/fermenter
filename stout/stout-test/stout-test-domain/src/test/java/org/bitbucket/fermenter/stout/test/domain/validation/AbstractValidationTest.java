package org.bitbucket.fermenter.stout.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.stout.test.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExample;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractValidationTest {

    @Before
    public void initializeMessageManager() throws Exception {
	MessageManagerInitializationDelegate.initializeMessageManager();
    }

    @After
    public void cleanupMessageManager() throws Exception {
	MessageManagerInitializationDelegate.cleanupMessageManager();
    }

    protected void verifyNumberOfErrors(Collection<Message> fieldLevelList, int expectedSize) {
	assertEquals("The wrong number of errors exists!", expectedSize, fieldLevelList.size());
    }

    protected Collection<Message> verifyFieldLevelList(Messages messages, String fieldIndicator) {
	Collection<Message> fieldLevelList = messages.getErrorMessages(fieldIndicator);
	assertNotNull("No field level errors available!", fieldLevelList);
	return fieldLevelList;
    }

    protected Messages verifyMessages() {
	Messages messages = MessageManager.getMessages();
	assertNotNull("No messages collected!", messages);
	return messages;
    }

    protected ValidationExampleBO getValidationExampleBO() {
	ValidationExampleBO ve = (ValidationExampleBO) BusinessObjectFactory
		.createBusinessObject(ValidationExample.ENTITY);
	ve.setRequiredField("requiredValue");
	return ve;
    }

}

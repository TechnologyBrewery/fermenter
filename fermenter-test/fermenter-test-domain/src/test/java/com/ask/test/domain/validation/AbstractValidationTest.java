package com.ask.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.tigris.atlas.bizobj.BusinessObject;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.ValidationExampleBO;
import com.ask.test.domain.transfer.ValidationExample;

public abstract class AbstractValidationTest {

	protected void verifyNumberOfErrors(Collection<Message> fieldLevelList, int expectedSize) {
		assertEquals("The wrong number of errors exists!", expectedSize, fieldLevelList.size());
	}

	protected Collection<Message> verifyFieldLevelList(Messages messages, String fieldIndicator) {
		Collection<Message> fieldLevelList = messages.getErrorMessages(fieldIndicator);
	    assertNotNull("No field level errors available!", fieldLevelList);
		return fieldLevelList;
	}

	protected Messages verifyErrorList(BusinessObject ve) {
		Messages messages = ve.getMessages();
	    assertNotNull("Not error list returned!", messages);
		return messages;
	}

	protected ValidationExampleBO getValidationExampleBO() {
		ValidationExampleBO ve = (ValidationExampleBO)BusinessObjectFactory.createBusinessObject( ValidationExample.ENTITY );
		ve.setRequiredField("requiredValue");		
		return ve;
	}

}

package org.bitbucket.fermenter.stout.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.test.domain.bizobj.ValidationExampleBO;

public class TestRequiredValidation extends AbstractValidationTest {

    public void testRequiredWithValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setRequiredField("iAmRequired");

	ve.validate();

	Messages messages = verifyMessages();
	assertFalse(messages.hasErrorMessages());
    }

    public void testRequiredWithoutValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setRequiredField(null);
	ve.validate();

	String fieldIndicator = "requiredField";
	Messages messages = verifyMessages();
	Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
	verifyNumberOfErrors(fieldLevelList, 1);

	Message e = messages.getErrorMessages(fieldIndicator).iterator().next();
	assertEquals("null.not.allowed", e.getKey());
	assertEquals("required field", e.getInserts().iterator().next());
    }

}

package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestRequiredValidation extends AbstractValidationTest {

	@Test
	public void testRequiredWithValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("iAmRequired");

		ve.validate();

		Messages messages = verifyMessages();
		assertFalse(messages.hasErrorMessages());
	}

	@Test
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

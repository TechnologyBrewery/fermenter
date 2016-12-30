package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestChildValidations extends AbstractValidationTest {

	@Test
	public void testCallValidateMultipleTimes() {
		ValidationExampleBO bo = new ValidationExampleBO();
		bo.setStringExample("111111111122222222223");
		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals(2, messages.getErrorMessageCount());

		bo.validate();

		messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals(2, messages.getErrorMessageCount());
	}

	@Test
	public void testSuccessfulValidation() {
		ValidationExampleBO bo = new ValidationExampleBO();
		bo.setRequiredField("TEST");

		ValidationExampleChildBO child = new ValidationExampleChildBO();
		child.setRequiredField("foo");
		bo.addValidationExampleChild(child);

		bo.validate();

		Messages messages = verifyMessages();
		assertFalse(messages.hasErrorMessages());
		assertEquals(0, messages.getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulParentValidation() {
		ValidationExampleBO bo = new ValidationExampleBO();

		ValidationExampleChildBO child = new ValidationExampleChildBO();
		child.setRequiredField("foo");
		bo.addValidationExampleChild(child);

		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals(1, messages.getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulChildValidation() {
		ValidationExampleBO bo = new ValidationExampleBO();
		bo.setRequiredField("TEST");

		ValidationExampleChildBO child = new ValidationExampleChildBO();
		bo.addValidationExampleChild(child);

		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals(1, messages.getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulParentChildValidation() {
		ValidationExampleBO bo = new ValidationExampleBO();

		ValidationExampleChildBO child = new ValidationExampleChildBO();
		bo.addValidationExampleChild(child);

		bo.validate();
		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals(2, messages.getErrorMessageCount());
	}

}

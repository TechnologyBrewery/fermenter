package com.ask.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.ValidationExampleBO;
import com.ask.test.domain.bizobj.ValidationExampleChildBO;

public class TestChildValidations extends AbstractValidationTest {

    @Test
    public void testCallValidateMultipleTimes() {
	ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
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
	ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
	bo.setRequiredField("TEST");

	ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
	child.setRequiredField("foo");
	bo.addValidationExampleChild(child);

	bo.validate();

	Messages messages = verifyMessages();
	assertFalse(messages.hasErrorMessages());
	assertEquals(0, messages.getErrorMessageCount());
    }

    @Test
    public void testUnsuccessfulParentValidation() {
	ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();

	ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
	child.setRequiredField("foo");
	bo.addValidationExampleChild(child);

	bo.validate();

	Messages messages = verifyMessages();
	assertTrue(messages.hasErrorMessages());
	assertEquals(1, messages.getErrorMessageCount());
    }

    @Test
    public void testUnsuccessfulChildValidation() {
	ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
	bo.setRequiredField("TEST");

	ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
	bo.addValidationExampleChild(child);

	bo.validate();

	Messages messages = verifyMessages();
	assertTrue(messages.hasErrorMessages());
	assertEquals(1, messages.getErrorMessageCount());
    }

    @Test
    public void testUnsuccessfulParentChildValidation() {
	ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();

	ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
	bo.addValidationExampleChild(child);

	bo.validate();
	Messages messages = verifyMessages();
	assertTrue(messages.hasErrorMessages());
	assertEquals(2, messages.getErrorMessageCount());
    }

}

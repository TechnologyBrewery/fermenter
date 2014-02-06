package com.ask.test.domain.validation;

import junit.framework.TestCase;

import org.junit.Test;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.ValidationExampleBO;
import com.ask.test.domain.bizobj.ValidationExampleChildBO;

public class TestChildValidations extends TestCase {
	
	@Test
	public void testCallValidateMultipleTimes() {
		ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
		bo.setStringExample("111111111122222222223");
		bo.validate();
		
		assertTrue(bo.getMessages().hasErrorMessages());
		assertEquals(2, bo.getMessages().getErrorMessageCount());
		
		bo.validate();
		
		assertTrue(bo.getMessages().hasErrorMessages());
		assertEquals(2, bo.getMessages().getErrorMessageCount());
	}

	@Test
	public void testSuccessfulValidation() {
		ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
		bo.setRequiredField("TEST");
		
		ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
		child.setRequiredField("foo");
		bo.addValidationExampleChild(child);
		
		bo.validate();
		assertFalse(bo.getMessages().hasErrorMessages());
		assertFalse(child.getMessages().hasErrorMessages());
		assertEquals(0, bo.getMessages().getErrorMessageCount());
		assertEquals(0, child.getMessages().getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulParentValidation() {
		ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
		
		ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
		child.setRequiredField("foo");
		bo.addValidationExampleChild(child);
		
		bo.validate();
		assertTrue(bo.getMessages().hasErrorMessages());
		assertFalse(child.getMessages().hasErrorMessages());
		assertEquals(1, bo.getMessages().getErrorMessageCount());
		assertEquals(1, bo.getAllMessages().getErrorMessageCount());
		assertEquals(0, child.getMessages().getErrorMessageCount());
		assertEquals(1, child.getAllMessages().getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulChildValidation() {
		ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
		bo.setRequiredField("TEST");
		
		ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
		bo.addValidationExampleChild(child);
		
		bo.validate();
		assertFalse(bo.getMessages().hasErrorMessages());
		assertTrue(bo.getAllMessages().hasErrorMessages());
		assertTrue(child.getMessages().hasErrorMessages());
		assertTrue(child.getAllMessages().hasErrorMessages());
		assertEquals(0, bo.getMessages().getErrorMessageCount());
		assertEquals(1, bo.getAllMessages().getErrorMessageCount());
		assertEquals(1, child.getMessages().getErrorMessageCount());
		assertEquals(1, child.getAllMessages().getErrorMessageCount());
	}

	@Test
	public void testUnsuccessfulParentChildValidation() {
		ValidationExampleBO bo = BusinessObjectFactory.createValidationExampleBO();
		
		ValidationExampleChildBO child = BusinessObjectFactory.createValidationExampleChildBO();
		bo.addValidationExampleChild(child);
		
		bo.validate();
		assertTrue(bo.getMessages().hasErrorMessages());
		assertTrue(child.getMessages().hasErrorMessages());
		assertEquals(1, bo.getMessages().getErrorMessageCount());
		assertEquals(1, child.getMessages().getErrorMessageCount());
		assertEquals(2, bo.getAllMessages().getErrorMessageCount());
		assertEquals(2, child.getAllMessages().getErrorMessageCount());
	}
	
}

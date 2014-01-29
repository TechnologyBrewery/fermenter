package com.ask.test.pojo.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.tigris.atlas.messages.Message;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.ValidationExampleBaseBO;

public class TestScale {

	private ValidationExampleBaseBO bo;
	
	@Before
	public void setUp() throws Exception {
		bo = BusinessObjectFactory.createValidationExampleBO();
		bo.setRequiredField("foo");
	}
	
	@Test
	public void testNullBigDecimals() {
		bo.validate();
		
		assertFalse(bo.getMessages().hasErrorMessages());
	}
	
	@Test
	public void testValidScales() {
		bo.setBigDecimalExample(new BigDecimal("23.45"));
		bo.setBigDecimalExampleWithScale(new BigDecimal("22.345"));
		
		bo.validate();
		
		assertFalse(bo.getMessages().hasErrorMessages());
	}
	
	@Test
	public void testInvalidScale() {
		bo.setBigDecimalExample(new BigDecimal("-1234.3463634346"));
		bo.setBigDecimalExampleWithScale(new BigDecimal("-1234.3463634346"));

		bo.validate();
		
		assertTrue(bo.getMessages().hasErrorMessages());
		assertTrue(bo.getMessages().hasErrorMessages("bigDecimalExampleWithScale"));
		assertEquals("invalid.scale", ((Message)bo.getMessages().getErrorMessages().iterator().next()).getKey());
	}
	
}

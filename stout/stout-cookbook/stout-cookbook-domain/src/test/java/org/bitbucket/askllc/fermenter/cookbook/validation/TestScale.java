package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestScale extends AbstractValidationTest {

	private ValidationExampleBO bo;

	@Before
	public void setUp() throws Exception {
		bo = new ValidationExampleBO();
		bo.setRequiredField("foo");
	}

	@Test
	public void testNullBigDecimals() {
		bo.validate();

		Messages messages = verifyMessages();
		assertFalse(messages.hasErrorMessages());
	}

	@Test
	public void testValidScales() {
		bo.setBigDecimalExample(new BigDecimal("23.45"));
		bo.setBigDecimalExampleWithScale(new BigDecimal("22.345"));

		bo.validate();

		Messages messages = verifyMessages();
		assertFalse(messages.hasErrorMessages());
	}

	@Test
	public void testInvalidScale() {
		bo.setBigDecimalExample(new BigDecimal("-1234.3463634346"));
		bo.setBigDecimalExampleWithScale(new BigDecimal("-1234.3463634346"));

		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertTrue(messages.hasErrorMessages("ValidationExample.bigDecimalExampleWithScale"));
		assertEquals("invalid.scale", ((Message) messages.getErrorMessages().iterator().next()).getKey());
	}

}

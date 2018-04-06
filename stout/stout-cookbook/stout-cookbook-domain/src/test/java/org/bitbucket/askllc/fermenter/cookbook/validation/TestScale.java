package org.bitbucket.askllc.fermenter.cookbook.validation;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml", "classpath:h2-spring-ds-context.xml" })
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
		bo.setBigDecimalExampleWithLargeScale(new BigDecimal("342424.1234567891"));
		bo.setBigDecimalExampleWithLargeScaleInteger(new BigDecimal("192839126.12"));

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
		assertEquals("bigDecimalExampleWithScale",messages.getErrorMessages().iterator().next().getInserts().toArray()[0]);
		assertEquals("invalid.field", ((Message) messages.getErrorMessages().iterator().next()).getKey());
	}

	@Test
	public void testInvalidLargeScale() throws Exception {

		bo.setBigDecimalExampleWithLargeScale(new BigDecimal("-1234.93463634346"));
		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals("bigDecimalExampleWithLargeScale",messages.getErrorMessages().iterator().next().getInserts().toArray()[0]);
		assertEquals("invalid.field", ((Message) messages.getErrorMessages().iterator().next()).getKey());
	}
	@Test
	public void testInvalidLargeIntegerScale() throws Exception {

		bo.setBigDecimalExampleWithLargeScaleInteger(new BigDecimal("11192839126.11"));
		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals("bigDecimalExampleWithLargeScaleInteger",messages.getErrorMessages().iterator().next().getInserts().toArray()[0]);
		assertEquals("invalid.field", ((Message) messages.getErrorMessages().iterator().next()).getKey());
	}
	@Test
	public void testInvalidLargeNegativeIntegerScale() throws Exception {

		bo.setBigDecimalExampleWithLargeScaleInteger(new BigDecimal("-11192839126.11"));
		bo.validate();

		Messages messages = verifyMessages();
		assertTrue(messages.hasErrorMessages());
		assertEquals("bigDecimalExampleWithLargeScaleInteger",messages.getErrorMessages().iterator().next().getInserts().toArray()[0]);
		assertEquals("invalid.field", ((Message) messages.getErrorMessages().iterator().next()).getKey());
	}

}

package org.bitbucket.fermenter.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationExampleBaseBO;
import org.junit.Before;
import org.junit.Test;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

public class TestScale extends AbstractValidationTest {

    private ValidationExampleBaseBO bo;

    @Before
    public void setUp() throws Exception {
	bo = BusinessObjectFactory.createValidationExampleBO();
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

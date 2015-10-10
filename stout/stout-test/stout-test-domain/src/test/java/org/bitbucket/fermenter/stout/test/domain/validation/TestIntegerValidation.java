package org.bitbucket.fermenter.stout.test.domain.validation;

import org.bitbucket.fermenter.stout.test.domain.bizobj.ValidationExampleBO;
import org.junit.Test;

public class TestIntegerValidation extends AbstractNumericValidationTest {

    public final Integer INTEGER_MAX_LENGTH = new Integer(12345);
    public final Integer INTEGER_MIN_LENGTH = new Integer(-12345);

    @Test
    public void testIntegerOverMaxValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(new Integer(Integer.MAX_VALUE));
	String fieldIndicator = "integerExample";

	performNumericOverMaxTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MAX_LENGTH);
    }

    @Test
    public void testIntegerUnderMinValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(new Integer(Integer.MIN_VALUE));
	String fieldIndicator = "integerExample";

	performNumericUnderMinTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MIN_LENGTH);
    }

    @Test
    public void testIntegerAtMaxValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(INTEGER_MAX_LENGTH);

	performNumericAtOrUnderMaxTest(ve, ve.getIntegerExample(), INTEGER_MAX_LENGTH);
    }

    @Test
    public void testIntegerAtMinValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(INTEGER_MIN_LENGTH);

	performNumericAtOrOverMinTest(ve, ve.getIntegerExample(), INTEGER_MIN_LENGTH);
    }

    @Test
    public void testIntegerUnderMaxValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(new Integer(12345));

	performNumericAtOrUnderMaxTest(ve, ve.getIntegerExample(), INTEGER_MAX_LENGTH);
    }

    @Test
    public void testIntegerOverMinValue() {
	ValidationExampleBO ve = getValidationExampleBO();
	ve.setIntegerExample(new Integer(-12345));

	performNumericAtOrOverMinTest(ve, ve.getIntegerExample(), INTEGER_MIN_LENGTH);
    }
}

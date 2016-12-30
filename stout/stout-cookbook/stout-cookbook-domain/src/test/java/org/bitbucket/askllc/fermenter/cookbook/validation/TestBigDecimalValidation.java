package org.bitbucket.askllc.fermenter.cookbook.validation;

import java.math.BigDecimal;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestBigDecimalValidation extends AbstractNumericValidationTest {

	public final BigDecimal BIG_DECIMAL_MAX_LENGTH = new BigDecimal("123456789.123456789");
	public final BigDecimal BIG_DECIMAL_MIN_LENGTH = new BigDecimal("-123456789.123456789");

	@Test
	public void testBigDecimalOverMaxValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(new BigDecimal("2345678913.9"));
		String fieldIndicator = "bigDecimalExample";

		performNumericOverMaxTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MAX_LENGTH);
	}

	@Test
	public void testBigDecimalUnderMinValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(new BigDecimal("-234567891.9"));
		String fieldIndicator = "bigDecimalExample";

		performNumericUnderMinTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MIN_LENGTH);
	}

	@Test
	public void testBigDecimalAtMaxValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(BIG_DECIMAL_MAX_LENGTH);

		performNumericAtOrUnderMaxTest(ve, ve.getBigDecimalExample(), BIG_DECIMAL_MAX_LENGTH);
	}

	@Test
	public void testBigDecimalAtMinValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(BIG_DECIMAL_MIN_LENGTH);

		performNumericAtOrOverMinTest(ve, ve.getBigDecimalExample(), BIG_DECIMAL_MIN_LENGTH);
	}

	@Test
	public void testBigDecimalUnderMaxValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(new BigDecimal(55.5));

		performNumericAtOrUnderMaxTest(ve, ve.getBigDecimalExample(), BIG_DECIMAL_MAX_LENGTH);
	}

	@Test
	public void testBigDecimalOverMinValue() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setBigDecimalExample(new BigDecimal(15.5));

		performNumericAtOrOverMinTest(ve, ve.getBigDecimalExample(), BIG_DECIMAL_MIN_LENGTH);
	}
}

package com.ask.test.domain.validation;

import java.math.BigDecimal;

import org.junit.Test;

import com.ask.test.domain.bizobj.ValidationExampleBO;

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
        String fieldIndicator = "bigDecimalExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MAX_LENGTH);
    }  
    
	@Test
    public void testBigDecimalAtMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setBigDecimalExample(BIG_DECIMAL_MIN_LENGTH);
        String fieldIndicator = "bigDecimalExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MIN_LENGTH);
    }    
    
	@Test
    public void testBigDecimalUnderMaxValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setBigDecimalExample(new BigDecimal(55.5));
        String fieldIndicator = "bigDecimalExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MAX_LENGTH);
    }  
    
	@Test
    public void testBigDecimalOverMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();               
        ve.setBigDecimalExample(new BigDecimal(15.5));
        String fieldIndicator = "bigDecimalExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getBigDecimalExample(), BIG_DECIMAL_MIN_LENGTH);
    } 	
}

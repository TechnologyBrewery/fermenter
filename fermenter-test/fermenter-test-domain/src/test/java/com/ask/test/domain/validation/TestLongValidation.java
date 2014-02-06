package com.ask.test.domain.validation;

import org.junit.Test;

import com.ask.test.domain.bizobj.ValidationExampleBO;

public class TestLongValidation extends AbstractNumericValidationTest {
	
	public final Long LONG_MAX_LENGTH = new Long(123456789);
	public final Long LONG_MIN_LENGTH = new Long(-123456789);

	@Test
    public void testLongOverMaxValue() {
        ValidationExampleBO ve = getValidationExampleBO();        
        ve.setLongExample(new Long(Long.MAX_VALUE));
        String fieldIndicator = "longExample";   
        
        performNumericOverMaxTest(ve, fieldIndicator, ve.getLongExample(), LONG_MAX_LENGTH);
    }
    
	@Test
    public void testLongUnderMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setLongExample(new Long(Long.MIN_VALUE));
        String fieldIndicator = "longExample";
        
        performNumericUnderMinTest(ve, fieldIndicator, ve.getLongExample(), LONG_MIN_LENGTH);
    }    
    
	@Test
    public void testLongAtMaxValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setLongExample(LONG_MAX_LENGTH);
        String fieldIndicator = "longExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getLongExample(), LONG_MAX_LENGTH);
    }  
    
	@Test
    public void testLongAtMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setLongExample(LONG_MIN_LENGTH);
        String fieldIndicator = "longExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getLongExample(), LONG_MIN_LENGTH);
    }    
    
	@Test
    public void testLongUnderMaxValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setLongExample(new Long(12345));
        String fieldIndicator = "longExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getLongExample(), LONG_MAX_LENGTH);
    }  
    
	@Test
    public void testLongOverMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();               
        ve.setLongExample(new Long(-12345));
        String fieldIndicator = "longExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getLongExample(), LONG_MIN_LENGTH);
    } 	
}

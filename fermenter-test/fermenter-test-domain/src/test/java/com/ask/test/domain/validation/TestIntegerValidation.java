package com.ask.test.domain.validation;

import org.junit.Test;

import com.ask.test.domain.bizobj.ValidationExampleBO;

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
        String fieldIndicator = "integerExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MAX_LENGTH);
    }  
    
	@Test
    public void testIntegerAtMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setIntegerExample(INTEGER_MIN_LENGTH);
        String fieldIndicator = "integerExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MIN_LENGTH);
    }    
    
	@Test
    public void testIntegerUnderMaxValue() {
        ValidationExampleBO ve = getValidationExampleBO();      
        ve.setIntegerExample(new Integer(12345));
        String fieldIndicator = "integerExample";   
        
        performNumericAtOrUnderMaxTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MAX_LENGTH);
    }  
    
	@Test
    public void testIntegerOverMinValue() {
        ValidationExampleBO ve = getValidationExampleBO();               
        ve.setIntegerExample(new Integer(-12345));
        String fieldIndicator = "integerExample";   
        
        performNumericAtOrOverMinTest(ve, fieldIndicator, ve.getIntegerExample(), INTEGER_MIN_LENGTH);
    } 	
}

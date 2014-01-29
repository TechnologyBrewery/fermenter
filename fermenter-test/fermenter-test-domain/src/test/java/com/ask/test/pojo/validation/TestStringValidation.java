package com.ask.test.pojo.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.ValidationExampleBO;

public class TestStringValidation extends AbstractValidationTest {
	
	private final int STRING_MAX_LENGTH = 20;
	private final int STRING_MIN_LENGTH = 2;	
	
    public void testLengthOverMax() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("abcdefghijklmnopqrstuvwxyz");
        
        //sanity check:
        verifyInvalidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);
        
        ve.validate();
        
        //ensure the correct number of errors:
        String fieldIndicator = "stringExample";        
        Messages messages = verifyErrorList(ve);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)ve.getMessages().getErrorMessages(fieldIndicator).iterator().next();
        assertEquals("value.must.be.shorter.than", e.getKey());
        Iterator<Object> i = e.getInserts().iterator();
        assertEquals(fieldIndicator, i.next());
        assertEquals(ve.getStringExample(), i.next());
        assertEquals(new Integer(STRING_MAX_LENGTH), i.next());
    }
    
    public void testLengthAtMax() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("01234567890123456789");
        
        //sanity check:
        verifyValidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);
        
        ve.validate();
        
        //ensure that no errors were returned:
        assertFalse(ve.getMessages().hasErrorMessages());
        
    }  
    
    public void testLengthUnderMax() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("0123456789");
        
        //sanity check:
        verifyValidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);
        
        ve.validate();
        
        //ensure that no errors were returned:
        assertFalse(ve.getMessages().hasErrorMessages());
        
    }      
    
    public void testLengthUnderMin() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("a");
        
        //sanity check:
        verifyInvalidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);
        
        ve.validate();
        
        //ensure the correct number of errors:
        String fieldIndicator = "stringExample";        
        Messages messages = verifyErrorList(ve);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)ve.getMessages().getErrorMessages(fieldIndicator).iterator().next();
        assertEquals("value.must.be.longer.than", e.getKey());
        Iterator<Object> i = e.getInserts().iterator();
        assertEquals(fieldIndicator, i.next());
        assertEquals(ve.getStringExample(), i.next());
        assertEquals(new Integer(STRING_MIN_LENGTH), i.next());
    }    
    
    public void testLengthAtMin() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("01");
        
        //sanity check:
        verifyValidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);
        
        ve.validate();
        
        //ensure that no errors were returned:
        assertFalse(ve.getMessages().hasErrorMessages());
        
    }  
    
    public void testLengthOverMin() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");        
        ve.setStringExample("0123456789");
        
        //sanity check:
        verifyValidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);
        
        ve.validate();
        
        //ensure that no errors were returned:
        assertFalse(ve.getMessages().hasErrorMessages());
        
    }
    
    public void testTrimLeadingWhiteSpace() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");  
        
        String baseString = "abcd";
        String whitespace = "    ";
        ve.setStringExample(whitespace + baseString);
        
        assertEquals("Trimming has not returned the correct value",
        	ve.getStringExample(), baseString);
        
    }
    
    public void testTrimTrailingWhiteSpace() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");  
        
        String baseString = "abcd";
        String whitespace = "    ";
        ve.setStringExample(baseString + whitespace);
        
        assertEquals("Trimming has not returned the correct value",
        	ve.getStringExample(), baseString);
        
    }  
    
    public void testTrimAllWhiteSpace() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");  
        
        String whitespace = "    ";
        ve.setStringExample(whitespace);
        
        assertEquals("Trimming has not returned the correct value",
        	ve.getStringExample(), null);
        
    }   
    
    public void testTrimAllOnNull() {
        ValidationExampleBO ve = getValidationExampleBO();
        ve.setRequiredField("requiredValue");  
        ve.setStringExample(null);
        
        assertEquals("Trimming has not returned the correct value",
        	ve.getStringExample(), null);
        
    }     
    
    //////////////////////////////////////////////////////////////////////
    // Helper methods:
    //////////////////////////////////////////////////////////////////////      
    
	private void verifyInvalidMaxData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(i > limit));
	}    
	
	private void verifyValidMaxData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(i <= limit));
	}  	
	
	private void verifyInvalidMinData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(i < limit));
	}    
	
	private void verifyValidMinData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(i >= limit));
	}
}

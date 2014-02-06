package com.ask.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.ValidationExampleBO;

public class TestRequiredValidation extends AbstractValidationTest {

	   public void testRequiredWithValue() {    	
	        ValidationExampleBO ve = getValidationExampleBO();
	        ve.setRequiredField("iAmRequired");
	        
	        ve.validate();

	        assertFalse(ve.getMessages().hasErrorMessages());
	    }

	    public void testRequiredWithoutValue() {
	        ValidationExampleBO ve = getValidationExampleBO();
	        ve.setRequiredField(null);
	        ve.validate();
	        
	        String fieldIndicator = "requiredField";        
	        Messages messages = verifyErrorList(ve);
	        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
	        verifyNumberOfErrors(fieldLevelList, 1);

	        Message e = (Message)ve.getMessages().getErrorMessages(fieldIndicator).iterator().next();
	        assertEquals("null.not.allowed", e.getKey());
	        assertEquals("required field", e.getInserts().iterator().next());
	    }	
	
}

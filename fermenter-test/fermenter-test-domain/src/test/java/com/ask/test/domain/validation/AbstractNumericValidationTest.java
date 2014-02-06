package com.ask.test.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.ValidationExampleBO;

public abstract class AbstractNumericValidationTest extends AbstractValidationTest {
    
	protected void performNumericOverMaxTest(ValidationExampleBO ve, String fieldIndicator, Comparable value, Object maxLength) {
		//sanity check:
        verifyInvalidMaxData(value, maxLength);
        
        ve.validate();
        
        //ensure the correct number of errors:             
        Messages messages = verifyErrorList(ve);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)ve.getMessages().getErrorMessages(fieldIndicator).iterator().next();
        Iterator<Object> i = e.getInserts().iterator();
        assertEquals("value.must.be.lt.max", e.getKey());
        assertEquals(fieldIndicator, i.next());
        assertEquals(value, i.next());
        assertTrue(((Comparable)maxLength).compareTo(i.next()) == 0);
	}
	
	protected void performNumericUnderMinTest(ValidationExampleBO ve, String fieldIndicator, Comparable value, Object minLength) {
		//sanity check:
        verifyInvalidMinData(value, minLength);
        
        ve.validate();
        
        //ensure the correct number of errors:             
        Messages messages = verifyErrorList(ve);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)ve.getMessages().getErrorMessages(fieldIndicator).iterator().next();
        assertEquals("value.must.be.gt.min", e.getKey());
        Iterator<Object> i = e.getInserts().iterator();
        assertEquals(fieldIndicator, i.next());
        assertEquals(value, i.next());
        assertTrue(((Comparable)minLength).compareTo(i.next()) == 0);
	}	
	
	protected void performNumericAtOrUnderMaxTest(ValidationExampleBO ve, String fieldIndicator, Comparable value, Object minLength) {
		//sanity check:
        verifyValidMaxData(value, minLength);
        
        ve.validate();
        
        assertFalse("No errrors should have been returned!", ve.getMessages().hasErrorMessages());
	}	
	
	protected void performNumericAtOrOverMinTest(ValidationExampleBO ve, String fieldIndicator, Comparable value, Object maxLength) {
		//sanity check:
        verifyValidMinData(value, maxLength);
        
        ve.validate();
        
        assertFalse("No errrors should have been returned!", ve.getMessages().hasErrorMessages());
	}		  	
	
	protected void verifyInvalidMaxData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(c.compareTo(limit) == 1));
	}   
	
	protected void verifyInvalidMinData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(c.compareTo(limit) == -1));
	} 	
	
	protected void verifyValidMaxData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(c.compareTo(limit) <= 0));
	}
	
	protected void verifyValidMinData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", 
        	(c.compareTo(limit) >= 0));
	}    	
	
}

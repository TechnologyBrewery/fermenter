package com.ask.test.pojo.validation;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.ValidationReferenceExampleBO;
import com.ask.test.domain.bizobj.ValidationReferencedObjectBO;

public class TestReferenceValidation extends AbstractValidationTest {

	public void testWithNullReference() {		
		//create a new object without a reference:
		ValidationReferenceExampleBO vre = getValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(null);
		
		((ValidationReferenceExampleBO)vre).validate();

        //ensure the correct number of errors:
        String refIndicator = "requiredReference";        
        Messages messages = verifyErrorList(vre);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)vre.getMessages().getErrorMessages(refIndicator).iterator().next();
        assertEquals("null.not.allowed", e.getKey());
        assertEquals(refIndicator, e.getInserts().iterator().next());
        
	}
	
	public void testWithInvalidReference() {	
		ValidationReferencedObjectBO vro = getValidationReferencedObjectBO();
		vro.setSomeDataField("invalidRefData");		
		
		//create a new object with this reference:
		ValidationReferenceExampleBO vre = getValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(vro);
		
		((ValidationReferenceExampleBO)vre).validate();

        //ensure the correct number of errors:
        String refIndicator = "requiredReference";        
        Messages messages = verifyErrorList(vre);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)vre.getMessages().getErrorMessages(refIndicator).iterator().next();
        assertEquals("invalid.reference", e.getKey());
        assertEquals(refIndicator, e.getInserts().iterator().next());
        
	}
	
	public void testWithNullKeyReference() {	
		ValidationReferencedObjectBO vro = getValidationReferencedObjectBO();
		vro.setSomeDataField("invalidRefData");
		
		//create a new object with this reference:
		ValidationReferenceExampleBO vre = getValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(vro);
		
		((ValidationReferenceExampleBO)vre).validate();

        //ensure the correct number of errors:
        String refIndicator = "requiredReference";        
        Messages messages = verifyErrorList(vre);
        Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
        verifyNumberOfErrors(fieldLevelList, 1);
        
        //ensure that error is well formed
        Message e = (Message)vre.getMessages().getErrorMessages(refIndicator).iterator().next();
        assertEquals("invalid.reference", e.getKey());
        assertEquals(refIndicator, e.getInserts().iterator().next());
        
	}		

	protected ValidationReferenceExampleBO getValidationReferenceExampleBO() {
		ValidationReferenceExampleBO vre = (ValidationReferenceExampleBO)
			BusinessObjectFactory.createValidationReferenceExampleBO();
		return vre;
	}	

	protected ValidationReferencedObjectBO getValidationReferencedObjectBO() {
		ValidationReferencedObjectBO vro = (ValidationReferencedObjectBO)
			BusinessObjectFactory.createValidationReferencedObjectBO();
		return vro;
	}

}

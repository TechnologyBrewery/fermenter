package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferenceExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestReferenceValidation extends AbstractValidationTest {

	@Test
	public void testWithNullReference() {
		// create a new object without a reference:
		ValidationReferenceExampleBO vre = new ValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(null);

		vre.validate();

		// ensure the correct number of errors:
		String refIndicator = "requiredReference";
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrorMessages(refIndicator).iterator().next();
		assertEquals("null.not.allowed", e.getKey());
		assertEquals(refIndicator, e.getInserts().iterator().next());

	}

	@Test
	public void testWithInvalidReference() {
		ValidationReferencedObjectBO vro = new ValidationReferencedObjectBO();
		vro.setSomeDataField("invalidRefData");

		// create a new object with this reference:
		ValidationReferenceExampleBO vre = new ValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(vro);

		vre.validate();

		// ensure the correct number of errors:
		String refIndicator = "requiredReference";
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrorMessages(refIndicator).iterator().next();
		assertEquals("invalid.reference", e.getKey());
		assertEquals(refIndicator, e.getInserts().iterator().next());

	}

	@Test
	public void testWithNullKeyReference() {
		ValidationReferencedObjectBO vro = new ValidationReferencedObjectBO();
		vro.setSomeDataField("invalidRefData");

		// create a new object with this reference:
		ValidationReferenceExampleBO vre = new ValidationReferenceExampleBO();
		vre.setSomeDataField("exampleWithValidReference");
		vre.setRequiredReference(vro);

		vre.validate();

		// ensure the correct number of errors:
		String refIndicator = "requiredReference";
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages, refIndicator);
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrorMessages(refIndicator).iterator().next();
		assertEquals("invalid.reference", e.getKey());
		assertEquals(refIndicator, e.getInserts().iterator().next());

	}

}

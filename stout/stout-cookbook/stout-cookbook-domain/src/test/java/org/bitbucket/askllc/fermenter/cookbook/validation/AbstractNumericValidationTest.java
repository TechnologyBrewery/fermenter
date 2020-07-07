package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;

public abstract class AbstractNumericValidationTest extends AbstractValidationTest {

	protected void performNumericOverMaxTest(ValidationExampleBO ve, String fieldIndicator, Comparable value,
			Object maxLength) {
		// sanity check:
		verifyInvalidMaxData(value, maxLength);

		ve.validate();

		// ensure the correct number of errors:
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages,
				getExpectedMessagePropertyKey(fieldIndicator));
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrors().iterator().next();
		Iterator<Entry<String, String>> i = e.getAllInserts().iterator();
		assertEquals("value.must.be.lt.max", e.getMetaMessage().toString());
		assertEquals(value, i.next().getValue());
		assertTrue(((Comparable) maxLength).compareTo(i.next()) == 0);
	}

	protected void performNumericUnderMinTest(ValidationExampleBO ve, String fieldIndicator, Comparable value,
			Object minLength) {
		// sanity check:
		verifyInvalidMinData(value, minLength);

		ve.validate();

		// ensure the correct number of errors:
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages,
				getExpectedMessagePropertyKey(fieldIndicator));
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrors().iterator().next();
		assertEquals("value.must.be.gt.min", e.getMetaMessage().toString());
		Iterator<Entry<String, String>> i = e.getAllInserts().iterator();		
		assertEquals(value, i.next().getValue());
		assertTrue(((Comparable) minLength).compareTo(i.next()) == 0);
	}

	protected void performNumericAtOrUnderMaxTest(ValidationExampleBO ve, Comparable value, Object minLength) {
		// sanity check:
		verifyValidMaxData(value, minLength);

		ve.validate();
		Messages messages = verifyMessages();
		assertFalse("No errrors should have been returned!", messages.hasErrors());
	}

	protected void performNumericAtOrOverMinTest(ValidationExampleBO ve, Comparable value, Object maxLength) {
		// sanity check:
		verifyValidMinData(value, maxLength);

		ve.validate();
		Messages messages = verifyMessages();
		assertFalse("No errrors should have been returned!", messages.hasErrors());
	}

	protected void verifyInvalidMaxData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", (c.compareTo(limit) == 1));
	}

	protected void verifyInvalidMinData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", (c.compareTo(limit) == -1));
	}

	protected void verifyValidMaxData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", (c.compareTo(limit) <= 0));
	}

	protected void verifyValidMinData(Comparable c, Object limit) {
		assertTrue("This test no longer has valid data parameters", (c.compareTo(limit) >= 0));
	}

	protected String getExpectedMessagePropertyKey(String fieldIndicator) {
		return "ValidationExample." + fieldIndicator;
	}
}

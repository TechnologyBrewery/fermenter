package org.bitbucket.askllc.fermenter.cookbook.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Temporarily ignoring until more granular validation message support is added")
public class TestStringValidation extends AbstractValidationTest {

	private final int STRING_MAX_LENGTH = 20;
	private final int STRING_MIN_LENGTH = 2;

	@Test
	public void testLengthOverMax() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("abcdefghijklmnopqrstuvwxyz");

		// sanity check:
		verifyInvalidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);

		ve.validate();

		// ensure the correct number of errors:
		String fieldIndicator = "stringExample";
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrorMessages(fieldIndicator).iterator().next();
		assertEquals("value.must.be.shorter.than", e.getKey());
		Iterator<Object> i = e.getInserts().iterator();
		assertEquals(fieldIndicator, i.next());
		assertEquals(ve.getStringExample(), i.next());
		assertEquals(new Integer(STRING_MAX_LENGTH), i.next());
	}

	@Test
	public void testLengthAtMax() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("01234567890123456789");

		// sanity check:
		verifyValidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);

		ve.validate();

		Messages messages = verifyMessages();
		// ensure that no errors were returned:
		assertFalse(messages.hasErrorMessages());
	}

	@Test
	public void testLengthUnderMax() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("0123456789");

		// sanity check:
		verifyValidMaxData(ve.getStringExample().length(), STRING_MAX_LENGTH);

		ve.validate();

		Messages messages = verifyMessages();
		// ensure that no errors were returned:
		assertFalse(messages.hasErrorMessages());

	}

	@Test
	public void testLengthUnderMin() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("a");

		// sanity check:
		verifyInvalidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);

		ve.validate();

		// ensure the correct number of errors:
		String fieldIndicator = "stringExample";
		Messages messages = verifyMessages();
		Collection<Message> fieldLevelList = verifyFieldLevelList(messages, fieldIndicator);
		verifyNumberOfErrors(fieldLevelList, 1);

		// ensure that error is well formed
		Message e = messages.getErrorMessages(fieldIndicator).iterator().next();
		assertEquals("value.must.be.longer.than", e.getKey());
		Iterator<Object> i = e.getInserts().iterator();
		assertEquals(fieldIndicator, i.next());
		assertEquals(ve.getStringExample(), i.next());
		assertEquals(new Integer(STRING_MIN_LENGTH), i.next());
	}

	@Test
	public void testLengthAtMin() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("01");

		// sanity check:
		verifyValidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);

		ve.validate();

		Messages messages = verifyMessages();
		// ensure that no errors were returned:
		assertFalse(messages.hasErrorMessages());
	}

	@Test
	public void testLengthOverMin() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample("0123456789");

		// sanity check:
		verifyValidMinData(ve.getStringExample().length(), STRING_MIN_LENGTH);

		ve.validate();

		Messages messages = verifyMessages();
		// ensure that no errors were returned:
		assertFalse(messages.hasErrorMessages());
	}

	@Test
	public void testTrimLeadingWhiteSpace() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");

		String baseString = "abcd";
		String whitespace = "    ";
		ve.setStringExample(whitespace + baseString);

		assertEquals("Trimming has not returned the correct value", ve.getStringExample(), baseString);

	}

	@Test
	public void testTrimTrailingWhiteSpace() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");

		String baseString = "abcd";
		String whitespace = "    ";
		ve.setStringExample(baseString + whitespace);

		assertEquals("Trimming has not returned the correct value", ve.getStringExample(), baseString);

	}

	@Test
	public void testTrimAllWhiteSpace() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");

		String whitespace = "    ";
		ve.setStringExample(whitespace);

		assertEquals("Trimming has not returned the correct value", ve.getStringExample(), null);

	}

	@Test
	public void testTrimAllOnNull() {
		ValidationExampleBO ve = getValidationExampleBO();
		ve.setRequiredField("requiredValue");
		ve.setStringExample(null);

		assertEquals("Trimming has not returned the correct value", ve.getStringExample(), null);

	}

	// ////////////////////////////////////////////////////////////////////
	// Helper methods:
	// ////////////////////////////////////////////////////////////////////

	private void verifyInvalidMaxData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", (i > limit));
	}

	private void verifyValidMaxData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", (i <= limit));
	}

	private void verifyInvalidMinData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", (i < limit));
	}

	private void verifyValidMinData(int i, int limit) {
		assertTrue("This test no longer has valid data parameters", (i >= limit));
	}
}

package com.ask.test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.service.ServiceResponse;

import com.ask.test.domain.enumeration.SimpleDomainEnumeration;
import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.TransferObjectFactory;
import com.ask.test.domain.transfer.ValidationExample;
import com.ask.test.domain.transfer.ValidationExampleChild;

/**
 * Contains common integration test logic for this project.
 */
public final class TestUtils {

    public static final String DOMAIN_GROUPID_ARTIFACTID_VERSION = "com.ask.fermenter:fermenter-test-domain:1-SNAPSHOT";

    private TestUtils() {
	// prevent instantiation
    }

    public static SimpleDomain createRandomSimpleDomain() {
	SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
	domain.setName(RandomStringUtils.randomAlphanumeric(25));
	domain.setTheDate1(new Date());
	domain.setTheLong1(RandomUtils.nextLong());
	domain.setType(RandomStringUtils.random(5));
	domain.setAnEnumeratedValue(SimpleDomainEnumeration.values()[RandomUtils.nextInt(SimpleDomainEnumeration
		.values().length)]);
	return domain;
    }

    public static ValidationExample createRandomValidationExample() {
	ValidationExample domain = TransferObjectFactory.createValidationExample();
	domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
	return domain;
    }

    public static ValidationExampleChild createRandomValidationExampleChild() {
	ValidationExampleChild domain = TransferObjectFactory.createValidationExampleChild();
	domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
	return domain;
    }

    public static void assertNoErrorMessages(ServiceResponse response) {
	if (response != null) {
	    Messages messages = response.getMessages();
	    assertFalse(messages.hasErrorMessages());
	}
    }

    public static void assertErrorMessagesInResponse(ServiceResponse response, int expectedNumErrorMessages) {
	assertNotNull("Service response wrapper was unexpectedly null", response);
	Messages messages = response.getMessages();
	assertNotNull("Messages object on service response wrapper was unexpected null", messages);
	assertEquals("An unexpected number of error messages were found", expectedNumErrorMessages,
		messages.getErrorMessageCount());
    }
}

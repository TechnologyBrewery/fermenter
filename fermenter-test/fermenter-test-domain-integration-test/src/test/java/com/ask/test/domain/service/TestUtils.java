package com.ask.test.domain.service;

import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.service.ValueServiceResponse;
import org.tigris.atlas.transfer.TransferObject;

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
		//prevent instantiation
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

	public static void assertNoErrorMessages(ValueServiceResponse<? extends TransferObject> response) {
		if (response != null) {
			Messages messages = response.getMessages();
			assertNoErrorMessages(messages.getErrorMessages());
			
			TransferObject to = response.getValue();
			if (to != null) {
				Messages toMessages = to.getMessages();
				assertNoErrorMessages(toMessages.getErrorMessages());
			}
			
		}
		
	}
	
	public static void assertNoErrorMessages(Collection<Message> messages) {
		if (messages != null) {
			assertFalse(messages.size() > 0);
				
		}
		
	}	

}

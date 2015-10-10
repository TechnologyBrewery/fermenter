package org.bitbucket.fermenter.stout.test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.service.ServiceResponse;
import org.bitbucket.fermenter.stout.test.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.stout.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.stout.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExampleChild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains common integration test logic for this project.
 */
public final class TestUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

	private TestUtils() {
		// prevent instantiation
	}

	public static SimpleDomain createRandomSimpleDomain() {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		domain.setName(RandomStringUtils.randomAlphanumeric(25));
		domain.setTheDate1(new Date());
		domain.setTheLong1(RandomUtils.nextLong(0L, 10000L));
		domain.setType(RandomStringUtils.random(5));
		domain.setAnEnumeratedValue(SimpleDomainEnumeration.values()[RandomUtils.nextInt(0,
				SimpleDomainEnumeration.values().length)]);
		return domain;
	}

	public static SimpleDomain createRandomSimpleDomain(double bigDecimalAttributeValue) {
		SimpleDomain simpleDomain = createRandomSimpleDomain();
		simpleDomain.setBigDecimalValue(BigDecimal.valueOf(bigDecimalAttributeValue));
		return simpleDomain;
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
			boolean hasErrorMessages = messages.hasErrorMessages();
			if (hasErrorMessages) {
				for (Message message : messages.getErrorMessages()) {
					LOGGER.error(MessageUtils.getSummaryMessage(message.getKey(), message.getInserts(),
							SimpleDomain.class));
				}
			}
			assertFalse(hasErrorMessages);
		}
	}

	public static void assertErrorMessagesInResponse(ServiceResponse response, int expectedNumErrorMessages) {
		assertNotNull("Service response wrapper was unexpectedly null", response);
		Messages messages = response.getMessages();
		assertNotNull("Messages object on service response wrapper was unexpected null", messages);
		assertEquals("An unexpected number of error messages were found", expectedNumErrorMessages,
				messages.getErrorMessageCount());
	}

	/**
	 * Rounds the given {@link BigDecimal} using the same scale utilized by the default DECIMAL/NUMERIC SQL type for
	 * HSQLDB, which is numeric(19,2).
	 * 
	 * @param bigDecimal
	 *            decimal value to round.
	 * @return {@link BigDecimal} rounded using a scale of 2.
	 */
	public static BigDecimal roundToHSQLDBDefaultDecimalType(BigDecimal bigDecimal) {
		return bigDecimal.setScale(2, RoundingMode.HALF_UP);
	}
}

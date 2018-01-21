package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomain;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomainChild;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomainEagerChild;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.messages.Messages;
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
		SimpleDomain domain = new SimpleDomain();
		domain.setName(RandomStringUtils.randomAlphanumeric(25));
		domain.setTheDate1(new Date());
		domain.setTheLong1(RandomUtils.nextLong(0L, 10000L));
		domain.setType(RandomStringUtils.random(5));
		domain.setAnEnumeratedValue(
				SimpleDomainEnumeration.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration.values().length)]);
		domain.setStandardBoolean(false);
		domain.setNumericBoolean(true);
		return domain;
	}

	public static SimpleDomain createRandomSimpleDomain(double bigDecimalAttributeValue) {
		SimpleDomain simpleDomain = createRandomSimpleDomain();
		simpleDomain.setBigDecimalValue(BigDecimal.valueOf(bigDecimalAttributeValue));
		return simpleDomain;
	}

	public static SimpleDomain createRandomSimpleDomain(int numChildEntities) {
		SimpleDomain domain = createRandomSimpleDomain();
		for (int iter = 0; iter < numChildEntities; iter++) {
			SimpleDomainChild lazyChild = new SimpleDomainChild();
			lazyChild.setName(RandomStringUtils.randomAlphabetic(10));
			domain.addSimpleDomainChild(lazyChild);
			
			SimpleDomainEagerChild eagerChild = new SimpleDomainEagerChild();
			lazyChild.setName(RandomStringUtils.randomAlphabetic(10));
			domain.addSimpleDomainEagerChild(eagerChild);
		}
		return domain;
	}

	public static void assertNoErrorMessages() {
		Messages messages = MessageManager.getMessages();
		boolean hasErrorMessages = messages.hasErrorMessages();
		if (hasErrorMessages) {
			for (Message message : messages.getErrorMessages()) {
				LOGGER.error(MessageUtils.getSummaryMessage(message.getKey(), message.getInserts(),
						org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomain.class));
			}
		}
		assertFalse(hasErrorMessages);
	}

}

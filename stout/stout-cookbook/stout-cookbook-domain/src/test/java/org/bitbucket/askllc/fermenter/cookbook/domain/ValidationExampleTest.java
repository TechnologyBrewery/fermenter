package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:application-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
@WebAppConfiguration
/**
 * Exercises generated code involving the {@link ValidationExampleBO} that is geared towards business object validation.
 * Similar to {@link SimpleDomainManagerTest}, this test leverages spring-test in conjunction with an embedded database.
 */
public class ValidationExampleTest extends AbstractMsgMgrAwareTestSupport {

	@Test
	public void testSaveValidBigDecimalAttrScale() throws Exception {
		ValidationExampleBO bizObj = TestUtils.createRandomValidationExample();
		BigDecimal randomBigDecimal = new BigDecimal(RandomUtils.nextDouble(0.0d, 1000.0d));

		// NB developers should consider putting data normalization logic such as the following rounding code into one
		// of the save() lifecycle hooks exposed on business objects, such as preValidate()
		bizObj.setBigDecimalExampleWithScale(randomBigDecimal.setScale(3, RoundingMode.HALF_EVEN));
		bizObj.setBigDecimalExampleWithLargeScale(randomBigDecimal.setScale(10, RoundingMode.HALF_EVEN));
		bizObj = bizObj.save();

		assertEquals(0, MessageManager.getMessages().getErrorMessageCount());
		ValidationExampleBO retrievedBizObj = ValidationExampleBO.findByPrimaryKey(bizObj.getKey());
		assertEquals(3, retrievedBizObj.getBigDecimalExampleWithScale().scale());
		assertEquals(10, retrievedBizObj.getBigDecimalExampleWithLargeScale().scale());
	}

	@Test
	public void testSaveInvalidBigDecimalAttrScale() throws Exception {
		ValidationExampleBO bizObj = TestUtils.createRandomValidationExample();
		bizObj.setBigDecimalExampleWithScale(new BigDecimal(RandomUtils.nextDouble(0.0d, 1000.0d)));
		bizObj = bizObj.save();

		assertEquals(1, MessageManager.getMessages().getErrorMessageCount());
		assertNull(ValidationExampleBO.findByPrimaryKey(bizObj.getKey()));
	}
}

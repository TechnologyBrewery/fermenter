package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.bitbucket.fermenter.stout.messages.CoreMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
		"classpath:h2-spring-ds-context.xml" })
@Transactional
@WebAppConfiguration
/**
 * Exercises generated code involving the {@link ValidationExampleBO} that is
 * geared towards business object validation. Similar to
 * {@link SimpleDomainManagerTest}, this test leverages spring-test in
 * conjunction with an embedded database.
 */
public class ValidationExampleTest extends AbstractMsgMgrAwareTestSupport {

	@After
	public void deleteValidationTestExamples() {
		ValidationExampleBO.deleteAllValidationExamples();
	}

	@Transactional
	@Test
	public void testSaveValidBigDecimalAttrScale() throws Exception {
		ValidationExampleBO bizObj = TestUtils.createRandomValidationExample();
		BigDecimal randomBigDecimal = new BigDecimal(RandomUtils.nextDouble(0.0d, 1000.0d));
      
		// NB developers should consider putting data normalization logic such as the
		// following rounding code into one
		// of the save() lifecycle hooks exposed on business objects, such as
		// preValidate()
		bizObj.setBigDecimalExampleWithScale(randomBigDecimal);
		bizObj.setBigDecimalExampleWithLargeScale(randomBigDecimal);
		bizObj = bizObj.save();	

		assertEquals(0, MessageManager.getMessages().getErrorMessageCount());
		ValidationExampleBO retrievedBizObj = ValidationExampleBO.findByPrimaryKey(bizObj.getKey());
		
		int bizObjScale1 = bizObj.getBigDecimalExampleWithScale().scale();
		int bizObjScale2 = bizObj.getBigDecimalExampleWithLargeScale().scale();
        
		assertEquals(bizObjScale1, retrievedBizObj.getBigDecimalExampleWithScale().scale());
		assertEquals(bizObjScale2, retrievedBizObj.getBigDecimalExampleWithLargeScale().scale());
	}

	@Transactional
	@Test
	public void testSaveInvalidBigDecimalAttrScale() throws Exception {
		ValidationExampleBO bizObj = TestUtils.createRandomValidationExample();
		bizObj.setBigDecimalExample(new BigDecimal(RandomUtils.nextDouble(0.0d, 1000.0d)));
		bizObj = bizObj.save();
		
		assertEquals(0, MessageManager.getMessages().getErrorMessageCount());
	
		ValidationExampleBO retrievedBizObj = ValidationExampleBO.findByPrimaryKey(bizObj.getKey());
		
		//this field does not have scale defined but it is 5 by default
		int bizObjScale = bizObj.getBigDecimalExample().scale();
		//the db definition has scale of 2
		int dbScale = retrievedBizObj.getBigDecimalExample().scale();
		assertTrue(bizObjScale != dbScale);
	}

	@Transactional
	@Test
	public void testSaveMultipleInvalidFields() throws Exception {
		ValidationExampleBO bizObj = TestUtils.createRandomValidationExample();
		bizObj.setRequiredField(null);
		bizObj.setStringExample(RandomStringUtils.randomAlphabetic(21));
		bizObj.setIntegerExample(RandomUtils.nextInt(20000, 30000));
		bizObj = bizObj.save();

		assertEquals(3, MessageManager.getMessages().getErrorMessageCount());
		for (Message message : MessageManager.getMessages().getErrorMessages()) {
			assertEquals(CoreMessages.INVALID_FIELD, message.getKey());

			String invalidPropertyName = message.getProperties().iterator().next();
			assertTrue("requiredField".equals(invalidPropertyName) || "stringExample".equals(invalidPropertyName)
					|| "integerExample".equals(invalidPropertyName));
		}
		assertNull(bizObj.getKey());
	}
}

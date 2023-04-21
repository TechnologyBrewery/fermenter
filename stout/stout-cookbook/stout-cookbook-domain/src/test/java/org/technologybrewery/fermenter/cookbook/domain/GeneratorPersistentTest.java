package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.IntegerKeyedEntityBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.LongKeyedEntityBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.UuidKeyedEntityBO;
import org.technologybrewery.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
@WebAppConfiguration
public class GeneratorPersistentTest extends AbstractMsgMgrAwareTestSupport {

	@After
	public void deleteAllGeneratorPersistentTestExamples() {
		UuidKeyedEntityBO.deleteAllUuidKeyedEntity();
		IntegerKeyedEntityBO.deleteAllIntegerKeyedEntity();
		LongKeyedEntityBO.deleteAllLongKeyedEntity();
	}
	
	@Test
	public void testSaveUuidGeneratedPriamryKey() throws Exception {
		UuidKeyedEntityBO bizObj = new UuidKeyedEntityBO();

		bizObj = bizObj.save();

		assertEquals(0, MessageManager.getMessages().getErrorCount());
		assertNotNull("PK should have been set to a valid UUID!", bizObj.getKey());
		UuidKeyedEntityBO retrievedBizObj = UuidKeyedEntityBO.findByPrimaryKey(bizObj.getKey());
		assertNotNull("Could not find a instance with PK: " + bizObj.getKey(), retrievedBizObj);
	}
	
    @Test
    public void testSaveIntegerGeneratedPriamryKey() throws Exception {
        IntegerKeyedEntityBO bizObj = new IntegerKeyedEntityBO();

        bizObj = bizObj.save();

        assertEquals(0, MessageManager.getMessages().getErrorCount());
        assertNotNull("PK should have been set to a valid integer!", bizObj.getKey());
        IntegerKeyedEntityBO retrievedBizObj = IntegerKeyedEntityBO.findByPrimaryKey(bizObj.getKey());
        assertNotNull("Could not find a instance with PK: " + bizObj.getKey(), retrievedBizObj);
    }
    
    @Test
    public void testSaveLongGeneratedPriamryKey() throws Exception {
        LongKeyedEntityBO bizObj = new LongKeyedEntityBO();

        bizObj = bizObj.save();

        assertEquals(0, MessageManager.getMessages().getErrorCount());
        assertNotNull("PK should have been set to a valid integer!", bizObj.getKey());
        LongKeyedEntityBO retrievedBizObj = LongKeyedEntityBO.findByPrimaryKey(bizObj.getKey());
        assertNotNull("Could not find a instance with PK: " + bizObj.getKey(), retrievedBizObj);
    }

}

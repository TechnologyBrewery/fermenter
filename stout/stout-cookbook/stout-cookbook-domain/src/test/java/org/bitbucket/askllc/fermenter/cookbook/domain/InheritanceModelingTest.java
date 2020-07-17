package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.IntegerKeyedEntityBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSubclassABO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSubclassBBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassParentBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.MappedSubclassAMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.MappedSubclassBMaintenanceService;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.junit.After;
import org.junit.Before;
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
/**
 * Exercises generated business object code that demonstrates how inheritance hierarchies may be modeled.
 */
public class InheritanceModelingTest extends AbstractMsgMgrAwareTestSupport {

    @Inject
    private MappedSubclassAMaintenanceService childAService;

    @Inject
    private MappedSubclassBMaintenanceService childBService;
    
    @Before
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
    }
    @After
    public void deleteAllInheritanceModelingTestExamples() {
    	MappedSubclassABO.deleteAllMappedSubclassA();
    	MappedSubclassBBO.deleteAllMappedSubclassB();
    	
    	AuthenticationTestUtils.logout();
    }

    @Test
    public void testSaveMappedSubclassren() throws Exception {
        String commonStringAttr = RandomStringUtils.randomAlphabetic(10);

        MappedSubclassABO childABO = createRandomMappedSubclassA(commonStringAttr);
        ValueServiceResponse<MappedSubclassABO> saveChildAResponse = childAService.saveOrUpdate(childABO);
        TestUtils.assertNoErrorMessages(saveChildAResponse);

        MappedSubclassBBO childBBO = createRandomMappedSubclassB(commonStringAttr);
        ValueServiceResponse<MappedSubclassBBO> saveChildBResponse = childBService.saveOrUpdate(childBBO);
        TestUtils.assertNoErrorMessages(saveChildBResponse);

        ValueServiceResponse<MappedSubclassABO> findChildAResponse = childAService
                .findByPrimaryKey(saveChildAResponse.getValue().getKey());
        TestUtils.assertNoErrorMessages(findChildAResponse);
        assertEquals(commonStringAttr, findChildAResponse.getValue().getCommonStringValue());
        assertNotNull(findChildAResponse.getValue().getRefToSimpleDomain().getName());

        ValueServiceResponse<MappedSubclassBBO> findChildBResponse = childBService
                .findByPrimaryKey(saveChildBResponse.getValue().getKey());
        TestUtils.assertNoErrorMessages(findChildBResponse);
        assertEquals(commonStringAttr, findChildBResponse.getValue().getCommonStringValue());
        assertNotNull(findChildAResponse.getValue().getRefToSimpleDomain().getTheDate1());
    }

    @Test
    public void testMappedSuperclassSimulatedPolymorphicQuery() throws Exception {
        int numChildAEntities = RandomUtils.nextInt(10) + 2;
        for (int iter = 0; iter < numChildAEntities; iter++) {
            MappedSubclassABO childABO = createRandomMappedSubclassA(null);
            ValueServiceResponse<MappedSubclassABO> saveChildAResponse = childAService.saveOrUpdate(childABO);
            TestUtils.assertNoErrorMessages(saveChildAResponse);
        }

        int numChildBEntities = RandomUtils.nextInt(10) + 2;
        for (int iter = 0; iter < numChildBEntities; iter++) {
            MappedSubclassBBO childBBO = createRandomMappedSubclassB(null);
            ValueServiceResponse<MappedSubclassBBO> saveChildBResponse = childBService.saveOrUpdate(childBBO);
            TestUtils.assertNoErrorMessages(saveChildBResponse);
        }

        List<MappedSuperclassParentBO> allChildEntities = MappedSuperclassParentBO.findAll();
        assertNotNull(allChildEntities);
        assertEquals(numChildAEntities + numChildBEntities, allChildEntities.size());
    }

    protected MappedSubclassABO createRandomMappedSubclassA(String commonStringAttr) {
        MappedSubclassABO bizObj = new MappedSubclassABO();
        bizObj.setChildAType(RandomStringUtils.randomAlphabetic(10));
        bizObj.setCommonStringValue(
                commonStringAttr != null ? commonStringAttr : RandomStringUtils.randomAlphabetic(15));
        SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
        simpleDomain.save();
        bizObj.setRefToSimpleDomain(simpleDomain);

        IntegerKeyedEntityBO intKeyBizObj = new IntegerKeyedEntityBO();
        intKeyBizObj.save();
        bizObj.setRefToIntKeyedEntity(intKeyBizObj);

        return bizObj;
    }

    protected MappedSubclassBBO createRandomMappedSubclassB(String commonStringAttr) {
        MappedSubclassBBO bizObj = new MappedSubclassBBO();
        bizObj.setChildBType(RandomStringUtils.randomAlphabetic(5));
        bizObj.setCommonStringValue(
                commonStringAttr != null ? commonStringAttr : RandomStringUtils.randomAlphabetic(15));
        SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
        simpleDomain.save();
        bizObj.setRefToSimpleDomain(simpleDomain);
        return bizObj;
    }

}

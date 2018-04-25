package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassChildABO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassChildBBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassParentBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.MappedSuperclassChildAMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.MappedSuperclassChildBMaintenanceService;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:base-application-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
@WebAppConfiguration
/**
 * Exercises generated business object code that demonstrates how inheritance hierarchies may be modeled.
 */
public class InheritanceModelingTest extends AbstractMsgMgrAwareTestSupport {

    @Inject
    private MappedSuperclassChildAMaintenanceService childAService;

    @Inject
    private MappedSuperclassChildBMaintenanceService childBService;

    @Test
    public void testSaveMappedSuperclassChildren() throws Exception {
        String commonStringAttr = RandomStringUtils.randomAlphabetic(10);

        MappedSuperclassChildABO childABO = createRandomMappedSuperclassChildA(commonStringAttr);
        ValueServiceResponse<MappedSuperclassChildABO> saveChildAResponse = childAService.saveOrUpdate(childABO);
        TestUtils.assertNoErrorMessages(saveChildAResponse);

        MappedSuperclassChildBBO childBBO = createRandomMappedSuperclassChildB(commonStringAttr);
        ValueServiceResponse<MappedSuperclassChildBBO> saveChildBResponse = childBService.saveOrUpdate(childBBO);
        TestUtils.assertNoErrorMessages(saveChildBResponse);

        ValueServiceResponse<MappedSuperclassChildABO> findChildAResponse = childAService
                .findByPrimaryKey(saveChildAResponse.getValue().getKey());
        TestUtils.assertNoErrorMessages(findChildAResponse);
        assertEquals(commonStringAttr, findChildAResponse.getValue().getCommonStringValue());

        ValueServiceResponse<MappedSuperclassChildBBO> findChildBResponse = childBService
                .findByPrimaryKey(saveChildBResponse.getValue().getKey());
        TestUtils.assertNoErrorMessages(findChildBResponse);
        assertEquals(commonStringAttr, findChildBResponse.getValue().getCommonStringValue());
    }

    @Test
    public void testMappedSuperclassSimulatedPolymorphicQuery() throws Exception {
        int numChildAEntities = RandomUtils.nextInt(10) + 2;
        for (int iter = 0; iter < numChildAEntities; iter++) {
            MappedSuperclassChildABO childABO = createRandomMappedSuperclassChildA(null);
            ValueServiceResponse<MappedSuperclassChildABO> saveChildAResponse = childAService.saveOrUpdate(childABO);
            TestUtils.assertNoErrorMessages(saveChildAResponse);
        }

        int numChildBEntities = RandomUtils.nextInt(10) + 2;
        for (int iter = 0; iter < numChildBEntities; iter++) {
            MappedSuperclassChildBBO childBBO = createRandomMappedSuperclassChildB(null);
            ValueServiceResponse<MappedSuperclassChildBBO> saveChildBResponse = childBService.saveOrUpdate(childBBO);
            TestUtils.assertNoErrorMessages(saveChildBResponse);
        }

        List<MappedSuperclassParentBO> allChildEntities = MappedSuperclassParentBO.findAll();
        assertNotNull(allChildEntities);
        assertEquals(numChildAEntities + numChildBEntities, allChildEntities.size());
    }

    protected MappedSuperclassChildABO createRandomMappedSuperclassChildA(String commonStringAttr) {
        MappedSuperclassChildABO bizObj = new MappedSuperclassChildABO();
        bizObj.setChildAType(RandomStringUtils.randomAlphabetic(10));
        bizObj.setCommonStringValue(
                commonStringAttr != null ? commonStringAttr : RandomStringUtils.randomAlphabetic(15));
        return bizObj;
    }

    protected MappedSuperclassChildBBO createRandomMappedSuperclassChildB(String commonStringAttr) {
        MappedSuperclassChildBBO bizObj = new MappedSuperclassChildBBO();
        bizObj.setChildBType(RandomStringUtils.randomAlphabetic(5));
        bizObj.setCommonStringValue(
                commonStringAttr != null ? commonStringAttr : RandomStringUtils.randomAlphabetic(15));
        return bizObj;
    }

}

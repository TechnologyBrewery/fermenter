package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:base-application-context.xml", "classpath:h2-spring-ds-context.xml" })
// enables each test method to transparently participate in any existing transactional context loaded via
// @ContextConfiguration application contexts and automatically rollbacks any transactions after each test method
@Transactional
// needed to allow Spring to instantiate request-scoped {@link Messages} that will be injected by generated code at the
// beginning of each business service operation into the {@link MessageManager}.
@WebAppConfiguration
/**
 * Tests generated code and developer integration points for building functionality not natively supported by Fermenter.
 * Additionally, this test classes demonstrates how spring-test may be leveraged to perform unit testing against an
 * embedded database.
 */
public class SimpleDomainManagerTest extends AbstractMsgMgrAwareTestSupport {

	@Inject
	private SimpleDomainManagerService simpleDomainMgr;

	@Test
	public void testCountPassedCollection() throws Exception {
		Integer numBizObjs = RandomUtils.nextInt(5, 10);
		List<SimpleDomainBO> input = TestUtils.createAndPersistRandomSimpleDomains(numBizObjs);

		ValueServiceResponse<Integer> response = simpleDomainMgr.countNumInputs(input);

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(numBizObjs, response.getValue());
	}

	@Test
	public void testEchoPlusWazzup() throws Exception {
		String root = RandomStringUtils.randomAlphanumeric(5);
		ValueServiceResponse<String> response = simpleDomainMgr.echoPlusWazzup(root);

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(root + "Wazzup", response.getValue());
	}

	@Test
	public void testGetSimpleDomainCount() throws Exception {
		Integer numBizObjs = RandomUtils.nextInt(5, 10);
		TestUtils.createAndPersistRandomSimpleDomains(numBizObjs);
		ValueServiceResponse<Integer> response = simpleDomainMgr.getSimpleDomainCount();

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(numBizObjs, response.getValue());
	}

	@Test
	public void testSelectAllSimpleDomains() throws Exception {
		int numBizObjs = RandomUtils.nextInt(5, 10);
		TestUtils.createAndPersistRandomSimpleDomains(numBizObjs);
		ValueServiceResponse<Collection<SimpleDomainBO>> response = simpleDomainMgr.selectAllSimpleDomains();

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		Collection<SimpleDomainBO> retrievedBizObjs = response.getValue();
		assertEquals(numBizObjs, retrievedBizObjs.size());
	}

	@Test
	public void testSelectAllSimpleDomainsWithPaging() throws Exception {
		int pageSize = 5;
		int numTotalBizObjs = RandomUtils.nextInt(6, 10);
		TestUtils.createAndPersistRandomSimpleDomains(numTotalBizObjs);
		ValueServiceResponse<Collection<SimpleDomainBO>> response = simpleDomainMgr.selectAllSimpleDomainsWithPaging(0,
				pageSize);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(pageSize, response.getValue().size());

		response = simpleDomainMgr.selectAllSimpleDomainsWithPaging(1, pageSize);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(numTotalBizObjs - pageSize, response.getValue().size());
	}

	@Test
	public void testDeleteAllSimpleDomains() throws Exception {
		int numBizObjs = RandomUtils.nextInt(5, 10);
		TestUtils.createAndPersistRandomSimpleDomains(numBizObjs);
		VoidServiceResponse response = simpleDomainMgr.deleteAllSimpleDomains();

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		assertEquals(0, SimpleDomainBO.findAll().size());
	}

	@Test
	public void testSelectAllSimpleDomainsByType() throws Exception {
		String type = RandomStringUtils.randomAlphanumeric(5);
		int numNonMatchingBizObjs = RandomUtils.nextInt(5, 10);
		TestUtils.createAndPersistRandomSimpleDomains(numNonMatchingBizObjs);

		SimpleDomainBO matchingBizObj = TestUtils.createRandomSimpleDomain();
		matchingBizObj.setType(type);
		matchingBizObj.save();

		ValueServiceResponse<Collection<SimpleDomainBO>> response = simpleDomainMgr.selectAllSimpleDomainsByType(type);

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		Collection<SimpleDomainBO> matchingBizObjs = response.getValue();
		assertEquals(1, matchingBizObjs.size());
		assertEquals(type, matchingBizObjs.iterator().next().getType());
	}

	@Test
	public void testBusinessServiceMethodPropagateErrorMessages() throws Exception {
		int numErrorMessagesToGenerate = RandomUtils.nextInt(1, 5);
		VoidServiceResponse response = simpleDomainMgr.createAndPropagateErrorMessages(numErrorMessagesToGenerate);
		TestUtils.assertErrorMessagesInResponse(response, numErrorMessagesToGenerate);
	}

	@Test
	public void testReturnNullEntityInBusinessServiceOperation() throws Exception {
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainMgr.returnNullEntity();
		TestUtils.assertNoErrorMessages(response);
		assertNull(response.getValue());
	}

}

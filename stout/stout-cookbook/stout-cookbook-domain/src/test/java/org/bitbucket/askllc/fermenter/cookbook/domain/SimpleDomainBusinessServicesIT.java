package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleDomainBusinessServicesIT extends RunTestsWithinArquillianWar {

    @ArquillianResource
    private URL deploymentURL;

	@Before
	public void deleteSimpleDomains() throws Exception {
		ResteasyClient client = new ResteasyClientBuilderImpl().build();
        client.target(deploymentURL.toURI()).path("rest").path("SimpleDomainManagerService")
                .path("deleteAllSimpleDomains").request().header("username", "testUser").post(null).close();
	}

	@Test
	@RunAsClient
	public void testEcho(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<String> response = managerService.echoPlusWazzup(RandomStringUtils.randomAlphabetic(10));

		TestUtils.assertNoErrorMessages(response);
		assertNotNull(response.getValue());
	}

	@Test
	@RunAsClient
	public void testPassingAnEntityAndPrimitive(@ArquillianResteasyResource ResteasyWebTarget webTarget)
			throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

		ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService.someBusinessOperation(domain,
				someImportantInfo);

		assertNotNull(responseDomainWrapper);
		TestUtils.assertNoErrorMessages(responseDomainWrapper);
		SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.endsWith(someImportantInfo));

	}

	@Test
	@RunAsClient
	public void testPassingListOfEntities(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		int numberOfItems = RandomUtils.nextInt(0, 10);
		List<SimpleDomainBO> list = new ArrayList<>();
		for (int i = 0; i < numberOfItems; i++) {
			SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
			domain.setName("item " + i);
			list.add(domain);
		}

		ValueServiceResponse<Integer> response = managerService.countNumInputs(list);

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		Integer count = response.getValue();
		assertNotNull(count);
		assertTrue(count == numberOfItems);

	}

	@Test
	@RunAsClient
	public void testReturnVoid(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		VoidServiceResponse response = managerService.returnVoid();

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
	}

	@Test
	@RunAsClient
	public void testPassingPrimitivesAsParameters(@ArquillianResteasyResource ResteasyWebTarget webTarget)
			throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		VoidServiceResponse response = managerService
				.doSomethingWithPrimitiveInputs(RandomStringUtils.randomAlphabetic(5), RandomUtils.nextInt(0, 10));
		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
	}

	@Test
	@RunAsClient
	public void testReturningACharacter(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<String> response = managerService.doSomethingAndReturnAPrimitive();

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		assertNotNull(response.getValue());

	}

	@Test
	@RunAsClient
	public void testReturnNullEntityInBusinessServiceOperation(@ArquillianResteasyResource ResteasyWebTarget webTarget)
			throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<SimpleDomainBO> response = managerService.returnNullEntity();
		TestUtils.assertNoErrorMessages(response);
		assertEquals("Unexpectedly received a non-null SimpleDomain entity", response.getValue(), null);
	}

	@Test
	@RunAsClient
	public void testSomeBusinessOperationWithEntityParam(@ArquillianResteasyResource ResteasyWebTarget webTarget) {
		SimpleDomainManagerService managerService = getService(webTarget);
		SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);
		domain.setName(someImportantInfo);

		ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService
				.methodWithSingleEntityAsParam(domain);
		assertNotNull(responseDomainWrapper);
		TestUtils.assertNoErrorMessages(responseDomainWrapper);
		SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.startsWith(someImportantInfo));
		assertTrue(name.endsWith("updated"));

	}

	@Test
	@RunAsClient
	public void testSelectAllSimpleDomains(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService maintenanceService = getMaintenanceService(webTarget);
		int numSimpleDomains = RandomUtils.nextInt(5, 10);
		int numSimpleDomainChildren = RandomUtils.nextInt(2, 6);

		for (int iter = 0; iter < numSimpleDomains; iter++) {
			maintenanceService.saveOrUpdate(TestUtils.createRandomSimpleDomain(numSimpleDomainChildren));
		}

		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<Collection<SimpleDomainBO>> allSimpleDomainsResponse = managerService
				.selectAllSimpleDomains();
		TestUtils.assertNoErrorMessages(allSimpleDomainsResponse);

		Collection<SimpleDomainBO> allSimpleDomains = allSimpleDomainsResponse.getValue();
		assertEquals(numSimpleDomains, allSimpleDomains.size());

		assertEquals(numSimpleDomainChildren, allSimpleDomains.iterator().next().getSimpleDomainChilds().size());		
	}
	
	@Test
	@RunAsClient
	public void testSelectAllSimpleDomainsEagerLazyLoadChild(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService maintenanceService = getMaintenanceService(webTarget);
		int numSimpleDomains = RandomUtils.nextInt(5, 10);
		int numSimpleDomainChildren = RandomUtils.nextInt(2, 6);

		for (int iter = 0; iter < numSimpleDomains; iter++) {
			maintenanceService.saveOrUpdate(TestUtils.createRandomSimpleDomain(numSimpleDomainChildren));
		}

        SimpleDomainManagerService managerService = getService(webTarget);
        ValueServiceResponse<Collection<SimpleDomainBO>> allSimpleDomainsResponse = managerService
                .selectAllSimpleDomainsLazySimpleDomainChild();

		TestUtils.assertNoErrorMessages(allSimpleDomainsResponse);

		Collection<SimpleDomainBO> allSimpleDomains = allSimpleDomainsResponse.getValue();

		assertEquals(numSimpleDomains, allSimpleDomains.size());

		assertEquals(0, allSimpleDomains.iterator().next().getSimpleDomainChilds().size());
		
		assertEquals(numSimpleDomainChildren, allSimpleDomains.iterator().next().getSimpleDomainEagerChilds().size());		
	}	

	@Test
	@RunAsClient
	public void testLazyLoadRelationViaAccessor(@ArquillianResteasyResource ResteasyWebTarget webTarget)
			throws Exception {
		SimpleDomainMaintenanceService maintenanceService = getMaintenanceService(webTarget);
		int numChildrenToCreate = 5;
		ValueServiceResponse<SimpleDomainBO> response = maintenanceService
				.saveOrUpdate(TestUtils.createRandomSimpleDomain(numChildrenToCreate));

		String updatedChildName = RandomStringUtils.randomAlphabetic(5);
		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<SimpleDomainBO> postUpdateResponse = managerService
				.updateAllSimpleDomainChildNames(response.getValue().getKey().toString(), updatedChildName);
		TestUtils.assertNoErrorMessages(postUpdateResponse);
		SimpleDomainBO updatedSimpleDomain = postUpdateResponse.getValue();
		assertNotNull(updatedSimpleDomain);

		Set<SimpleDomainChildBO> updatedChildren = updatedSimpleDomain.getSimpleDomainChilds();
		assertNotNull(updatedChildren);
		assertEquals(numChildrenToCreate, updatedChildren.size());
		assertEquals(updatedChildName, updatedChildren.iterator().next().getName());
	}

	private SimpleDomainManagerService getService(ResteasyWebTarget webTarget) {
		webTarget = initWebTarget(webTarget);
		SimpleDomainManagerService simpleDomainManager = webTarget.proxy(SimpleDomainManagerService.class);
		return simpleDomainManager;
	}

	private SimpleDomainMaintenanceService getMaintenanceService(ResteasyWebTarget webTarget) {
		webTarget = initWebTarget(webTarget);
		SimpleDomainMaintenanceService maintenanceService = webTarget.proxy(SimpleDomainMaintenanceService.class);
		return maintenanceService;
	}
}

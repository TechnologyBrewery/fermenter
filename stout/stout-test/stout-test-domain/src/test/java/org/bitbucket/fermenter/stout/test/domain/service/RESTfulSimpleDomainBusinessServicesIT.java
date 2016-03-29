package org.bitbucket.fermenter.stout.test.domain.service;

import static org.bitbucket.fermenter.stout.test.domain.service.TestUtils.assertNoErrorMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.bitbucket.fermenter.stout.test.domain.service.ejb.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import org.bitbucket.fermenter.stout.test.domain.transfer.SimpleDomain;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RESTfulSimpleDomainBusinessServicesIT {

	@ArquillianResource
	private URL base;

	@Deployment(testable = false)
	protected static Archive<?> createDeployment() {
		return ArquillianITUtil.createFermenterTestDomainDeployment();
	}

	@Before
	public void setup() {
		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
		factory.registerProvider(JacksonObjectMapperResteasyProvider.class);
		RegisterBuiltin.register(factory);

	}

	@Test
	public void testEjbLookup(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		assertNotNull(managerService);
	}

	@Test
	public void testSomeBusinessOperation(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

		ValueServiceResponse<SimpleDomain> responseDomainWrapper = managerService.someBusinessOperation(domain,
				someImportantInfo);

		assertNotNull(responseDomainWrapper);
		assertNoErrorMessages(responseDomainWrapper);
		SimpleDomain responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.endsWith(someImportantInfo));

	}

	@Test
	public void testCountPassedCollection(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		int numberOfItems = RandomUtils.nextInt(0, 10);
		List<SimpleDomain> list = new ArrayList<SimpleDomain>();
		for (int i = 0; i < numberOfItems; i++) {
			SimpleDomain domain = TestUtils.createRandomSimpleDomain();
			domain.setName("item " + i);
			list.add(domain);
		}

		ValueServiceResponse<Integer> response = managerService.count(list);

		assertNotNull(response);
		assertNoErrorMessages(response);
		Integer count = response.getValue();
		assertNotNull(count);
		assertTrue(count == numberOfItems);

	}

	@Test
	public void testDoSomething(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		VoidServiceResponse response = managerService.doSomething();

		assertNotNull(response);
		assertNoErrorMessages(response);

		response = managerService.doSomethingWithPrimitiveInputs(RandomStringUtils.randomAlphabetic(5),
				RandomUtils.nextInt(0, 10));
		assertNotNull(response);
		assertNoErrorMessages(response);
	}

	@Test
	public void testDoSomethingAndReturnACharacter(
			@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<String> response = managerService.doSomethingAndReturnACharacter();

		assertNotNull(response);
		assertNoErrorMessages(response);
		assertNotNull(response.getValue());

	}

	@Test
	public void testEchoPlusWazzup(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		final String root = RandomStringUtils.randomAlphanumeric(5);
		ValueServiceResponse<String> response = managerService.echoPlusWazzup(root);

		assertNotNull(response);
		assertNoErrorMessages(response);
		String echoResponse = response.getValue();
		assertNotNull(echoResponse);
		assertTrue(echoResponse.startsWith(root));
		assertTrue(echoResponse.endsWith("WAZZUP"));

	}

	@Test
	public void testGetSimpleDomainCount(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<Long> response = managerService.getSimpleDomainCount();

		assertNotNull(response);
		assertNoErrorMessages(response);
		Long count = response.getValue();
		assertNotNull(count);
		assertTrue(count > 0);

	}

	@Test
	public void testSelectAllSimpleDomains(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<Collection<SimpleDomain>> response = managerService.selectAllSimpleDomains();

		assertNotNull(response);
		assertNoErrorMessages(response);
		Collection<SimpleDomain> domains = response.getValue();
		assertNotNull(domains);
		assertTrue(domains.size() > 0);

	}

	@Test
	public void testSelectAllSimpleDomainsByType(
			@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		final String type = RandomStringUtils.randomAlphanumeric(5);
		ValueServiceResponse<Collection<SimpleDomain>> response = managerService.selectAllSimpleDomainsByType(type);

		assertNotNull(response);
		assertNoErrorMessages(response);
		Collection<SimpleDomain> domains = response.getValue();
		assertNotNull(domains);
		assertTrue(domains.size() > 0);
		for (SimpleDomain domain : domains) {
			assertEquals(type, domain.getType());
		}

	}

	@Test
	public void testReturnNullEntityInBusinessServiceOperation(
			@ArquillianResteasyResource SimpleDomainManagerService managerService) throws Exception {
		ValueServiceResponse<SimpleDomain> response = managerService.returnNullEntity();
		TestUtils.assertNoErrorMessages(response);
		assertEquals("Unexpectedly received a non-null SimpleDomain entity", response.getValue(), null);
	}
}

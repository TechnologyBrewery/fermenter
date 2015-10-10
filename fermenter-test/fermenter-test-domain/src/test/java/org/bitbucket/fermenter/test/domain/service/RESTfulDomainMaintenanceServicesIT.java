package org.bitbucket.fermenter.test.domain.service;

import static org.bitbucket.fermenter.test.domain.service.TestUtils.assertNoErrorMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.fermenter.service.ValueServiceResponse;
import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainMaintenanceRestService;
import org.bitbucket.fermenter.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;
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
public class RESTfulDomainMaintenanceServicesIT {

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
	public void testDomainMaintenanceGet(
			@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.findByPrimaryKey("BAD_PK");
		assertNotNull(result);
		assertNull(result.getValue());
	}

	@Test
	public void testDomainMaintenanceCreate(
			@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		double expectedBigDecimalAttributeValue = RandomUtils.nextDouble(0.0d, 1000.0d);
		SimpleDomain domain = TestUtils.createRandomSimpleDomain(expectedBigDecimalAttributeValue);

		ValueServiceResponse<SimpleDomain> result = simpleDomainService.saveOrUpdate(domain);
		assertNoErrorMessages(result);
		SimpleDomain savedDomain = result.getValue();
		assertNotNull(savedDomain);
		String id = savedDomain.getId();
		assertNotNull(id);

		ValueServiceResponse<SimpleDomain> foundResult = simpleDomainService.findByPrimaryKey(id);
		assertNoErrorMessages(foundResult);

		SimpleDomain retrievedDomain = foundResult.getValue();
		assertNotNull(retrievedDomain);
		assertEquals("Persisted BigDecimal attribute did not match the expected value",
				TestUtils.roundToHSQLDBDefaultDecimalType(BigDecimal.valueOf(expectedBigDecimalAttributeValue)),
				retrievedDomain.getBigDecimalValue());
	}

	@Test
	public void testDomainMaintenanceUpdate(
			@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();

		ValueServiceResponse<SimpleDomain> result = simpleDomainService.saveOrUpdate(domain);
		assertNotNull(result);
		SimpleDomain savedDomain = result.getValue();
		assertNotNull(savedDomain);
		String id = savedDomain.getId();
		String originalName = savedDomain.getName();
		savedDomain.setName(RandomStringUtils.randomAlphabetic(3));

		ValueServiceResponse<SimpleDomain> updateResult = simpleDomainService.saveOrUpdate(id, savedDomain);
		assertNoErrorMessages(updateResult);

		ValueServiceResponse<SimpleDomain> foundResult = simpleDomainService.findByPrimaryKey(id);
		assertNotNull(foundResult);
		SimpleDomain refetchedUpdatedDomain = foundResult.getValue();
		assertNotNull(refetchedUpdatedDomain);
		assertFalse(originalName.equals(refetchedUpdatedDomain.getName()));

	}

	@Test
	public void testDomainMaintenanceDelete(
			@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();

		ValueServiceResponse<SimpleDomain> result = simpleDomainService.saveOrUpdate(domain);
		assertNotNull(result);
		SimpleDomain savedDomain = result.getValue();
		assertNotNull(savedDomain);
		String id = savedDomain.getId();

		ValueServiceResponse<SimpleDomain> deleteResult = simpleDomainService.delete(id);
		assertNoErrorMessages(deleteResult);

		ValueServiceResponse<SimpleDomain> foundResult = simpleDomainService.findByPrimaryKey(id);
		assertNotNull(foundResult);
		assertNull(foundResult.getValue());

	}

}

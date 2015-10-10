package org.bitbucket.fermenter.stout.test.domain.service;

import static org.bitbucket.fermenter.stout.test.domain.service.TestUtils.assertNoErrorMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.test.domain.persist.SimpleDomainDao;
import org.bitbucket.fermenter.stout.test.domain.service.ejb.SimpleDomainMaintenanceService;
import org.bitbucket.fermenter.stout.test.domain.service.ejb.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.stout.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.stout.test.domain.transfer.SimpleDomainPK;
import org.bitbucket.fermenter.stout.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExampleChild;
import org.bitbucket.fermenter.stout.test.domain.transfer.ValidationExamplePK;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DomainMaintenanceServicesIT {

	@Inject
	private SimpleDomainDao simpleDomainDao;

	@EJB
	private SimpleDomainMaintenanceService simpleDomainMaintenaceService;

	@EJB
	private ValidationExampleMaintenanceService validationExampleMaintenanceService;

	@Deployment
	protected static Archive<?> createDeployment() {
		return ArquillianITUtil.createFermenterTestDomainDeployment();
	}

	@Test
	public void testEjbLookup() {
		assertNotNull(simpleDomainMaintenaceService);
	}

	@Test
	public void testDaoLookup() {
		assertNotNull(simpleDomainDao);
	}

	@Test
	public void testDomainMaintenanceCreateWithoutChildren() {
		double expectedBigDecimalAttributeValue = RandomUtils.nextDouble(0.0d, 1.0d);
		SimpleDomain domain = TestUtils.createRandomSimpleDomain(expectedBigDecimalAttributeValue);

		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);
		assertNotNull(responseDomainWrapper);
		assertNoErrorMessages(responseDomainWrapper);
		SimpleDomain responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String pk = responseDomain.getSimpleDomainPK().getId();
		assertNotNull(pk);

		ValueServiceResponse<SimpleDomain> retrievedDomainResponseWrapper = simpleDomainMaintenaceService
				.findByPrimaryKey(responseDomain.getSimpleDomainPK());
		assertNotNull(retrievedDomainResponseWrapper);
		assertNoErrorMessages(retrievedDomainResponseWrapper);
		SimpleDomain retrievedDomain = retrievedDomainResponseWrapper.getValue();
		assertNotNull(retrievedDomain);
		assertEquals("Persisted BigDecimal attribute did not match the expected value",
				TestUtils.roundToHSQLDBDefaultDecimalType(BigDecimal.valueOf(expectedBigDecimalAttributeValue)),
				retrievedDomain.getBigDecimalValue());
	}

	@Test
	public void testDomainMaintenanceRetrieveWithoutChildren() {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();

		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);

		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService
				.findByPrimaryKey(originalPk);
		assertNoErrorMessages(findResponseDomainWrapper);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNotNull(foundDomain);

	}

	@Test
	public void testDomainMaintenanceUpdateWithoutChildren() {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();

		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);

		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService
				.findByPrimaryKey(originalPk);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNotNull(foundDomain);
		final String newName = RandomStringUtils.randomAlphabetic(15);
		foundDomain.setName(newName);
		ValueServiceResponse<SimpleDomain> updateResponseDomainWrapper = simpleDomainMaintenaceService
				.saveOrUpdate(foundDomain);

		assertNotNull(updateResponseDomainWrapper);
		assertNoErrorMessages(updateResponseDomainWrapper);
		SimpleDomain responseDomain = updateResponseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		assertEquals(originalPk.getId(), responseDomain.getSimpleDomainPK().getId());
		assertEquals(newName, responseDomain.getName());

	}

	@Test
	public void testDomainMaintenanceDeleteWithoutChildren() {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();

		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);

		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService.delete(originalPk);
		assertNoErrorMessages(findResponseDomainWrapper);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNull(foundDomain);

	}

	@Test
	public void testDomainMaintenanceCreateWith1MChildren() {
		ValidationExample parent = TestUtils.createRandomValidationExample();
		ValidationExampleChild child1 = TestUtils.createRandomValidationExampleChild();
		ValidationExampleChild child2 = TestUtils.createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);

		int numberOfChildren = 2;

		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		validateExpectedResponse(numberOfChildren, response);

	}

	@Test
	public void testDomainMaintenanceRetrieveWith1MChildren() {
		ValidationExample parent = TestUtils.createRandomValidationExample();
		ValidationExampleChild child1 = TestUtils.createRandomValidationExampleChild();
		ValidationExampleChild child2 = TestUtils.createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);

		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);

		ValueServiceResponse<ValidationExample> findResponse = validationExampleMaintenanceService
				.findByPrimaryKey(response.getValue().getValidationExamplePK());
		validateExpectedResponse(2, findResponse);

	}

	@Test
	public void testDomainMaintenanceUpdateWith1MChildren() {
		ValidationExample parent = TestUtils.createRandomValidationExample();
		ValidationExampleChild child = TestUtils.createRandomValidationExampleChild();
		parent.addValidationExampleChild(child);

		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);

		ValidationExample responseParent = response.getValue();
		ValidationExampleChild responseChild = responseParent.getValidationExampleChilds().iterator().next();
		final String updatedValue = "THIS IS AN UPDATE";
		responseChild.setRequiredField(updatedValue);

		ValueServiceResponse<ValidationExample> updateResponse = validationExampleMaintenanceService
				.saveOrUpdate(responseParent);
		assertNoErrorMessages(updateResponse);

		ValueServiceResponse<ValidationExample> findResponse = validationExampleMaintenanceService
				.findByPrimaryKey(responseParent.getValidationExamplePK());
		assertNoErrorMessages(findResponse);

		ValidationExampleChild responseChildPostUpdate = responseParent.getValidationExampleChilds().iterator().next();
		assertEquals(updatedValue, responseChildPostUpdate.getRequiredField());

	}

	@Test
	public void testDomainMaintenanceDeleteWith1MChildren() {
		ValidationExample parent = TestUtils.createRandomValidationExample();
		ValidationExampleChild child1 = TestUtils.createRandomValidationExampleChild();
		ValidationExampleChild child2 = TestUtils.createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);

		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		ValidationExamplePK responseParentPk = response.getValue().getValidationExamplePK();

		ValueServiceResponse<ValidationExample> deleteResponse = validationExampleMaintenanceService
				.delete(responseParentPk);
		assertNoErrorMessages(deleteResponse);

		ValueServiceResponse<ValidationExample> findResponse = validationExampleMaintenanceService
				.findByPrimaryKey(responseParentPk);
		assertNull(findResponse.getValue());

	}

	@Test
	public void testDomainMaintenanceSaveInvalidEntity() {
		ValidationExample invalidEntity = TransferObjectFactory.createValidationExample();
		invalidEntity.setIntegerExample(123456789);
		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService
				.saveOrUpdate(invalidEntity);
		TestUtils.assertErrorMessagesInResponse(response, 2);
	}

	@Test
	public void testDomainMaintenanceSaveInvalidChildEntity() {
		ValidationExample parent = TestUtils.createRandomValidationExample();
		parent.addValidationExampleChild(TestUtils.createRandomValidationExampleChild());
		parent.addValidationExampleChild(TransferObjectFactory.createValidationExampleChild());

		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		TestUtils.assertErrorMessagesInResponse(response, 1);
	}

	protected void validateExpectedResponse(int numberOfChildren, ValueServiceResponse<ValidationExample> response) {
		assertNotNull(response);
		assertNoErrorMessages(response);
		ValidationExample responseDomain = response.getValue();
		assertNotNull(responseDomain);
		assertNotNull(responseDomain.getKey().getValue());

		Set<ValidationExampleChild> children = responseDomain.getValidationExampleChilds();
		assertNotNull(children);
		assertEquals(numberOfChildren, children.size());
		for (ValidationExampleChild child : children) {
			assertNotNull(child.getKey().getValue());
		}
	}

}

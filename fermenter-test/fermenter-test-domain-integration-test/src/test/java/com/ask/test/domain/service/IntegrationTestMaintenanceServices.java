package com.ask.test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Date;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.persist.hibernate.HibernateSessionFactoryManager;
import org.tigris.atlas.service.ValueServiceResponse;
import org.tigris.atlas.transfer.TransferObject;

import com.ask.test.domain.persist.SimpleDomainDao;
import com.ask.test.domain.service.ejb.SimpleDomainMaintenanceService;
import com.ask.test.domain.service.ejb.ValidationExampleMaintenanceService;
import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.SimpleDomainPK;
import com.ask.test.domain.transfer.TransferObjectFactory;
import com.ask.test.domain.transfer.ValidationExample;
import com.ask.test.domain.transfer.ValidationExampleChild;
import com.ask.test.domain.transfer.ValidationExamplePK;

@RunWith(Arquillian.class)
public class IntegrationTestMaintenanceServices {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestMaintenanceServices.class);
	
	//TODO: figure out how to make this use the current project version instead of a hardcoded value:
	private static final String DOMAIN_GROUPID_ARTIFACTID_VERSION = "com.ask.fermenter:fermenter-test-domain:1-SNAPSHOT";
	
	@Inject
	private SimpleDomainDao simpleDomainDao;
	
	@EJB
	private SimpleDomainMaintenanceService simpleDomainMaintenaceService;
	
	@EJB
	private ValidationExampleMaintenanceService validationExampleMaintenanceService;

	@Deployment
	public static Archive<?> createDeployment() {
		MavenResolverSystem mavenResolver = Maven.resolver();
		File[] mavenDependencies = mavenResolver.resolve(DOMAIN_GROUPID_ARTIFACTID_VERSION).withTransitivity().asFile();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "maintenance-service-integration-test.war");
		war.addAsLibraries(mavenDependencies);
		war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.addAsWebInfResource(new File("./src/test/resources/web.xml"));
		war.addAsWebInfResource(new File("./src/test/resources/jboss-deployment-structure.xml"));
		
		return war;
	}
	
	@Before
	public void prepDatabase() {
		//trigger schema update before we are in a CMT transaction so that it doesn't
		//blow up due to its use of autocommit:
		HibernateSessionFactoryManager.getInstance();
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
		SimpleDomain domain = createRandomSimpleDomain();
		
		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);
		assertNotNull(responseDomainWrapper);
		assertNoErrorMessages(responseDomainWrapper);
		SimpleDomain responseDomain = responseDomainWrapper.getValue(); 
		assertNotNull(responseDomain);
		String pk = responseDomain.getSimpleDomainPK().getId();
		assertNotNull(pk);
		
		LOGGER.debug(pk);
		
	}
	
	@Test
	public void testDomainMaintenanceRetrieveWithoutChildren() {
		SimpleDomain domain = createRandomSimpleDomain();
		
		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);
		
		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService.findByPrimaryKey(originalPk);
		assertNoErrorMessages(findResponseDomainWrapper);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNotNull(foundDomain);
		
	}
	
	@Test
	public void testDomainMaintenanceUpateWithoutChildren() {
		SimpleDomain domain = createRandomSimpleDomain();
		
		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);
		
		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService.findByPrimaryKey(originalPk);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNotNull(foundDomain);
		final String newName = RandomStringUtils.randomAlphabetic(15);
		foundDomain.setName(newName);
		ValueServiceResponse<SimpleDomain> updateResponseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(foundDomain);
		
		assertNotNull(updateResponseDomainWrapper);
		assertNoErrorMessages(updateResponseDomainWrapper);
		SimpleDomain responseDomain = updateResponseDomainWrapper.getValue(); 
		assertNotNull(responseDomain);
		assertEquals(originalPk.getId(), responseDomain.getSimpleDomainPK().getId());
		assertEquals(newName, responseDomain.getName());
		
	}
	
	@Test
	public void testDomainMaintenanceDeleteWithoutChildren() {
		SimpleDomain domain = createRandomSimpleDomain();
		
		ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainMaintenaceService.saveOrUpdate(domain);
		
		SimpleDomainPK originalPk = responseDomainWrapper.getValue().getSimpleDomainPK();
		ValueServiceResponse<SimpleDomain> findResponseDomainWrapper = simpleDomainMaintenaceService.delete(originalPk);
		assertNoErrorMessages(findResponseDomainWrapper);
		SimpleDomain foundDomain = findResponseDomainWrapper.getValue();
		assertNull(foundDomain);
		
	}
	
	@Test
	public void testDomainMaintenanceCreateWith1MChildren() {
		ValidationExample parent = createRandomValidationExample();
		ValidationExampleChild child1 = createRandomValidationExampleChild();
		ValidationExampleChild child2 = createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);
		
		int numberOfChildren = 2;
		
		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		validateExpectedResponse(numberOfChildren, response);
		
	}
	
	@Test
	public void testDomainMaintenanceRetrieveWith1MChildren() {
		ValidationExample parent = createRandomValidationExample();
		ValidationExampleChild child1 = createRandomValidationExampleChild();
		ValidationExampleChild child2 = createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);
		
		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		
		ValueServiceResponse<ValidationExample> findResponse = 
				validationExampleMaintenanceService.findByPrimaryKey(response.getValue().getValidationExamplePK());
		validateExpectedResponse(2, findResponse);
		
	}	
	
	@Test
	public void testDomainMaintenanceUpdateWith1MChildren() {
		ValidationExample parent = createRandomValidationExample();
		ValidationExampleChild child = createRandomValidationExampleChild();
		parent.addValidationExampleChild(child);
		
		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		
		ValidationExample responseParent = response.getValue();
		ValidationExampleChild responseChild = responseParent.getValidationExampleChilds().iterator().next();
		final String updatedValue = "THIS IS AN UPDATE";
		responseChild.setRequiredField(updatedValue);
		
		ValueServiceResponse<ValidationExample> updateResponse = validationExampleMaintenanceService.saveOrUpdate(responseParent);
		assertNoErrorMessages(updateResponse);
		
		ValueServiceResponse<ValidationExample> findResponse = 
				validationExampleMaintenanceService.findByPrimaryKey(responseParent.getValidationExamplePK());
		assertNoErrorMessages(findResponse);
		
		ValidationExampleChild responseChildPostUpdate = responseParent.getValidationExampleChilds().iterator().next();
		assertEquals(updatedValue, responseChildPostUpdate.getRequiredField());
		
	}	
	
	@Test
	public void testDomainMaintenanceDeleteWith1MChildren() {
		ValidationExample parent = createRandomValidationExample();
		ValidationExampleChild child1 = createRandomValidationExampleChild();
		ValidationExampleChild child2 = createRandomValidationExampleChild();
		parent.addValidationExampleChild(child1);
		parent.addValidationExampleChild(child2);
		
		ValueServiceResponse<ValidationExample> response = validationExampleMaintenanceService.saveOrUpdate(parent);
		ValidationExamplePK responseParentPk = response.getValue().getValidationExamplePK();
		
		ValueServiceResponse<ValidationExample> deleteResponse = validationExampleMaintenanceService.delete(responseParentPk);
		assertNoErrorMessages(deleteResponse);
		
		ValueServiceResponse<ValidationExample> findResponse 
			= validationExampleMaintenanceService.findByPrimaryKey(responseParentPk);
		assertNull(findResponse.getValue());
		
	}
	
	protected SimpleDomain createRandomSimpleDomain() {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		domain.setName(RandomStringUtils.randomAlphanumeric(25));
		domain.setTheDate1(new Date(RandomUtils.nextLong()));
		domain.setTheLong1(RandomUtils.nextLong());
		domain.setType(RandomStringUtils.random(5));
		return domain;
	}
	
	protected ValidationExample createRandomValidationExample() {
		ValidationExample domain = TransferObjectFactory.createValidationExample();
		domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
		return domain;
	}
	
	protected ValidationExampleChild createRandomValidationExampleChild() {
		ValidationExampleChild domain = TransferObjectFactory.createValidationExampleChild();
		domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
		return domain;
	}
	
	protected void assertNoErrorMessages(ValueServiceResponse<? extends TransferObject> response) {
		if (response != null) {
			Messages messages = response.getMessages();
			assertFalse(messages.hasErrorMessages());
			
			TransferObject to = response.getValue();
			if (to != null) {
				Messages toMessages = to.getMessages();
				assertFalse(toMessages.hasErrorMessages());
			}
			
		}
		
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

package com.ask.test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Date;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.service.ValueServiceResponse;

import com.ask.test.domain.persist.SimpleDomainDao;
import com.ask.test.domain.service.ejb.SimpleDomainMaintenanceService;
import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.SimpleDomainPK;
import com.ask.test.domain.transfer.TransferObjectFactory;

@RunWith(Arquillian.class)
public class IntegrationTestMaintenanceServices {
	
	//TODO: figure out how to make this use the current project version instead of a hardcoded value:
	private static final String DOMAIN_GROUPID_ARTIFACTID_VERSION = "com.ask.fermenter:fermenter-test-domain:1-SNAPSHOT";
	
	@Inject
	private SimpleDomainDao simpleDomainDao;
	
	@EJB
	private SimpleDomainMaintenanceService simpleDomainMaintenaceService;

	@Deployment
	public static Archive<?> createDeployment() {
		MavenResolverSystem mavenResolver = Maven.resolver();
		File[] mavenDependencies = mavenResolver.resolve(DOMAIN_GROUPID_ARTIFACTID_VERSION).withTransitivity().asFile();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "maintenance-service-integration-test.war");
		war.addAsLibraries(mavenDependencies);
		war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.addAsWebInfResource(new File("./src/test/resources/web.xml"));
		
		return war;
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
		assertNotNull(responseDomain.getKey().getValue());
		
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
	
	protected SimpleDomain createRandomSimpleDomain() {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		domain.setName(RandomStringUtils.randomAlphanumeric(25));
		domain.setTheDate1(new Date(RandomUtils.nextLong()));
		domain.setTheLong1(RandomUtils.nextLong());
		domain.setType(RandomStringUtils.random(5));
		return domain;
	}
	
	protected void assertNoErrorMessages(ValueServiceResponse<SimpleDomain> response) {
		if (response != null) {
			Messages messages = response.getMessages();
			assertFalse(messages.hasErrorMessages());
			
		}
		
	}
	
	
}

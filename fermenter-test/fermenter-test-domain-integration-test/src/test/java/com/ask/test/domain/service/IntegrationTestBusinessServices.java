package com.ask.test.domain.service;

import static com.ask.test.domain.service.TestUtils.assertNoErrorMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;

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
import org.tigris.atlas.persist.hibernate.HibernateSessionFactoryManager;
import org.tigris.atlas.service.ValueServiceResponse;
import org.tigris.atlas.service.VoidServiceResponse;

import com.ask.test.domain.bizobj.SimpleDomainBO;
import com.ask.test.domain.service.ejb.SimpleDomainManagerService;
import com.ask.test.domain.transfer.SimpleDomain;

@RunWith(Arquillian.class)
public class IntegrationTestBusinessServices {

    @EJB
    private SimpleDomainManagerService simpleDomainManagerService;

    @Deployment
    protected static Archive<?> createDeployment() {
	MavenResolverSystem mavenResolver = Maven.resolver();
	File[] mavenDependencies = mavenResolver.resolve(TestUtils.DOMAIN_GROUPID_ARTIFACTID_VERSION)
		.withTransitivity().asFile();

	WebArchive war = ShrinkWrap.create(WebArchive.class, "business-service-integration-test.war");
	war.addAsLibraries(mavenDependencies);
	war.addClass(TestUtils.class);
	war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	war.addAsWebInfResource(new File("./src/test/resources/web.xml"));
	war.addAsWebInfResource(new File("./src/test/resources/jboss-deployment-structure.xml"));

	return war;
    }

    @Before
    public void prepDatabase() {
	// trigger schema update before we are in a CMT transaction so that it doesn't
	// blow up due to its use of autocommit:
	HibernateSessionFactoryManager.getInstance();
    }

    @Test
    public void testEjbLookup() {
	assertNotNull(simpleDomainManagerService);
    }

    @Test
    public void testSomeBusinessOperation() {
	SimpleDomain domain = TestUtils.createRandomSimpleDomain();
	final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

	ValueServiceResponse<SimpleDomain> responseDomainWrapper = simpleDomainManagerService.someBusinessOperation(
		domain, someImportantInfo);

	assertNotNull(responseDomainWrapper);
	assertNoErrorMessages(responseDomainWrapper);
	SimpleDomain responseDomain = responseDomainWrapper.getValue();
	assertNotNull(responseDomain);
	String name = responseDomain.getName();
	assertNotNull(name);
	assertTrue(name.endsWith(someImportantInfo));

    }

    @Test
    public void testCountPassedCollection() {
	int numberOfItems = RandomUtils.nextInt(10);
	List<SimpleDomain> list = new ArrayList<SimpleDomain>();
	for (int i = 0; i < numberOfItems; i++) {
	    SimpleDomain domain = TestUtils.createRandomSimpleDomain();
	    domain.setName("item " + i);
	    list.add(domain);
	}

	ValueServiceResponse<Integer> response = simpleDomainManagerService.count(list);

	assertNotNull(response);
	assertNoErrorMessages(response);
	Integer count = response.getValue();
	assertNotNull(count);
	assertTrue(count == numberOfItems);

    }

    @Test
    public void testDoSomething() {
	VoidServiceResponse response = simpleDomainManagerService.doSomething();

	assertNotNull(response);
	assertNoErrorMessages(response);

    }

    @Test
    public void testDoSomethingAndReturnACharacter() {
	ValueServiceResponse<String> response = simpleDomainManagerService.doSomethingAndReturnACharacter();

	assertNotNull(response);
	assertNoErrorMessages(response);
	assertNotNull(response.getValue());

    }

    @Test
    public void testEchoPlusWazzup() {
	final String root = RandomStringUtils.randomAlphanumeric(5);
	ValueServiceResponse<String> response = simpleDomainManagerService.echoPlusWazzup(root);

	assertNotNull(response);
	assertNoErrorMessages(response);
	String echoResponse = response.getValue();
	assertNotNull(echoResponse);
	assertTrue(echoResponse.startsWith(root));
	assertTrue(echoResponse.endsWith("WAZZUP"));

    }

    @Test
    public void testGetSimpleDomainCount() {
	ValueServiceResponse<Long> response = simpleDomainManagerService.getSimpleDomainCount();

	assertNotNull(response);
	assertNoErrorMessages(response);
	Long count = response.getValue();
	assertNotNull(count);
	assertTrue(count > 0);

    }

    @Test
    public void testSelectAllSimpleDomains() {
	ValueServiceResponse<Collection<SimpleDomain>> response = simpleDomainManagerService.selectAllSimpleDomains();

	assertNotNull(response);
	assertNoErrorMessages(response);
	Collection<SimpleDomain> domains = response.getValue();
	assertNotNull(domains);
	assertTrue(domains.size() > 0);

    }

    @Test
    public void testSelectAllSimpleDomainsByType() {
	final String type = RandomStringUtils.randomAlphanumeric(5);
	ValueServiceResponse<Collection<SimpleDomain>> response = simpleDomainManagerService
		.selectAllSimpleDomainsByType(type);

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
    public void testBusinessServiceMethodPropagateErrorMessages() throws Exception {
	int numErrorMessagesToGenerate = RandomUtils.nextInt(5);
	VoidServiceResponse response = simpleDomainManagerService
		.createAndPropagateErrorMessages(numErrorMessagesToGenerate);
	TestUtils.assertErrorMessagesInResponse(response, numErrorMessagesToGenerate);
    }

    @Test
    public void testRollbackBusinessServiceMethod() throws Exception {
	int numErrorMessagesToGenerate = RandomUtils.nextInt(5);
	String targetNameValue = RandomStringUtils.randomAlphabetic(20);
	ValueServiceResponse<SimpleDomain> response = simpleDomainManagerService
		.saveSimpleDomainEntityAndPropagateErrorMessages(targetNameValue, numErrorMessagesToGenerate);
	TestUtils.assertErrorMessagesInResponse(response, numErrorMessagesToGenerate);

	SimpleDomainBO persistedEntityWithSamePK = SimpleDomainBO.findByPrimaryKey(response.getValue()
		.getSimpleDomainPK());
	assertNull("SimpleDomain object that should have been rolled back was unexpected persisted",
		persistedEntityWithSamePK);
	List<SimpleDomainBO> persistedEntityWithSameName = SimpleDomainBO.selectAllSimpleDomainsByName(targetNameValue);
	assertTrue("SimpleDomain object that should have been rolled back was unexpected persisted",
		persistedEntityWithSameName == null || persistedEntityWithSameName.isEmpty());
    }
}

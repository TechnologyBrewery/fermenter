package com.ask.test.domain.service;

import static com.ask.test.domain.service.TestUtils.assertNoErrorMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tigris.atlas.service.ValueServiceResponse;
import org.tigris.atlas.service.VoidServiceResponse;

import com.ask.test.domain.service.ejb.SimpleDomainManagerService;
import com.ask.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import com.ask.test.domain.transfer.SimpleDomain;

@RunWith(Arquillian.class)
public class IntegrationTestRESTfulBusinessServices {

	@ArquillianResource
	private URL base;
	
	@Deployment(testable=false)
	protected static Archive<?> createDeployment() {
		MavenResolverSystem mavenResolver = Maven.resolver();
		File[] mavenDependencies = mavenResolver.resolve(TestUtils.DOMAIN_GROUPID_ARTIFACTID_VERSION).withTransitivity().asFile();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "business-service-integration-test.war");
		war.addAsLibraries(mavenDependencies);
		war.addClass(TestUtils.class);
		war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.addAsWebInfResource(new File("./src/test/resources/web.xml"));
		war.addAsWebInfResource(new File("./src/test/resources/jboss-deployment-structure.xml"));
		
		return war;
	}
	
	@Before
    public void setup() {
		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();		
		JacksonObjectMapperResteasyProvider objectMapperProvider = factory.getProvider(JacksonObjectMapperResteasyProvider.class);
		
		if (objectMapperProvider == null) {
			factory.registerProvider(JacksonObjectMapperResteasyProvider.class);
			RegisterBuiltin.register(factory);
		}
		
    }
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testEjbLookup(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		assertNotNull(managerService);
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testSomeBusinessOperation(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);
		
		ValueServiceResponse<SimpleDomain> responseDomainWrapper 
			= managerService.someBusinessOperation(domain, someImportantInfo);
		
		assertNotNull(responseDomainWrapper);
		assertNoErrorMessages(responseDomainWrapper);
		SimpleDomain responseDomain = responseDomainWrapper.getValue(); 
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.endsWith(someImportantInfo));
		
	}
	
	@Test @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public void testCountPassedCollection(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		int numberOfItems = RandomUtils.nextInt(10);
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
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testDoSomething(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		VoidServiceResponse response = managerService.doSomething();
		
		assertNotNull(response);
		assertNoErrorMessages(response);
		
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testDoSomethingAndReturnACharacter(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<String> response = managerService.doSomethingAndReturnACharacter();
		
		assertNotNull(response);
		assertNoErrorMessages(response);
		assertNotNull(response.getValue());
		
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
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
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testGetSimpleDomainCount(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<Long> response = managerService.getSimpleDomainCount();
		
		assertNotNull(response);
		assertNoErrorMessages(response);
		Long count = response.getValue();
		assertNotNull(count);
		assertTrue(count > 0);
		
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testSelectAllSimpleDomains(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
		ValueServiceResponse<Collection<SimpleDomain>> response = managerService.selectAllSimpleDomains();
		
		assertNotNull(response);
		assertNoErrorMessages(response);
		Collection<SimpleDomain> domains = response.getValue();
		assertNotNull(domains);
		assertTrue(domains.size() > 0);
		
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Test
	public void testSelectAllSimpleDomainsByType(@ArquillianResteasyResource SimpleDomainManagerService managerService) {
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
}

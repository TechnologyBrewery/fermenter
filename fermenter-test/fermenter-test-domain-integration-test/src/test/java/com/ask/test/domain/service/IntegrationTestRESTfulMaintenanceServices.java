package com.ask.test.domain.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static com.ask.test.domain.service.TestUtils.assertNoErrorMessages;

import java.io.File;
import java.net.URL;

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

import com.ask.test.domain.service.ejb.SimpleDomainMaintenanceRestService;
import com.ask.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import com.ask.test.domain.transfer.SimpleDomain;

@RunWith(Arquillian.class)
public class IntegrationTestRESTfulMaintenanceServices {
	
	@ArquillianResource
	private URL base;
	
	@Deployment(testable=false)
	protected static Archive<?> createDeployment() {
		MavenResolverSystem mavenResolver = Maven.resolver();
		File[] mavenDependencies = mavenResolver.resolve(TestUtils.DOMAIN_GROUPID_ARTIFACTID_VERSION).withTransitivity().asFile();		
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "maintenance-service-integration-test.war");
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
		factory.registerProvider(JacksonObjectMapperResteasyProvider.class);
		RegisterBuiltin.register(factory);
		
    }
	
	@Test
	public void testDomainMaintenanceGet(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.findByPrimaryKey("BAD_PK");
		assertNotNull(result);
		assertNull(result.getValue());
	}
	
	@Test
	public void testDomainMaintenanceCreate(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		SimpleDomain domain = TestUtils.createRandomSimpleDomain();
		
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.saveOrUpdate(domain);
		assertNoErrorMessages(result);
		SimpleDomain savedDomain = result.getValue();
		assertNotNull(savedDomain);
		String id = savedDomain.getId();
		assertNotNull(id);
		
		ValueServiceResponse<SimpleDomain> foundResult = simpleDomainService.findByPrimaryKey(id);
		assertNoErrorMessages(foundResult);
		assertNotNull(foundResult.getValue());
		
	}
	
	@Test
	public void testDomainMaintenanceUpdate(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
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
	public void testDomainMaintenanceDelete(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
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

package com.ask.test.domain.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;
import java.util.Date;

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

import com.ask.test.domain.service.ejb.SimpleDomainMaintenanceRestService;
import com.ask.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.TransferObjectFactory;

@RunWith(Arquillian.class)
public class IntegrationTestRESTfulMaintenanceServices {
	
	//TODO: figure out how to make this use the current project version instead of a hardcoded value:
	private static final String DOMAIN_GROUPID_ARTIFACTID_VERSION = "com.ask.fermenter:fermenter-test-domain:1-SNAPSHOT";
	
	@ArquillianResource
	private URL base;
	
	@Deployment (testable = false)
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
    public void setup(){
		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();		
		JacksonObjectMapperResteasyProvider objectMapperProvider = factory.getProvider(JacksonObjectMapperResteasyProvider.class);
		
		if (objectMapperProvider == null) {
			factory.registerProvider(JacksonObjectMapperResteasyProvider.class);
			RegisterBuiltin.register(factory);
		}
		
    }
	
	@Test
	public void testDomainMaintenanceGet(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.findByPrimaryKey("BAD_PK");
		assertNotNull(result);
		assertNull(result.getValue());
	}
	
	@Test
	public void testDomainMaintenanceCreate(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		domain.setName(RandomStringUtils.randomAlphanumeric(20));
		domain.setTheDate1(new Date());
		domain.setTheLong1(RandomUtils.nextLong());
		domain.setType(RandomStringUtils.randomAlphabetic(5));
		
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.saveOrUpdate(domain);
		assertNotNull(result);
		SimpleDomain savedDomain = result.getValue();
		assertNotNull(savedDomain);
		String id = savedDomain.getId();
		assertNotNull(id);
		
		ValueServiceResponse<SimpleDomain> foundResult = simpleDomainService.findByPrimaryKey(id);
		assertNotNull(foundResult);
		assertNotNull(foundResult.getValue());
		
	}	
	
}

package com.ask.test.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tigris.atlas.service.ValueServiceResponse;

import com.ask.test.domain.service.ejb.SimpleDomainMaintenanceRestService;
import com.ask.test.domain.service.rest.JacksonObjectMapperResteasyProvider;
import com.ask.test.domain.transfer.SimpleDomain;

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
	
//	@Test
//	public void testDomainMaintenanceGet() throws Exception {	
//		URL custom = new URL("http","localhost", 8080, "/maintenance-service-integration-test");
//		ClientRequest request = new ClientRequest(new URL(custom, "/rest/SimpleDomain/" + "4028818c446ca29d01446ca2a0c10005").toExternalForm());
//		ClientResponse<String> response = request.get(String.class);
//		int responseCode = response.getStatus();
//		assertEquals(200, responseCode);
//		
//	}
	
	@Test
	public void testDomainMaintenanceGet(@ArquillianResteasyResource SimpleDomainMaintenanceRestService simpleDomainService) {
		ValueServiceResponse<SimpleDomain> result = simpleDomainService.findByPrimaryKey("BAD_PK");
		assertNotNull(result);
		assertNull(result.getValue());
	}
	
}

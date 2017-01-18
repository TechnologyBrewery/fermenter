package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.JacksonObjectMapperResteasyProvider;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleDomainManagerIT extends AbstractArquillianTestSupport {

	@Before
	public void setup() {
		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
		factory.registerProvider(JacksonObjectMapperResteasyProvider.class);
		RegisterBuiltin.register(factory);
	}

	@Test
	@RunAsClient
	public void saveNewSimpleDomain(@ArquillianResteasyResource SimpleDomainMaintenanceService simpleDomainService)
			throws Exception {
		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertNotNull(retrievedSimpleDomain.getUpdatedAt());
	}

}

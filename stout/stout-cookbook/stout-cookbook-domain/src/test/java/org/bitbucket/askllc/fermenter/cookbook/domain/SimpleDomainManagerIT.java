package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleDomainManagerIT extends AbstractArquillianTestSupport {

	@Test
	@RunAsClient
	public void testSaveNewSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		webTarget = initWebTarget(webTarget);
		SimpleDomainMaintenanceService simpleDomainService = webTarget.proxy(SimpleDomainMaintenanceService.class);

		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertNotNull(retrievedSimpleDomain.getUpdatedAt());
	}

	@Test
	@RunAsClient
	public void testEcho(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		webTarget = initWebTarget(webTarget);
		SimpleDomainManagerService simpleDomainMgr = webTarget.proxy(SimpleDomainManagerService.class);
		ValueServiceResponse<String> response = simpleDomainMgr.echoPlusWazzup(RandomStringUtils.randomAlphabetic(10));

		TestUtils.assertNoErrorMessages(response);
		assertNotNull(response.getValue());
	}
}

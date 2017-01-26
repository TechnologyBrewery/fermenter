package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
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
    public void testDomainMaintenanceGet(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.findByPrimaryKey("BAD_PK");
        assertNotNull(result);
        assertNull(result.getValue());
    }
    
	@Test
	@RunAsClient
	public void testSaveNewSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertNotNull(retrievedSimpleDomain.getUpdatedAt());
	}
	
    @Test
    @RunAsClient
    public void testDomainMaintenanceUpdate(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();

        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        assertNotNull(result);
        SimpleDomainBO savedDomain = result.getValue();
        assertNotNull(savedDomain);
        String id = savedDomain.getKey();
        String originalName = savedDomain.getName();
        savedDomain.setName(RandomStringUtils.randomAlphabetic(3));

        ValueServiceResponse<SimpleDomainBO> updateResult = simpleDomainService.saveOrUpdate(id, savedDomain);
        TestUtils.assertNoErrorMessages(updateResult);

        ValueServiceResponse<SimpleDomainBO> foundResult = simpleDomainService.findByPrimaryKey(id);
        assertNotNull(foundResult);
        SimpleDomainBO refetchedUpdatedDomain = foundResult.getValue();
        assertNotNull(refetchedUpdatedDomain);
        assertFalse(originalName.equals(refetchedUpdatedDomain.getName()));

    }

    @Test
    @RunAsClient
    public void testDomainMaintenanceDelete(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();

        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        assertNotNull(result);
        SimpleDomainBO savedDomain = result.getValue();
        assertNotNull(savedDomain);
        String id = savedDomain.getKey();

        VoidServiceResponse deleteResult = simpleDomainService.delete(id);
        TestUtils.assertNoErrorMessages(deleteResult);

        ValueServiceResponse<SimpleDomainBO> foundResult = simpleDomainService.findByPrimaryKey(id);
        assertNotNull(foundResult);
        assertNull(foundResult.getValue());

    }	

    private SimpleDomainMaintenanceService getMaintenanceService(ResteasyWebTarget webTarget) {
        webTarget = initWebTarget(webTarget);
		SimpleDomainMaintenanceService simpleDomainService = webTarget.proxy(SimpleDomainMaintenanceService.class);
        return simpleDomainService;
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

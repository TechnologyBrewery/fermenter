package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleDomainMaintenanceIT extends AbstractArquillianTestSupport {

    @Test
    @RunAsClient
    public void testGetNonExistentSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.findByPrimaryKey("BAD_PK");
        assertNotNull(result);
        assertNull(result.getValue());
    }
    
	@Test
	@RunAsClient
	public void testGetSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
		int numChildEntities = RandomUtils.nextInt(5) + 2;
		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain(numChildEntities);
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		ValueServiceResponse<SimpleDomainBO> resultWrapper = simpleDomainService
				.findByPrimaryKey(simpleDomain.getKey());
		assertNotNull(resultWrapper);
		SimpleDomainBO result = resultWrapper.getValue();
		assertNotNull(result);
		assertEquals(numChildEntities, result.getSimpleDomainChilds().size());
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
	public void testDefaultSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		simpleDomain.setType(null);
		assertNull(simpleDomain.getType());
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertNotNull(retrievedSimpleDomain.getType());
	}
	
	@Test
	@RunAsClient
	public void testSaveNumericBooleanTrue(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertTrue(simpleDomain.getNumericBoolean());
	}
	
	@Test
	@RunAsClient
	public void testSaveNumericBooleanFalse(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

		SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
		simpleDomain.setNumericBoolean(false);
		ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
		TestUtils.assertNoErrorMessages(response);

		SimpleDomainBO retrievedSimpleDomain = response.getValue();
		assertEquals(simpleDomain.getKey(), retrievedSimpleDomain.getKey());
		assertFalse(simpleDomain.getNumericBoolean());
	}
	
    @Test
    @RunAsClient
    public void testUpdateExistingSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
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
    public void testDeleteExistingSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
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
    
    @Test
    @RunAsClient
    public void testDeleteSimpleDomainChild(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        int numChildEntities = RandomUtils.nextInt(5) + 5;
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain(numChildEntities);
        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        TestUtils.assertNoErrorMessages(result);

        SimpleDomainBO persistedSimpleDomain = result.getValue();
        Iterator<SimpleDomainChildBO> childIter = persistedSimpleDomain.getSimpleDomainChilds().iterator();
        SimpleDomainChildBO deletedChild = childIter.next();
        childIter.remove();

        result = simpleDomainService.saveOrUpdate(persistedSimpleDomain);
        TestUtils.assertNoErrorMessages(result);

        result = simpleDomainService.findByPrimaryKey(persistedSimpleDomain.getKey());
        TestUtils.assertNoErrorMessages(result);

        Set<SimpleDomainChildBO> children = result.getValue().getSimpleDomainChilds();
        assertEquals(numChildEntities - 1, children.size());
        assertFalse(children.stream().anyMatch(child -> child.equals(deletedChild)));
    }

    @Test
    @RunAsClient
    public void testUpdateSimpleDomainChildren(@ArquillianResteasyResource ResteasyWebTarget webTarget)
            throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain(RandomUtils.nextInt(5) + 5);
        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        TestUtils.assertNoErrorMessages(result);

        SimpleDomainBO persistedSimpleDomain = result.getValue();
        persistedSimpleDomain.getSimpleDomainChilds().clear();

        SimpleDomainChildBO child = new SimpleDomainChildBO();
        child.setName(RandomStringUtils.randomAlphabetic(10));
        persistedSimpleDomain.addSimpleDomainChild(child);

        result = simpleDomainService.saveOrUpdate(persistedSimpleDomain);
        TestUtils.assertNoErrorMessages(result);

        result = simpleDomainService.findByPrimaryKey(persistedSimpleDomain.getKey());
        persistedSimpleDomain = result.getValue();
        assertEquals(1, persistedSimpleDomain.getSimpleDomainChilds().size());

        String updatedChildName = RandomStringUtils.randomAlphabetic(10);
        persistedSimpleDomain.getSimpleDomainChilds().iterator().next().setName(updatedChildName);
        result = simpleDomainService.saveOrUpdate(persistedSimpleDomain);
        TestUtils.assertNoErrorMessages(result);

        result = simpleDomainService.findByPrimaryKey(persistedSimpleDomain.getKey());
        Set<SimpleDomainChildBO> children = result.getValue().getSimpleDomainChilds();
        assertEquals(1, children.size());
        assertEquals(updatedChildName, children.iterator().next().getName());
    }
    
    
    private SimpleDomainMaintenanceService getMaintenanceService(ResteasyWebTarget webTarget) {
        webTarget = initWebTarget(webTarget);
        SimpleDomainMaintenanceService simpleDomainService = webTarget.proxy(SimpleDomainMaintenanceService.class);
        return simpleDomainService;
    }    

}

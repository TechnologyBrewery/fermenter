package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.ValuedEnumerationExample;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.fermenter.stout.page.PageWrapper;
import org.bitbucket.fermenter.stout.page.json.FindByExampleCriteria;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.bitbucket.fermenter.stout.sort.SortWrapper;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleDomainMaintenanceIT extends RunTestsWithinArquillianWar {

    @ArquillianResource
    private URL deploymentURL;

    @Before
    public void deleteSimpleDomains() throws Exception {
        ResteasyClient client = new ResteasyClientBuilderImpl().build();
        client.target(deploymentURL.toURI()).path("rest").path("SimpleDomainManagerService")
                .path("deleteAllSimpleDomains").request().header("username", "testUser").post(null).close();
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
                .findByPrimaryKey(response.getValue().getKey());
        assertNotNull(resultWrapper);
        SimpleDomainBO result = resultWrapper.getValue();
        assertNotNull(result);
        assertEquals(numChildEntities, result.getSimpleDomainChilds().size());
    }

    @Test
    @RunAsClient
    public void testFindAllSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        int totalCount = 50;
        for (int i = 0; i < totalCount; i++) {
            SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
            simpleDomainService.saveOrUpdate(simpleDomain);
        }

        int page = 0;
        int size = 25;
        // TODO: generate field names as enums
        SortWrapper sort = new SortWrapper(OrderWrapper.ASC, "name");
        SimpleDomainBO simpleDomainBOWithoutAnyRestrictions = new SimpleDomainBO();
        FindByExampleCriteria<SimpleDomainBO> criteria = new FindByExampleCriteria<>(
                simpleDomainBOWithoutAnyRestrictions, Boolean.FALSE, page, size, sort);
        ValueServiceResponse<PageWrapper<SimpleDomainBO>> results = simpleDomainService.findByExample(criteria);

        PageWrapper<SimpleDomainBO> value = results.getValue();
        assertEquals("Total count of elements didn't match!", new Long(totalCount), value.getTotalResults());
        assertEquals("Expected there to be two pages of 25!", new Integer(2), value.getTotalPages());
        assertEquals("Page size didn't match!", new Integer(size), value.getNumberOfElements());
    }

    @Test
    @RunAsClient
    public void testSaveNewSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

        SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
        ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
        TestUtils.assertNoErrorMessages(response);

        SimpleDomainBO retrievedSimpleDomain = response.getValue();
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
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
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
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
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
        assertTrue(retrievedSimpleDomain.getNumericBoolean());
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
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
        assertFalse(retrievedSimpleDomain.getNumericBoolean());
    }

    @Test
    @RunAsClient
    public void testSaveNamedEnumeration(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

        SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
        SimpleDomainEnumeration expectedValue = simpleDomain.getAnEnumeratedValue();
        ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
        TestUtils.assertNoErrorMessages(response);

        SimpleDomainBO retrievedSimpleDomain = response.getValue();
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
        assertEquals(expectedValue, retrievedSimpleDomain.getAnEnumeratedValue());
    }

    @Test
    @RunAsClient
    public void testSaveValuedEnumeration(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);

        SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
        ValuedEnumerationExample expectedValue = simpleDomain.getAnotherEnumeratedValue();
        ValueServiceResponse<SimpleDomainBO> response = simpleDomainService.saveOrUpdate(simpleDomain);
        TestUtils.assertNoErrorMessages(response);

        SimpleDomainBO retrievedSimpleDomain = response.getValue();
        assertNotNull("No generated key added to the persistented object!", retrievedSimpleDomain.getKey());
        assertEquals(expectedValue, retrievedSimpleDomain.getAnotherEnumeratedValue());
    }

    @Test
    @RunAsClient
    public void testUpdateExistingSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget)
            throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();

        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        assertNotNull(result);
        SimpleDomainBO savedDomain = result.getValue();
        assertNotNull(savedDomain);
        UUID id = savedDomain.getKey();
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
    public void testDeleteExistingSimpleDomain(@ArquillianResteasyResource ResteasyWebTarget webTarget)
            throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();

        ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
        assertNotNull(result);
        SimpleDomainBO savedDomain = result.getValue();
        assertNotNull(savedDomain);
        UUID id = savedDomain.getKey();

        VoidServiceResponse deleteResult = simpleDomainService.delete(id);
        TestUtils.assertNoErrorMessages(deleteResult);

        try {
            simpleDomainService.findByPrimaryKey(id);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }

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

    @Test
    @RunAsClient
    public void testFindByExampleSimple(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = new SimpleDomainBO();
        String[] searchNames = new String[] { "name1", "name1", "name2", "name3" };
        for (String name : searchNames) {
            domain.setName(name);
            ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
            TestUtils.assertNoErrorMessages(result);
        }

        int page = 0;
        int size = 25;
        SortWrapper sort = new SortWrapper(OrderWrapper.ASC, "name");

        SimpleDomainBO probe = new SimpleDomainBO();
        probe.setName(searchNames[0]);
        FindByExampleCriteria<SimpleDomainBO> criteria = new FindByExampleCriteria<>(probe, Boolean.FALSE, page, size, sort);
        ValueServiceResponse<PageWrapper<SimpleDomainBO>> searchResults = simpleDomainService.findByExample(criteria);
        TestUtils.assertNoErrorMessages(searchResults);

        PageWrapper<SimpleDomainBO> value = searchResults.getValue();
        assertEquals(new Long(2), value.getTotalResults());
    }

    @Test
    @RunAsClient
    public void testFindByExamplePages(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = new SimpleDomainBO();
        String[] searchNames = new String[] { "name1", "name1", "name2", "name3" };
        for (String name : searchNames) {
            domain.setName(name);
            ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
            TestUtils.assertNoErrorMessages(result);
        }

        Integer page = 0;
        Integer size = 1;
        OrderWrapper order = new OrderWrapper(OrderWrapper.ASC, "name", true);
        SortWrapper sort = new SortWrapper(order);
        

        SimpleDomainBO probe = new SimpleDomainBO();
        probe.setName(searchNames[0]);
        FindByExampleCriteria<SimpleDomainBO> criteria = new FindByExampleCriteria<>(probe, Boolean.FALSE, page, size, sort);
        ValueServiceResponse<PageWrapper<SimpleDomainBO>> searchResults = simpleDomainService.findByExample(criteria);
        TestUtils.assertNoErrorMessages(searchResults);

        PageWrapper<SimpleDomainBO> value = searchResults.getValue();
        assertEquals(new Long(2), value.getTotalResults());
        assertEquals(new Integer(2), value.getTotalPages());
        assertEquals(size, value.getItemsPerPage());
    }

    @Test
    @RunAsClient
    public void testFindByExampleSort(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainMaintenanceService simpleDomainService = getMaintenanceService(webTarget);
        SimpleDomainBO domain = new SimpleDomainBO();
        String[] searchNames = new String[] { "bbb", "hhh", "aaa", "zzz" };
        for (String name : searchNames) {
            domain.setName(name);
            ValueServiceResponse<SimpleDomainBO> result = simpleDomainService.saveOrUpdate(domain);
            TestUtils.assertNoErrorMessages(result);
        }

        int page = 0;
        int size = 25;
        SortWrapper sort = new SortWrapper(OrderWrapper.ASC, "name");

        SimpleDomainBO probe = new SimpleDomainBO();
        FindByExampleCriteria<SimpleDomainBO> criteria = new FindByExampleCriteria<>(probe, Boolean.FALSE, page, size, sort);
        ValueServiceResponse<PageWrapper<SimpleDomainBO>> searchResults = simpleDomainService.findByExample(criteria);
        TestUtils.assertNoErrorMessages(searchResults);

        PageWrapper<SimpleDomainBO> value = searchResults.getValue();
        assertEquals(new Long(searchNames.length), value.getTotalResults());
        assertEquals(searchNames[2], value.getContent().get(0).getName());
        assertEquals(searchNames[0], value.getContent().get(1).getName());
        assertEquals(searchNames[1], value.getContent().get(2).getName());
        assertEquals(searchNames[3], value.getContent().get(3).getName());
    }

    private SimpleDomainMaintenanceService getMaintenanceService(ResteasyWebTarget webTarget) {
        webTarget = initWebTarget(webTarget);
        SimpleDomainMaintenanceService simpleDomainService = webTarget.proxy(SimpleDomainMaintenanceService.class);
        return simpleDomainService;
    }

}

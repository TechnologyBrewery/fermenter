package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.RunTestsWithinArquillianWar;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.SimpleDomainMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.SimpleDomainManagerDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.stout.page.PageWrapper;
import org.bitbucket.fermenter.stout.page.json.FindByExampleCriteria;
import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.bitbucket.fermenter.stout.sort.SortWrapper;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// Kicks off a test cookbook-domain war that can be used as the target of rest calls: 
@RunWith(Arquillian.class)
// Configures the rest-client with needed Spring support to wire underlying classes:
@SpringClientConfiguration("application-test-context.xml")
// Allows Spring to inject the service endpoints into this class:
@Component
public class SimpleDomainClientIT extends RunTestsWithinArquillianWar {

    @ArquillianResource
    private URL deploymentURL;

    @Inject
    private SimpleDomainManagerDelegate delegate;

    @Inject
    private SimpleDomainMaintenanceDelegate simpleDomainMaintenanceDelegate;

    @Before
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }    
    
    @After
    public void deleteSimpleDomains() throws Exception {
        delegate.deleteAllSimpleDomains();
        MessageTestUtils.assertNoErrorMessages();
    }

    @Test
    @RunAsClient
    public void testEcho() throws Exception {
        String response = delegate.echoPlusWazzup(RandomStringUtils.randomAlphabetic(10));

        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(response);
    }

    @Test
    @RunAsClient
    public void testPassingAnEntityAndPrimitive() throws Exception {

        assertNotNull("Delegate was not correctly injected!", delegate);

        SimpleDomain domain = TestUtils.createRandomSimpleDomain();
        final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

        SimpleDomain responseDomain = delegate.someBusinessOperation(domain, someImportantInfo);

        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(responseDomain);
        String name = responseDomain.getName();
        assertNotNull(name);
        assertTrue(name.endsWith(someImportantInfo));

    }

    @Test
    @RunAsClient
    public void testPassingListOfEntities() throws Exception {
        Integer numberOfItems = RandomUtils.nextInt(0, 10);
        List<SimpleDomain> list = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            SimpleDomain domain = TestUtils.createRandomSimpleDomain();
            domain.setName("item " + i);
            list.add(domain);
        }

        Integer response = delegate.countNumInputs(list);

        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(response);
        assertEquals(numberOfItems, response);

    }

    @Test
    @RunAsClient
    public void testReturnVoid() throws Exception {
        delegate.returnVoid();
        MessageTestUtils.assertNoErrorMessages();
    }

    @Test
    @RunAsClient
    public void testPassingPrimitivesAsParameters() throws Exception {
        delegate.doSomethingWithPrimitiveInputs(RandomStringUtils.randomAlphabetic(5), RandomUtils.nextInt(0, 10));
        MessageTestUtils.assertNoErrorMessages();
    }

    @Test
    @RunAsClient
    public void testReturningACharacter() throws Exception {
        String response = delegate.doSomethingAndReturnAPrimitive();

        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(response);

    }

    @Test
    @RunAsClient
    public void testReturnNullEntityInBusinessServiceOperation() throws Exception {
        SimpleDomain response = delegate.returnNullEntity();
        MessageTestUtils.assertNoErrorMessages();
        assertNull("Unexpectedly received a non-null SimpleDomain entity", response);
    }

    @Test
    @RunAsClient
    public void testSomeBusinessOperationWithEntityParam() throws Exception {
        SimpleDomain domain = TestUtils.createRandomSimpleDomain();
        domain.setId(UUID.randomUUID());
        final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);
        domain.setName(someImportantInfo);

        SimpleDomain responseDomain = delegate.methodWithSingleEntityAsParam(domain);
        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(responseDomain);
        String name = responseDomain.getName();
        assertNotNull(name);
        assertTrue(name.startsWith(someImportantInfo));
        assertTrue(name.endsWith("updated"));

    }

    @Test
    @RunAsClient
    public void testSelectAllSimpleDomains() throws Exception {
        int numSimpleDomains = RandomUtils.nextInt(5, 10);
        int numSimpleDomainChildren = RandomUtils.nextInt(2, 6);

        for (int iter = 0; iter < numSimpleDomains; iter++) {
            simpleDomainMaintenanceDelegate.create(TestUtils.createRandomSimpleDomain(numSimpleDomainChildren));
        }

        Collection<SimpleDomain> allSimpleDomains = delegate.selectAllSimpleDomains();
        MessageTestUtils.assertNoErrorMessages();

        assertEquals(numSimpleDomains, allSimpleDomains.size());

        assertEquals(numSimpleDomainChildren, allSimpleDomains.iterator().next().getSimpleDomainChilds().size());
    }
    
    @Test
    @RunAsClient
    public void testFindByExampleSimpleDomains() throws Exception {
        int numSimpleDomains = RandomUtils.nextInt(5, 10);
        int numSimpleDomainChildren = RandomUtils.nextInt(2, 6);

        SimpleDomain lastCreatedSimpleDomain = null;
        for (int iter = 0; iter < numSimpleDomains; iter++) {
             lastCreatedSimpleDomain = simpleDomainMaintenanceDelegate.create(TestUtils.createRandomSimpleDomain(numSimpleDomainChildren));
        }

        SortWrapper sort = new SortWrapper(OrderWrapper.ASC, "name");
        FindByExampleCriteria<SimpleDomain> criteria = new FindByExampleCriteria<>(new SimpleDomain(), 0, numSimpleDomains, sort);
        PageWrapper<SimpleDomain> allSimpleDomains = simpleDomainMaintenanceDelegate.findByExample(criteria);
        MessageTestUtils.assertNoErrorMessages();

        assertEquals(new Long(numSimpleDomains), allSimpleDomains.getTotalResults());
        boolean responseContainsLastCreatedSimpleDomain = false;
        for(SimpleDomain simpleDomain : allSimpleDomains.getContent()) {
            if(simpleDomain.getName().equals(lastCreatedSimpleDomain.getName())) {
                responseContainsLastCreatedSimpleDomain = true;
            }
        }
        assertTrue(responseContainsLastCreatedSimpleDomain);
    }

    @Test
    @RunAsClient
    public void testSelectAllSimpleDomainsEagerLazyLoadChild() throws Exception {
        int numSimpleDomains = RandomUtils.nextInt(5, 10);
        int numSimpleDomainChildren = RandomUtils.nextInt(2, 6);

        for (int iter = 0; iter < numSimpleDomains; iter++) {
            simpleDomainMaintenanceDelegate.create(TestUtils.createRandomSimpleDomain(numSimpleDomainChildren));
        }

        Collection<SimpleDomain> allSimpleDomains = delegate.selectAllSimpleDomainsLazySimpleDomainChild();
        MessageTestUtils.assertNoErrorMessages();

        assertEquals(numSimpleDomains, allSimpleDomains.size());
        assertEquals(0, allSimpleDomains.iterator().next().getSimpleDomainChilds().size());
        assertEquals(numSimpleDomainChildren, allSimpleDomains.iterator().next().getSimpleDomainEagerChilds().size());
    }

}

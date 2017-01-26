package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
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
public class SimpleDomainBusinessServicesIT extends AbstractArquillianTestSupport {

	@Test
	@RunAsClient
	public void testEcho(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
		SimpleDomainManagerService managerService = getService(webTarget);
		ValueServiceResponse<String> response = managerService.echoPlusWazzup(RandomStringUtils.randomAlphabetic(10));

		TestUtils.assertNoErrorMessages(response);
		assertNotNull(response.getValue());
	}
	
    @Test
    @RunAsClient
    public void testPassingAnEntityAndPrimitive(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
        final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

        ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService.someBusinessOperation(domain,
                someImportantInfo);

        assertNotNull(responseDomainWrapper);
        TestUtils.assertNoErrorMessages(responseDomainWrapper);
        SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
        assertNotNull(responseDomain);
        String name = responseDomain.getName();
        assertNotNull(name);
        assertTrue(name.endsWith(someImportantInfo));

    }
    
    @Test
    @RunAsClient
    public void testPassingListOfEntities(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        int numberOfItems = RandomUtils.nextInt(0, 10);
        List<SimpleDomainBO> list = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
            domain.setName("item " + i);
            list.add(domain);
        }

        ValueServiceResponse<Integer> response = managerService.countNumInputs(list);

        assertNotNull(response);
        TestUtils.assertNoErrorMessages(response);
        Integer count = response.getValue();
        assertNotNull(count);
        assertTrue(count == numberOfItems);

    }
    
    @Test
    @RunAsClient
    public void testReturnVoid(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        VoidServiceResponse response = managerService.returnVoid();

        assertNotNull(response);
        TestUtils.assertNoErrorMessages(response);
    }
    
    @Test
    @RunAsClient
    public void testPassingPrimitivesAsParameters(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        VoidServiceResponse response = managerService.doSomethingWithPrimitiveInputs(RandomStringUtils.randomAlphabetic(5),
                RandomUtils.nextInt(0, 10));
        assertNotNull(response);
        TestUtils.assertNoErrorMessages(response);
    }    

    @Test
    @RunAsClient
    public void testReturningACharacter(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        ValueServiceResponse<String> response = managerService.doSomethingAndReturnAPrimitive();

        assertNotNull(response);
        TestUtils.assertNoErrorMessages(response);
        assertNotNull(response.getValue());

    }

    @Test
    @RunAsClient
    public void testReturnNullEntityInBusinessServiceOperation(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        SimpleDomainManagerService managerService = getService(webTarget);
        ValueServiceResponse<SimpleDomainBO> response = managerService.returnNullEntity();
        TestUtils.assertNoErrorMessages(response);
        assertEquals("Unexpectedly received a non-null SimpleDomain entity", response.getValue(), null);
    }
    
    private SimpleDomainManagerService getService(ResteasyWebTarget webTarget) {
        webTarget = initWebTarget(webTarget);
        SimpleDomainManagerService simpleDomainManager = webTarget.proxy(SimpleDomainManagerService.class);
        return simpleDomainManager;
    }      
}

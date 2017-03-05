package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json.ObjectMapperManager;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Arquillian.class)
public class SimpleDomainBusinessServicesIT extends AbstractArquillianTestSupport {
	
	private ObjectMapper objectMapper = ObjectMapperManager.getObjectMapper();

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
    
	@Test
	@RunAsClient
	public void testSomeBusinessOperationWithEntityParam(@ArquillianResteasyResource ResteasyWebTarget webTarget) {
		SimpleDomainManagerService managerService = getService(webTarget);
		SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
		domain.setKey(UUID.randomUUID().toString());
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);
		domain.setName(someImportantInfo);

		ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService.methodWithSingleEntityAsParam(domain);

		assertNotNull(responseDomainWrapper);
		TestUtils.assertNoErrorMessages(responseDomainWrapper);
		SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.startsWith(someImportantInfo));
		assertTrue(name.endsWith("updated"));

	}
	
	@Test
	@RunAsClient
	public void testSomeBusinessOperationWithEntityParamViaPut(@ArquillianResteasyResource ResteasyWebTarget webTarget) {
		SimpleDomainManagerService managerService = getService(webTarget);
		SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
		domain.setKey(UUID.randomUUID().toString());
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);
		domain.setName(someImportantInfo);

		ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService.methodWithSingleEntityAsParamViaPut(domain);

		assertNotNull(responseDomainWrapper);
		TestUtils.assertNoErrorMessages(responseDomainWrapper);
		SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.startsWith(someImportantInfo));
		assertTrue(name.endsWith("updated"));

	}
	
	@Test
	@RunAsClient
	public void testSomeBusinessOperatonWithSimpleParamViaPut(@ArquillianResteasyResource ResteasyWebTarget webTarget) {
		SimpleDomainManagerService managerService = getService(webTarget);
		final String root = RandomStringUtils.randomAlphanumeric(5);
		ValueServiceResponse<String> response = managerService.echoPlusWazzupViaPut(root);

		assertNotNull(response);
		TestUtils.assertNoErrorMessages(response);
		String echoResponse = response.getValue();
		assertNotNull(echoResponse);
		assertTrue(echoResponse.startsWith(root));
		assertTrue(echoResponse.endsWith("Wazzup"));

	}
	
	@Test
	@RunAsClient
	public void testSomeBusinessOperatonWithMixedParamViaPut(@ArquillianResteasyResource ResteasyWebTarget webTarget) {
		SimpleDomainManagerService managerService = getService(webTarget);
		SimpleDomainBO domain = TestUtils.createRandomSimpleDomain();
		final String someImportantInfo = RandomStringUtils.randomAlphanumeric(5);

		ValueServiceResponse<SimpleDomainBO> responseDomainWrapper = managerService.someBusinessOperationViaPut(domain,
				someImportantInfo);

		assertNotNull(responseDomainWrapper);
		TestUtils.assertNoErrorMessages(responseDomainWrapper);
		SimpleDomainBO responseDomain = responseDomainWrapper.getValue();
		assertNotNull(responseDomain);
		String name = responseDomain.getName();
		assertNotNull(name);
		assertTrue(name.endsWith(someImportantInfo));
	}    
    
    private SimpleDomainManagerService getService(ResteasyWebTarget webTarget) {
        webTarget = initWebTarget(webTarget);
        SimpleDomainManagerService simpleDomainManager = webTarget.proxy(SimpleDomainManagerService.class);
        return simpleDomainManager;
    }      
}

package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.EnumerationBasedService;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EnumerationBasedServiceIT extends RunTestsWithinArquillianWar {

    @ArquillianResource
	private URL deploymentURL;
	
	@Test
	@RunAsClient
	public void testSingleParameterService(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
	    EnumerationBasedService enumerationService = getService(webTarget);
		VoidServiceResponse response = enumerationService.passInSingleEnumeratedValue(SimpleDomainEnumeration.FIRST);

		TestUtils.assertNoErrorMessages(response);
	}
	
    @Test
    @RunAsClient
    public void testMultipleParametersService(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        EnumerationBasedService enumerationService = getService(webTarget);
        List<SimpleDomainEnumeration> parameters = new ArrayList<>();
        parameters.add(SimpleDomainEnumeration.SECOND);
        parameters.add(SimpleDomainEnumeration.SECOND);
        VoidServiceResponse response = enumerationService.passInMultipleEnumeratedValues(parameters);

        TestUtils.assertNoErrorMessages(response);
    }
    
    @Test
    @RunAsClient
    public void testSingleReturnService(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        EnumerationBasedService enumerationService = getService(webTarget);
        ValueServiceResponse<SimpleDomainEnumeration> response = enumerationService.returnSingleEnumeratedValue();

        TestUtils.assertNoErrorMessages(response);
        assertEquals("Did not find expected enumeration value!", SimpleDomainEnumeration.FIRST, response.getValue());
    }

    @Test
    @RunAsClient
    public void testManyReturnService(@ArquillianResteasyResource ResteasyWebTarget webTarget) throws Exception {
        EnumerationBasedService enumerationService = getService(webTarget);
        ValueServiceResponse<Collection<SimpleDomainEnumeration>> response = enumerationService.returnMultipleEnumeratedValue();

        TestUtils.assertNoErrorMessages(response);
        assertEquals("Did not find number of enumeration values!", 3, response.getValue().size());
    }

	private EnumerationBasedService getService(ResteasyWebTarget webTarget) {
		webTarget = initWebTarget(webTarget);
		EnumerationBasedService enumerationBasedService = webTarget.proxy(EnumerationBasedService.class);
		return enumerationBasedService;
	}

}

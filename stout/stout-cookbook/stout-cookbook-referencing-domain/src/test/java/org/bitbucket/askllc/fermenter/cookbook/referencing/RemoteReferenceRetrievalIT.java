package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationReferencedObjectMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class RemoteReferenceRetrievalIT extends RunTestsOutsideOfArquillianWar {

    @ArquillianResource
    private URL deploymentURL;
    
    @Inject
    private ValidationReferencedObjectMaintenanceDelegate referenceMaintenanceDelegate;
    
    private LocalDomainBO localDomain;
    private ValidationReferencedObject reference;
    
    @Before
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After
    public void deleteSimpleDomains() throws Exception {
        localDomain.delete();
        referenceMaintenanceDelegate.delete(reference.getId());
        MessageTestUtils.assertNoErrorMessages();
    }

    @Test
    @RunAsClient
    public void testRemoteReferenceRetrieval() throws Exception {
        ValidationReferencedObject newReference = new ValidationReferencedObject();
        newReference.setId(UUID.randomUUID());
        newReference.setSomeDataField(RandomStringUtils.randomAlphanumeric(10));
        reference = referenceMaintenanceDelegate.create(newReference);
        MessageTestUtils.assertNoErrorMessages();
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName("testWithoutExternalReference");
        local.setExternalReference(reference);
        localDomain = local.save();
        
        LocalDomainBO retreivedLocal = LocalDomainBO.findByPrimaryKey(local.getKey());
        assertNotNull("Could not find newly minted local domain!", retreivedLocal);
        ValidationReferencedObject retreivedReference = retreivedLocal.getExternalReference();
        assertNotNull("Should have invoked the remote retrieval service!", retreivedReference);

        MessageTestUtils.assertNoErrorMessages();

    }

}

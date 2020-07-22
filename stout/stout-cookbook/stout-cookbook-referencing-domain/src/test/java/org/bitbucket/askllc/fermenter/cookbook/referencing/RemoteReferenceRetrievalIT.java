package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationReferencedObjectMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationTransientReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        AuthenticationTestUtils.login("testUser");
    }

    @After
    public void deleteSimpleDomains() throws Exception {
        if (localDomain != null) {
            localDomain.delete();
        }
        if (reference != null) {
            referenceMaintenanceDelegate.delete(reference.getId());
        }
        
        MessageTestUtils.assertNoErrorMessages();
        
        AuthenticationTestUtils.logout();
    }

    @Test
    @RunAsClient
    public void testRemoteReferenceRetrieval() throws Exception {
        ValidationReferencedObject newReference = new ValidationReferencedObject();
        newReference.setId(UUID.randomUUID());
        newReference.setSomeDataField(RandomStringUtils.randomAlphanumeric(10));
        reference = referenceMaintenanceDelegate.create(newReference);
        MessageTestUtils.assertNoErrorMessages();
        
        //remote transient entity reference
        ValidationTransientReferencedObject newTransientReference = new ValidationTransientReferencedObject();
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName("testWithExternalReference");
        local.setExternalReference(reference);
        //transient reference is required in LocalDomain entity
        local.setExternalTransientReference(newTransientReference);
        localDomain = local.save();
        
        LocalDomainBO retreivedLocal = LocalDomainBO.findByPrimaryKey(localDomain.getKey());
        assertNotNull("Could not find newly minted local domain!", retreivedLocal);
        ValidationReferencedObject retreivedReference = retreivedLocal.getExternalReference();
        assertNotNull("Should have invoked the remote retrieval service!", retreivedReference);

        MessageTestUtils.assertNoErrorMessages();

    }

}

package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.ReferenceValidationSteps;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationReferencedObjectMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringClientConfiguration({ "application-test-context.xml", "h2-spring-ds-context.xml" })
@Component
public class RemoteReferenceValidationSteps {
    
    @ArquillianResource
    private URL deploymentURL;
    
    @Inject
    private ValidationReferencedObjectMaintenanceDelegate referenceMaintenanceDelegate;
    
    private LocalDomainBO localDomain;
    private ValidationReferencedObject reference;
    
    @Before("@remoteReferenceValidation")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @After("@remoteReferenceValidation")
    public void cleanUp() throws Exception {
        localDomain.delete();
        referenceMaintenanceDelegate.delete(reference.getId());
    		MessageManagerInitializationDelegate.cleanupMessageManager();
    }
    
    @Given("^the \"([^\"]*)\" has a remote reference to an existing \"([^\"]*)\"$")
    public void the_has_a_remote_reference_to_an_existing(String address, String state) throws Throwable {
        ValidationReferencedObject newReference = new ValidationReferencedObject();
        newReference.setId(UUID.randomUUID());
        newReference.setSomeDataField(state);
        reference = referenceMaintenanceDelegate.create(newReference);
        MessageTestUtils.assertNoErrorMessages();
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName(address);
        local.setExternalReference(reference);
        localDomain = local;
    }
    
    
    @Given("^the \"([^\"]*)\" has a remote reference to a non-existing \"([^\"]*)\"$")
    public void the_has_a_remote_reference_to_a_non_existing(String address, String state) throws Throwable {
        ValidationReferencedObject newReference = new ValidationReferencedObject();
        newReference.setId(UUID.randomUUID());
        newReference.setSomeDataField(state);
        reference = referenceMaintenanceDelegate.create(newReference);
        MessageTestUtils.assertNoErrorMessages();
        
        referenceMaintenanceDelegate.delete(reference.getId());;
        MessageTestUtils.assertNoErrorMessages();
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName(address);
        local.setExternalReference(reference);
        localDomain = local;
    }
    
    
    @When("^the reference level validation is performed on the instance \"([^\"]*)\"$")
    public void the_reference_level_validation_is_performed_on_the_instance(String arg1) throws Throwable {
    		localDomain.validate();
    }

    @Then("^the reference level validation passes$")
    public void the_reference_level_validation_passes() throws Throwable {
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), RemoteReferenceValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
		
		localDomain.save();
		LocalDomainBO persisted = LocalDomainBO.findByPrimaryKey(localDomain.getKey());
		assertNotNull("LocalDomainBO should have been persisted to the DB", persisted);
    }


    @Then("^the reference level validation fails$")
    public void the_reference_level_validation_fails() throws Throwable {
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
		
		localDomain.save();
		assertNull("LocalDomainBO should not have been persisted to the DB", localDomain.getKey());
    }
}

package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.ReferenceValidationSteps;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationReferencedObjectMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationTransientReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainTransientReferenceBO;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalTransientDomainBO;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
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

@Component
public class RemoteReferenceValidationSteps {
    
    @ArquillianResource
    private URL deploymentURL;

    @Inject
    private ValidationReferencedObjectMaintenanceDelegate referenceMaintenanceDelegate;
    
    private LocalDomainBO localDomain;
    private LocalTransientDomainBO localTransientDomain;
    private LocalDomainTransientReferenceBO localTransientReferenceDomain;
    private ValidationReferencedObject reference;
    private ValidationTransientReferencedObject transientReference;
    
    @Before("@remoteReferenceValidation")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertNotNull("Missing needed delegate!", referenceMaintenanceDelegate);
        MessageManagerInitializationDelegate.cleanupMessageManager();
        MessageTestUtils.assertNoErrorMessages();
    }
    
    @After("@remoteReferenceValidation")
    public void cleanUp() throws Exception {
    	if (localDomain != null) {
    		localDomain.delete();
    	}

    	if (reference != null) {
    	    ValidationReferencedObject persistedReference = referenceMaintenanceDelegate.findByPrimaryKey(reference.getId());
    	    if(persistedReference != null) {
    	        referenceMaintenanceDelegate.delete(reference.getId());
    	    }
    	}
    	
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
        
        referenceMaintenanceDelegate.delete(reference.getId());
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
    
    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\"$")
    public void a_entity(String persistence, String entityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            //local persistent entity with reference to a transient entity
            LocalDomainTransientReferenceBO local=  new LocalDomainTransientReferenceBO();
            local.setName(entityName);
            localTransientReferenceDomain = local;
        }else if ("transient".equalsIgnoreCase(persistence)) {
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(entityName);
            localTransientDomain = localTransient;
        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }
    }

    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" in another domain$")
    public void a_entity_in_another_domain(String persistence, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            ValidationReferencedObject newReference = new ValidationReferencedObject();
            //newReference.setId(UUID.randomUUID());
            newReference.setSomeDataField(remoteEntityName);
            reference = referenceMaintenanceDelegate.create(newReference);
            MessageTestUtils.assertNoErrorMessages();
        }else if ("transient".equalsIgnoreCase(persistence)) {
            ValidationTransientReferencedObject newReference = new ValidationTransientReferencedObject();
            newReference.setName(remoteEntityName);
            transientReference = newReference;
        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }
    }
    
    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to an existing transient entity \"([^\"]*)\"$")
    public void a_entity_has_a_remote_reference_to_an_existing_transient_entity(String persistence,
            String localEntityName, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            // remote transient entity
            ValidationTransientReferencedObject newTransientReference = new ValidationTransientReferencedObject();
            newTransientReference.setName(remoteEntityName);

            // local persistent entity
            LocalDomainTransientReferenceBO local = new LocalDomainTransientReferenceBO();
            local.setName(localEntityName);
            local.setExternalTransientReference(newTransientReference);

            localTransientReferenceDomain = local;

        } else if ("transient".equalsIgnoreCase(persistence)) {

        } else {
            fail("Invalid entity state.  Should be either persistent or transient");
        }
    }

    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to a non-existing transient entity \"([^\"]*)\"$")
    public void a_entity_has_a_remote_reference_to_a_non_existing_transient_entity(String persistence, String localEntityName, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {           
            //local persistent entity
            LocalDomainTransientReferenceBO local=  new LocalDomainTransientReferenceBO();
            local.setName(localEntityName); 
            //external reference does not exist
            local.setExternalTransientReference(null);
            
            localTransientReferenceDomain = local;
        }else if ("transient".equalsIgnoreCase(persistence)) {
            
        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }
    }    
    
    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to an existing persistent entity \"([^\"]*)\"$")
    public void a_entity_has_a_remote_reference_to_an_existing_persistent_entity(String persistence, String localEntityName, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            //maybe move the existing code here
        }else if ("transient".equalsIgnoreCase(persistence)) {
            //remote persistent entity
            ValidationReferencedObject persistentReference = new ValidationReferencedObject();
            persistentReference.setId(UUID.randomUUID());
            persistentReference.setSomeDataField(remoteEntityName);
            reference = referenceMaintenanceDelegate.create(persistentReference);
            
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            localTransient.setExternalReference(reference);
            localTransientDomain = localTransient;
            
            MessageTestUtils.assertNoErrorMessages();

        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }
    }

    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to a non-existing persistent entity \"([^\"]*)\"$")
    public void a_entity_has_a_remote_reference_to_a_non_existing_persistent_entity(String persistence, String localEntityName, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            //maybe move the existing code here
        }else if ("transient".equalsIgnoreCase(persistence)) {
            //remote persistent entity
            //first create the reference
            ValidationReferencedObject newReference = new ValidationReferencedObject();
            newReference.setId(UUID.randomUUID());
            newReference.setSomeDataField(remoteEntityName);
            reference = referenceMaintenanceDelegate.create(newReference);
            MessageTestUtils.assertNoErrorMessages();            
            //then delete it 
            referenceMaintenanceDelegate.delete(reference.getId());
            MessageTestUtils.assertNoErrorMessages();
            
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            //reference non-existing reference
            localTransient.setExternalReference(reference);
            localTransientDomain = localTransient;
        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }     
    }    

    @When("^references are added$")
    public void references_are_added() throws Throwable {
        //nothing to do here
    }
      
    @When("^the reference level validation is performed on the \"([^\"]*)\" entity instance \"([^\"]*)\"$")
    public void the_reference_level_validation_is_performed_on_the_entity_instance(String persistence, String arg2)
            throws Throwable {

        if ("persistent".equalsIgnoreCase(persistence)) {
            localTransientReferenceDomain.validate();
        } else if ("transient".equalsIgnoreCase(persistence)) {
            localTransientDomain.validate();
        } else {
            fail("Invalid entity state.  Should be either persistent or transient");
        }
    }

    @Then("^the reference level validation on transient entity passes$")
    public void the_reference_level_validation_on_transient_entity_passes() throws Throwable {        
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), RemoteReferenceValidationSteps.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());        
    }
    
    @Then("^the reference level validation on persistent entity passes$")
    public void the_reference_level_validation_on_entity_passes() throws Throwable {        
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), RemoteReferenceValidationSteps.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());        

        localTransientReferenceDomain.save();
        LocalDomainTransientReferenceBO persisted = LocalDomainTransientReferenceBO.findByPrimaryKey(localTransientReferenceDomain.getKey());
        assertNotNull("LocalDomainTransientReferenceBO should have been persisted to the DB", persisted);
    }
    
    @Then("^the reference level validation on transient entity fails$")
    public void the_reference_level_validation_on_transient_entity_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
    }
    
    @Then("^the reference level validation on persistent entity fails$")
    public void the_reference_level_validation_on_persistent_entity_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        
        localTransientReferenceDomain.save();
        assertNull("LocalDomainBO should not have been persisted to the DB", localTransientReferenceDomain.getKey());
    }

    @Then("^the \"([^\"]*)\" entity \"([^\"]*)\" can have a reference \"([^\"]*)\"$")
    public void the_entity_can_have_a_reference(String persistence, String entityName, String remoteReference)
            throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            // persistent local entity with transient remote reference
            localTransientReferenceDomain.setExternalTransientReference(transientReference);
        } else if ("transient".equalsIgnoreCase(persistence)) {
            // transient local entity with persistent remote reference
            localTransientDomain.setExternalReference(reference);
        } else {
            fail("Invalid entity state.  Should be either persistent or transient");
        }
    }


    
}

package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.bitbucket.askllc.fermenter.cookbook.domain.ReferenceValidationSteps;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationReferencedObjectMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationTransientReferencedObject;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalTransientDomainBO;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.jboss.arquillian.test.api.ArquillianResource;
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
    
    private LocalDomainBO localDomainBO;
    private LocalTransientDomainBO localTransientDomain;
    private ValidationReferencedObject reference;
    private ValidationTransientReferencedObject transientReference;
    private static final ValidationTransientReferencedObject DEFAULT_TRANSIENT_REFERENCE = new ValidationTransientReferencedObject();
    private boolean errorThrownValidate = false;
    private boolean errorThrownSave = false;
    
    @Before("@remoteReferenceValidation")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
        assertNotNull("Missing needed delegate!", referenceMaintenanceDelegate);
        MessageManagerInitializationDelegate.cleanupMessageManager();
    }
    
    @After("@remoteReferenceValidation")
    public void cleanUp() throws Exception {
        if (localDomainBO != null) {
            localDomainBO.delete();
        }
        if (reference != null) {
            try {
                ValidationReferencedObject persistedReference = referenceMaintenanceDelegate
                        .findByPrimaryKey(reference.getId());
                if (persistedReference != null) {
                    referenceMaintenanceDelegate.delete(reference.getId());
                }
            } catch (WebApplicationException e) {
                //nothing to do
            }
        }
        MessageManagerInitializationDelegate.cleanupMessageManager();
        errorThrownValidate = false;
        errorThrownSave = false;
        
        AuthenticationTestUtils.logout();
        
    }
    
    @Given("^the \"([^\"]*)\" has a remote reference to an existing \"([^\"]*)\"$")
    public void the_has_a_remote_reference_to_an_existing(String address, String state) throws Throwable {
        // remote persistent entity reference
        reference = createRemotePersistentReference(state);
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName(address);
        local.setExternalReference(reference);
        //transient reference is required in LocalDomain entity
        local.setExternalTransientReference(DEFAULT_TRANSIENT_REFERENCE);
        localDomainBO = local;
    }
    
    
    @Given("^the \"([^\"]*)\" has a remote reference to a non-existing \"([^\"]*)\"$")
    public void the_has_a_remote_reference_to_a_non_existing(String address, String state) throws Throwable {
        // remote persistent entity reference
        reference = createRemotePersistentReference(state);
        
        referenceMaintenanceDelegate.delete(reference.getId());
        MessageTestUtils.assertNoErrorMessages();
        
        LocalDomainBO local = new LocalDomainBO();
        local.setName(address);
        local.setExternalReference(reference);
        //transient reference is required in LocalDomain entity
        local.setExternalTransientReference(DEFAULT_TRANSIENT_REFERENCE);
        localDomainBO = local;
    }
    
    
    @When("^the reference level validation is performed on the instance \"([^\"]*)\"$")
    public void the_reference_level_validation_is_performed_on_the_instance(String arg1) throws Throwable {
        try {
            localDomainBO.validate();
        } catch (WebApplicationException e) {
            errorThrownValidate = Boolean.TRUE;
        }
    }

    @Then("^the reference level validation passes$")
    public void the_reference_level_validation_passes() throws Throwable {
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), RemoteReferenceValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
		
		localDomainBO.save();
		LocalDomainBO persisted = LocalDomainBO.findByPrimaryKey(localDomainBO.getKey());
		assertNotNull("LocalDomainBO should have been persisted to the DB", persisted);
    }


    @Then("^the reference level validation fails$")
    public void the_reference_level_validation_fails() throws Throwable {
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
		assertTrue("Should have encountered an error!", errorThrownValidate);
		
        try {
            localDomainBO = localDomainBO.save();
        } catch (WebApplicationException e) {
            errorThrownSave = true;
        }
		assertTrue("Should have encountered an error!", errorThrownSave);
    }
    
    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\"$")
    public void a_entity(String persistence, String entityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            LocalDomainBO local=  new LocalDomainBO();
            local.setName(entityName);
            localDomainBO = local;
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
            // remote persistent entity reference
            reference = createRemotePersistentReference(remoteEntityName);
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
            
            // remote persistent entity reference
            // not needed for this test but it is required on the localDomain entity
            reference = createRemotePersistentReference(remoteEntityName);

            // local persistent entity
            LocalDomainBO local = new LocalDomainBO();
            local.setName(localEntityName);
            // set reference to remote transient entity
            local.setExternalTransientReference(newTransientReference);
            local.setExternalReference(reference);
            localDomainBO = local;

        } else if ("transient".equalsIgnoreCase(persistence)) {
            // remote transient entity
            ValidationTransientReferencedObject remoteTransientReference = new ValidationTransientReferencedObject();
            remoteTransientReference.setName(remoteEntityName);
            
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            //set remote transient reference
            localTransient.setExternalTransientReference(remoteTransientReference);
            localTransientDomain = localTransient;

        } else {
            fail("Invalid entity state.  Should be either persistent or transient");
        }
    }

    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to a non-existing transient entity$")
    public void a_entity_has_a_remote_reference_to_a_non_existing_transient_entity(String persistence, String localEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {           
            //local persistent entity
            LocalDomainBO local =  new LocalDomainBO();
            local.setName(localEntityName); 
            //remote transient reference does not exist
            local.setExternalTransientReference(null);
            localDomainBO = local;
        }else if ("transient".equalsIgnoreCase(persistence)) { 
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            //remote transient reference does not exist 
            localTransient.setExternalTransientReference(null);
            localTransientDomain = localTransient;          
        }else{
            fail ("Invalid entity state.  Should be either persistent or transient");
        }
    }    
    
    @Given("^a \"([^\"]*)\" entity \"([^\"]*)\" has a remote reference to an existing persistent entity \"([^\"]*)\"$")
    public void a_entity_has_a_remote_reference_to_an_existing_persistent_entity(String persistence, String localEntityName, String remoteEntityName) throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            //maybe move the existing code here
        }else if ("transient".equalsIgnoreCase(persistence)) {
            //remote persistent entity reference
            reference = createRemotePersistentReference(remoteEntityName);
            
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            //persistent reference
            localTransient.setExternalReference(reference);
            //transient reference is required
            localTransient.setExternalTransientReference(DEFAULT_TRANSIENT_REFERENCE);
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
            //remote persistent entity reference
            //first create the reference
            reference = createRemotePersistentReference(remoteEntityName);            
            //then delete it 
            referenceMaintenanceDelegate.delete(reference.getId());
            MessageTestUtils.assertNoErrorMessages();
            
            //local transient entity
            LocalTransientDomainBO localTransient = new LocalTransientDomainBO();
            localTransient.setName(localEntityName);
            //set non-existing persistent reference
            localTransient.setExternalReference(reference);
            //set transient reference since it is required although not a part of this test
            localTransient.setExternalTransientReference(DEFAULT_TRANSIENT_REFERENCE);
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
            try {
                localDomainBO.validate();
            } catch (WebApplicationException e) {
                errorThrownValidate = true;
            }
        } else if ("transient".equalsIgnoreCase(persistence)) {
            try {
                localTransientDomain.validate();
            } catch (WebApplicationException e) {
                errorThrownValidate = true;
            }
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

        localDomainBO.save();
        LocalDomainBO persisted = LocalDomainBO.findByPrimaryKey(localDomainBO.getKey());
        assertNotNull("LocalDomainBO should have been persisted to the DB", persisted);
    }
    
    @Then("^the reference level validation on transient entity fails$")
    public void the_reference_level_validation_on_transient_entity_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        if(!errorThrownValidate) {
            assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        }
        
    }
    
    @Then("^the reference level validation on persistent entity fails$")
    public void the_reference_level_validation_on_persistent_entity_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        
        localDomainBO.save();
        assertNull("LocalDomainBO should not have been persisted to the DB", localDomainBO.getKey());
    }

    @Then("^the \"([^\"]*)\" entity \"([^\"]*)\" can have a reference \"([^\"]*)\"$")
    public void the_entity_can_have_a_reference(String persistence, String entityName, String remoteReference)
            throws Throwable {
        if ("persistent".equalsIgnoreCase(persistence)) {
            // persistent local entity with transient remote reference
            localDomainBO.setExternalTransientReference(transientReference);
        } else if ("transient".equalsIgnoreCase(persistence)) {
            // transient local entity with persistent remote reference
            localTransientDomain.setExternalReference(reference);
        } else {
            fail("Invalid entity state.  Should be either persistent or transient");
        }
    }

    private ValidationReferencedObject createRemotePersistentReference(String remoteEntityValue) {
        ValidationReferencedObject referenceObject = new ValidationReferencedObject();
        referenceObject.setId(UUID.randomUUID());
        referenceObject.setSomeDataField(remoteEntityValue);
        ValidationReferencedObject newReference = referenceMaintenanceDelegate.create(referenceObject);
        MessageTestUtils.assertNoErrorMessages();   
        return newReference;
    }
    
}

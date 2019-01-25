package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.LocalTransientReferenceExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferenceExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationTransientReferencedObjectBO;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml",
"classpath:h2-spring-ds-context.xml" })
@Transactional
public class ReferenceValidationSteps {
    private ValidationReferencedObjectBO object;
    private ValidationReferenceExampleBO example;
    private ValidationTransientReferencedObjectBO transientEntity;
    private LocalTransientReferenceExampleBO transientReferenceExample;

    @After("@localReferenceValidation")
    public void cleanUp() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        ValidationReferenceExampleBO.deleteAllValidationExamples();
        ValidationReferencedObjectBO.deleteAllValidationExamples();
    }

    @Given("^the \"([^\"]*)\" has a local reference to an existing \"([^\"]*)\"$")
    public void the_has_a_local_reference_to_an_existing(String address, String state) throws Throwable {
        object = new ValidationReferencedObjectBO();
        object.setSomeDataField(state);
        object.save();

        example = new ValidationReferenceExampleBO();
        example.setSomeDataField(address);
        example.setRequiredReference(object);
    }

    @Given("^the \"([^\"]*)\" has a local reference to a non-existing \"([^\"]*)\"$")
    public void the_has_a_local_reference_to_a_non_existing(String address, String state) throws Throwable {
        object = new ValidationReferencedObjectBO();
        object.setSomeDataField(state);
        object.save();
        object.delete();

        example = new ValidationReferenceExampleBO();
        example.setSomeDataField(address);
        example.setRequiredReference(object);
    }

    @When("^the reference level validation is performed on the value \"([^\"]*)\"$")
    public void the_reference_level_validation_is_performed_on_the_value(String address) throws Throwable {
        example.validate();
    }

    @Then("^the reference level validation passes$")
    public void the_reference_level_validation_passes() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

        example.save();
        ValidationReferenceExampleBO persisted = ValidationReferenceExampleBO.findByPrimaryKey(example.getKey());
        assertNotNull("ValidationReferenceExampleBO should have been persisted to the DB", persisted);
    }

    @Then("^the reference level validation fails$")
    public void the_reference_level_validation_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());

        example.save();
        assertNull("ValidationReferenceExampleBO should not have been persisted to the DB", example.getKey());
    }

    @Given("^a persistent entity \"([^\"]*)\" has a local reference to an existing transient entity \"([^\"]*)\"$")
    public void a_persistent_entity_has_a_local_reference_to_an_existing_transient_entity(String persistentEntity,
            String transientEntityName) throws Throwable {
        // transient entity
        transientEntity = new ValidationTransientReferencedObjectBO();
        transientEntity.setName(transientEntityName);

        // persistent entity with reference to transient entity
        transientReferenceExample = new LocalTransientReferenceExampleBO();
        transientReferenceExample.setSomeDataField(persistentEntity);
        transientReferenceExample.setTransientReference(transientEntity);
    }

    @When("^the reference level validation is performed on the persistent entity instance \"([^\"]*)\"$")
    public void the_reference_level_validation_is_performed_on_the_persistent_entity_instance(String arg1)
            throws Throwable {
        transientReferenceExample.validate();
    }

    @Then("^the reference level validation on persistent entity passes$")
    public void the_reference_level_validation_on_persistent_entity_passes() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

        transientReferenceExample.save();
        LocalTransientReferenceExampleBO persisted = LocalTransientReferenceExampleBO
                .findByPrimaryKey(transientReferenceExample.getKey());
        assertNotNull("LocalTransientReferenceExampleBO should have been persisted to the DB", persisted);
    }

    @Given("^a persistent entity \"([^\"]*)\" has a local reference to a non-existing transient entity \"([^\"]*)\"$")
    public void a_persistent_entity_has_a_local_reference_to_a_non_existing_transient_entity(String persistentEntity,
            String transientEntityName) throws Throwable {
        // persistent entity with reference to null transient entity
        transientReferenceExample = new LocalTransientReferenceExampleBO();
        transientReferenceExample.setSomeDataField(persistentEntity);
        transientReferenceExample.setTransientReference(null);
    }

    @Then("^the reference level validation on persistent entity fails$")
    public void the_reference_level_validation_on_persistent_entity_fails() throws Throwable {
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ReferenceValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());

        transientReferenceExample.save();
        assertNull("LocalTransientReferenceExampleBO should not have been persisted to the DB",
                transientReferenceExample.getKey());
    }

}

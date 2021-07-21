package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientFieldExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientSubEntityExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class TransientFieldSteps {
    private String userString;
    private TransientFieldExampleBO transientFieldExampleBO;
    private TransientSubEntityExampleBO transientSubEntityExampleBO;
    private static final int DEFAULT_VALUE = 3;
    private int modifiedValue;
    private int actualValue;

    @After("@transientField")
    public void cleanupMsgMgr() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        TransientFieldExampleBO.deleteAllTransientFieldExamples();
        ValidationReferencedObjectBO.deleteAllValidationExamples();
        transientFieldExampleBO = null;
        transientSubEntityExampleBO = null;
    }

    @Given("^a field is marked as transient and I assign \"([^\"]*)\" to the field value$")
    public void a_field_is_marked_as_transient_and_I_assign_to_the_field_value(String value) throws Throwable {
        this.userString = value;

        transientFieldExampleBO = new TransientFieldExampleBO();
        transientFieldExampleBO.setTransientField(this.userString);
    }

    @When("^the entity is saved and retrieved$")
    public void the_entity_is_saved_and_retrieved() throws Throwable {
        transientFieldExampleBO = transientFieldExampleBO.save();
    }

    @Then("^the only fields with values are the ones not marked as transient$")
    public void the_only_fields_with_values_are_the_ones_not_marked_as_transient() throws Throwable {
        TransientFieldExampleBO retrievedTransientFieldObj = TransientFieldExampleBO.findByPrimaryKey(transientFieldExampleBO.getKey());
        assertEquals("Persisted field value did not match input", transientFieldExampleBO.getBeerType(), retrievedTransientFieldObj.getBeerType());
        assertNotEquals("Transient field should not be persisted", transientFieldExampleBO.getTransientField(), retrievedTransientFieldObj.getTransientField());
    }

    @Given("^a persistent entity has a transient field with a default value$")
    public void a_persistent_entity_has_a_transient_field_with_a_default_value() throws Throwable {
        transientFieldExampleBO = new TransientFieldExampleBO();
        transientFieldExampleBO = transientFieldExampleBO.save();
    }

    @Given("^a transient entity has a field with a default value$")
    public void a_transient_entity_has_a_field_with_a_default_value() throws Throwable {
        transientSubEntityExampleBO = new TransientSubEntityExampleBO();
        ValidationReferencedObjectBO referenceObject = new ValidationReferencedObjectBO();
        transientSubEntityExampleBO.setRequiredReference(referenceObject.save());
    }

    @Given("^the value of the field has been modified$")
    public void the_value_of_the_field_has_been_modified() throws Throwable {
        modifiedValue = DEFAULT_VALUE + 1;
        if (transientFieldExampleBO != null) {
            transientFieldExampleBO.setTransientFieldDefaultValue(modifiedValue);
        } else if (transientSubEntityExampleBO != null) {
            transientSubEntityExampleBO.setDefaultValueField(modifiedValue);
        }
    }

    @When("^the entity is retrieved$")
    public void the_entity_is_retrieved() throws Throwable {
        if (transientFieldExampleBO != null) {
            actualValue = transientFieldExampleBO.getTransientFieldDefaultValue();
        } else if (transientSubEntityExampleBO != null) {
            actualValue = transientSubEntityExampleBO.getDefaultValueField();
        }
    }

    @Then("^the value of the field is equal to the default$")
    public void the_value_of_the_field_is_equal_to_the_default() throws Throwable {
        assertEquals("Returned value did not equal the default!", DEFAULT_VALUE, actualValue);
    }

    @Then("^the field has the new value$")
    public void the_field_has_the_new_value() throws Throwable {
        assertNotEquals("Returned value equaled the default when it should not!", DEFAULT_VALUE, actualValue);
        assertEquals("Returned value did not match the modified value!", modifiedValue, actualValue);
    }

}

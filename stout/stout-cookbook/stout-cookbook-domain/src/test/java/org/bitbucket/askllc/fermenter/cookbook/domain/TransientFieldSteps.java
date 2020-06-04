package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientFieldExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;
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

    @After("@fieldValidation")
    public void cleanupMsgMgr() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        TransientFieldExampleBO.deleteAllTransientFieldExamples();
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


}

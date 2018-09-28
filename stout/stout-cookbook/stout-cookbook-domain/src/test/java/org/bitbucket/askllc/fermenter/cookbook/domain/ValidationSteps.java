package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;

import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
		"classpath:h2-spring-ds-context.xml" })
@Transactional
public class ValidationSteps {

	private String userString;
	private Long userLong;
	private ValidationExampleBO example;

	@After("@fieldValidation")
	public void cleanupMsgMgr() throws Exception {
		MessageManagerInitializationDelegate.cleanupMessageManager();
		ValidationExampleBO.deleteAllValidationExamples();
	}

	@Given("^a \"([^\"]*)\" to validate against the validation example string example field$")
	public void a_to_validate_against_the_validation_example_string_example_field(String value) throws Throwable {
		this.userString = value;

		example = new ValidationExampleBO();
		example.setStringExample(userString);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that value$")
	public void field_level_validation_is_performed_on_that_value() throws Throwable {

		example.validate();

	}

	@Then("^the validation returns no errors$")
	public void the_validation_returns_no_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				org.bitbucket.fermenter.stout.messages.Messages.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Then("^the validation returns errors$")
	public void the_validation_returns_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				org.bitbucket.fermenter.stout.messages.Messages.class);
		assertFalse("Should have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Given("^a (\\d+) to validate against the validation example long example field$")
	public void a_to_validate_against_the_validation_example_long_example_field(long value) throws Throwable {
		this.userLong = value;
		
		example = new ValidationExampleBO();
		example.setLongExample(userLong);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that long value$")
	public void field_level_validation_is_performed_on_that_long_value() throws Throwable {

		example.validate();

	}

	@Then("^the long validation returns no errors$")
	public void the_long_validation_returns_no_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				ValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Then("^the long validation returns errors$")
	public void the_long_validation_returns_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				ValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());


	}

	@Given("^a negative -(\\d+) to validate against the validation example long example field$")
	public void a_negative_to_validate_against_the_validation_example_long_example_field(long value) throws Throwable {
		this.userLong = value;

		example = new ValidationExampleBO();
		example.setLongExample(userLong);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

}

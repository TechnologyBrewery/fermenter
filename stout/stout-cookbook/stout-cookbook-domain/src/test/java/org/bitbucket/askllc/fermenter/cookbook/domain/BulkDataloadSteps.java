package org.bitbucket.askllc.fermenter.cookbook.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml",
		"classpath:h2-spring-ds-context.xml" })
@Transactional
public class BulkDataloadSteps {
	@Inject
	private ValidationExampleMaintenanceService validationExampleMaintenanceService;

	private Collection<ValidationExampleBO> allValidExamples = new ArrayList<>();
	private Collection<ValidationExampleBO> mixedExamples = new ArrayList<>();

	@Before("@bulkDataload")
	public void setUp() {
		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@After("@bulkDataload")
	public void cleanupMsgMgr() throws Exception {
		MessageManagerInitializationDelegate.cleanupMessageManager();
		ValidationExampleBO.deleteAllValidationExamples();
	}

	@Given("^the following valid data exists$")
	public void the_following_valid_data_exists(List<String> exampleFields) throws Throwable {
		for (String field : exampleFields) {
			ValidationExampleBO example = new ValidationExampleBO();
			example.setRequiredField(field);

			allValidExamples.add(example);
		}
	}

	@Given("^the following valid and invalid data exists$")
	public void the_following_valid_and_invalid_data_exists(List<String> exampleFields) throws Throwable {
		for (String field : exampleFields) {
			ValidationExampleBO example = new ValidationExampleBO();

			if (field.contains("good")) {
				example.setRequiredField(field);
			}

			mixedExamples.add(example);
		}
	}

	@When("^the valid data is sent over in bulk to be created$")
	public void the_valid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
		validationExampleMaintenanceService.bulkSaveOrUpdate(allValidExamples);
	}

	@Then("^each data value is created and saved$")
	public void each_data_value_is_created_and_saved() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^the valid data is sent over in bulk to be updated$")
	public void the_valid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^each data value is updated and saved$")
	public void each_data_value_is_updated_and_saved() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^the valid data is sent over to be deleted$")
	public void the_valid_data_is_sent_over_to_be_deleted() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^each data value is deleted$")
	public void each_data_value_is_deleted() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^the valid and invalid data is sent over in bulk to be created$")
	public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^each data value is not saved and an error is thrown$")
	public void each_data_value_is_not_saved_and_an_error_is_thrown() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^the valid and invalid data is sent over in bulk to be updated$")
	public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^each data value is not updated and an error is thrown$")
	public void each_data_value_is_not_updated_and_an_error_is_thrown() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^the valid and invalid data is sent over in bulk to be deleted$")
	public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^each data value is not deleted and an error is thrown$")
	public void each_data_value_is_not_deleted_and_an_error_is_thrown() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

}

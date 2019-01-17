package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class BulkDataloadSteps {
    @Inject
    private ValidationExampleMaintenanceService validationExampleMaintenanceService;

    private Collection<ValidationExampleBO> allValidExamples = new ArrayList<>();
    private Collection<ValidationExampleBO> allMixedExamples = new ArrayList<>();
    private Collection<ValidationExampleBO> existingExamples = new ArrayList<>();
    private Collection<ValidationExampleBO> emptyExamples = new ArrayList<>();
    private ValueServiceResponse valueServiceResponse = new ValueServiceResponse();
    private VoidServiceResponse voidServiceResponse = new VoidServiceResponse();
    private Boolean errorCaught = false;

    @Before("@bulkDataload")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After("@bulkDataload")
    public void cleanupMsgMgr() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        ValidationExampleBO.deleteAllValidationExamples();
        valueServiceResponse = null;
        voidServiceResponse = null;
        allValidExamples = null;
        allMixedExamples = null;
    }

    @Given("^the following valid data$")
    public void the_following_valid_data(List<String> fields) throws Throwable {
        for (String field : fields) {
            ValidationExampleBO example = new ValidationExampleBO();
            example.setRequiredField(field);
            allValidExamples.add(example);
        }
    }

    @Given("^the following valid and invalid data$")
    public void the_following_valid_and_invalid_data(List<String> fields) throws Throwable {
        for (String field : fields) {
            ValidationExampleBO example = new ValidationExampleBO();
            if (field.contains("good")) {
                example.setRequiredField(field);
            }
            allMixedExamples.add(example);
        }
    }

    @When("^the valid data is sent over in bulk to be created$")
    public void the_valid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
        valueServiceResponse = validationExampleMaintenanceService.bulkSaveOrUpdate(allValidExamples);
    }

    @Then("^each data value is created and saved$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void each_data_value_is_created_and_saved() throws Throwable {
        checkSaveSuccess(allValidExamples);
    }

    @When("^the valid data is sent over in bulk to be updated$")
    public void the_valid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        Collection<ValidationExampleBO> savedRecords = (Collection<ValidationExampleBO>) valueServiceResponse
                .getValue();
        for (ValidationExampleBO validExample : savedRecords) {
            validExample.setRequiredField("some updated string");
        }
        saveOrUpdateRecords(savedRecords);
    }

    @Then("^each data value is updated and saved$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void each_data_value_is_updated_and_saved() throws Throwable {
        checkSaveSuccess(allValidExamples);
        Collection<ValidationExampleBO> updatedExamples = (Collection<ValidationExampleBO>) valueServiceResponse
                .getValue();
        for (ValidationExampleBO validExample : updatedExamples) {
            assertEquals("the valid data was not actually updated", "some updated string",
                    validExample.getRequiredField());
        }
    }

    @When("^the valid data is sent over to be deleted$")
    public void the_valid_data_is_sent_over_to_be_deleted() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        voidServiceResponse = validationExampleMaintenanceService
                .bulkDelete((Collection<ValidationExampleBO>) valueServiceResponse.getValue());
    }

    @Then("^each data value is deleted$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void each_data_value_is_deleted() throws Throwable {
        for (ValidationExampleBO value : (Collection<ValidationExampleBO>) valueServiceResponse.getValue()) {
            ValidationExampleBO persisted = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNull("Should not have found any ValidationExampleBO records", persisted);
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be created$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
        try {
            valueServiceResponse = validationExampleMaintenanceService.bulkSaveOrUpdate(allMixedExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            valueServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
        }
    }

    @Then("^each data value is not saved and an error is thrown$")
    public void each_data_value_is_not_saved_and_an_error_is_thrown() throws Throwable {
        assertTrue("Response should come with error messages, but it didnt.",
                valueServiceResponse.getMessages().hasErrorMessages());

        List<ValidationExampleBO> examples = ValidationExampleBO.grabAllWithRequiredField();
        assertEquals("No ValidationExampleBO record should have been persisted", examples.size(), 0);
    }

    @Given("^valid data already exists in the system$")
    public void valid_data_already_exists_in_the_system() throws Throwable {
        for (int i = 0; i < 8; i++) {
            ValidationExampleBO validationExample = new ValidationExampleBO();
            validationExample.setRequiredField(RandomStringUtils.random(7));
            validationExample.setKey(UUID.randomUUID());

            ValidationExampleBO persisted = validationExample.save();
            existingExamples.add(persisted);
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be updated$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        for (ValidationExampleBO test : existingExamples) {
            test.setRequiredField(null);
        }

        try {
            valueServiceResponse = validationExampleMaintenanceService.bulkSaveOrUpdate(existingExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            valueServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
        }
    }

    @Then("^each data value is not updated and an error is thrown$")
    public void each_data_value_is_not_updated_and_an_error_is_thrown() throws Throwable {
        assertTrue("Response should come with error messages, but it didnt.",
                valueServiceResponse.getMessages().hasErrorMessages());

        List<ValidationExampleBO> examples = ValidationExampleBO.grabAllWithRequiredField();
        assertEquals("No ValidationExampleBO record should have been updated have null required field", 8,
                examples.size());
    }

    @When("^the valid and invalid data is sent over in bulk to be deleted$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        for (ValidationExampleBO entity : existingExamples) {
            entity.setKey(UUID.randomUUID());
        }
        try {
            voidServiceResponse = validationExampleMaintenanceService.bulkDelete(existingExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            voidServiceResponse = (VoidServiceResponse) e.getResponse().getEntity();
        }
    }

    @Then("^each data value is not deleted and an error is thrown$")
    public void each_data_value_is_not_deleted_and_an_error_is_thrown() throws Throwable {
        assertTrue("Response should come with error messages, but it didnt.",
                voidServiceResponse.getMessages().hasErrorMessages());

        List<ValidationExampleBO> examples = ValidationExampleBO.getAllValidationExamples();
        assertEquals("No ValidationExampleBO record should have been deleted", 8, examples.size());
    }

    @When("^the empty data is sent over in bulk to be created$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_empty_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
        try {
            validationExampleMaintenanceService.bulkSaveOrUpdate(emptyExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorCaught = Boolean.TRUE;
        }
    }

    @Then("^a HTTP (\\d+) error is returned$")
    public void a_HTTP_error_is_returned(int arg1) throws Throwable {
        assertTrue("an error was not thrown when attempting to pass in empty data", errorCaught);
    }

    @When("^the empty data is sent over in bulk to be updated$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_empty_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        try {
            validationExampleMaintenanceService.bulkSaveOrUpdate(emptyExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorCaught = Boolean.TRUE;
        }
    }

    @When("^the empty data is sent over in bulk to be deleted$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_empty_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        try {
            validationExampleMaintenanceService.bulkDelete(emptyExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorCaught = Boolean.TRUE;
        }
    }

    private void saveOrUpdateRecords(Collection<ValidationExampleBO> examples) {
        valueServiceResponse = validationExampleMaintenanceService.bulkSaveOrUpdate(examples);
    }

    private void checkSaveSuccess(Collection<ValidationExampleBO> examples) {
        Collection<ValidationExampleBO> entities = (Collection<ValidationExampleBO>) valueServiceResponse.getValue();
        assertEquals("the valid bulk set of data did not save properly", entities.size(), examples.size());

        for (ValidationExampleBO value : entities) {
            ValidationExampleBO persisted = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNotNull("Should have found ValidationExampleBO record for " + value.getKey(), persisted);
        }
    }
}

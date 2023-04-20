package org.technologybrewery.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.RandomStringUtils;
import org.technologybrewery.fermenter.cookbook.domain.client.service.ValidationExampleMaintenanceDelegate;
import org.technologybrewery.fermenter.cookbook.domain.transfer.ValidationExample;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.technologybrewery.fermenter.stout.messages.Messages;
import org.technologybrewery.fermenter.stout.service.ServiceResponse;
import org.technologybrewery.fermenter.stout.service.ValueServiceResponse;
import org.technologybrewery.fermenter.stout.test.MessageTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class ClientBulkDataloadSteps {

    private Collection<ValidationExample> allValidExamples = new ArrayList<>();
    private Collection<ValidationExample> allMixedExamples = new ArrayList<>();
    private Collection<ValidationExample> savedExamples = new ArrayList<>();
    private Collection<ValidationExample> emptyExamples = new ArrayList<>();
    private Collection<ValidationExample> invalidExamplesForErrorValidation = new ArrayList<>();
    private boolean bulkDeleteSuccess = false;
    private boolean bulkCreateOrUpdateSuccess = false;
    private boolean errorCaught = false;
    private ServiceResponse errorServiceResponse;

    private ValidationExample exampleTest = new ValidationExample();

    @Inject
    private ValidationExampleMaintenanceDelegate validationExampleMaintenanceDelegate;

    @Before("@clientBulkDataload")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
        assertNotNull("Missing needed maitenance manager delegate!", validationExampleMaintenanceDelegate);

        MessageManagerInitializationDelegate.initializeMessageManager();
    }

    @After("@clientBulkDataload")
    public void cleanupMsgMgr() throws Exception {
        if (!savedExamples.isEmpty()) {
            validationExampleMaintenanceDelegate.bulkDelete(savedExamples);
            savedExamples.clear();
        }

        allValidExamples.clear();
        allMixedExamples.clear();
        invalidExamplesForErrorValidation.clear();
        MessageManagerInitializationDelegate.cleanupMessageManager();
        
        AuthenticationTestUtils.logout();
    }

    @Given("^the following valid data$")
    public void the_following_valid_data(List<String> fields) throws Throwable {
        for (String field : fields) {
            ValidationExample example = new ValidationExample();
            example.setRequiredField(field);
            allValidExamples.add(example);
        }
    }

    @Given("^the following valid and invalid data$")
    public void the_following_valid_and_invalid_data(List<String> fields) throws Throwable {
        for (String field : fields) {
            ValidationExample example = new ValidationExample();
            if (field.contains("good")) {
                example.setRequiredField(field);
            }
            allMixedExamples.add(example);
        }
    }

    @Given("^a collection of valid data items$")
    public void a_collection_of_valid_data_items() throws Throwable {
        assertNotNull(allValidExamples);
        assertTrue("Valid data items not found", allValidExamples.size() > 0);
    }

    @Given("^a collection of invalid data items$")
    public void a_collection_of_invalid_data_items() throws Throwable {
        assertNotNull(allMixedExamples);
        assertTrue("Invalid data items not found", allMixedExamples.size() > 0);
    }

    @Given("^valid data already exists in the system$")
    public void valid_data_already_exists_in_the_system() throws Throwable {
        for (int i = 0; i < 8; i++) {
            ValidationExample validationExample = new ValidationExample();
            validationExample.setRequiredField(RandomStringUtils.random(7));
            validationExample.setId(UUID.randomUUID());

            ValidationExample persisted = validationExampleMaintenanceDelegate.create(validationExample);
            savedExamples.add(persisted);
        }
    }

    @Given("^three objects are created with valid fields$")
    public void three_objects_are_created_with_valid_fields() throws Throwable {
        for (int i = 0; i < 3; i++) {
            ValidationExample object = new ValidationExample();
            object.setRequiredField("value");
            exampleTest = validationExampleMaintenanceDelegate.create(object);
            savedExamples.add(exampleTest);
        }
    }

    @Given("^an object created with valid fields$")
    public void an_object_created_with_valid_fields() throws Throwable {
        exampleTest.setRequiredField("value");
        exampleTest = validationExampleMaintenanceDelegate.create(exampleTest);
    }

    @When("^the collection is sent over in bulk to be created$")
    public void the_collection_is_sent_over_in_bulk_to_be_created() throws Throwable {
        savedExamples = validationExampleMaintenanceDelegate.bulkSaveOrUpdate(allValidExamples);
    }

    @When("^the collection is sent over in bulk to be updated$")
    public void the_collection_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        for (ValidationExample validExample : savedExamples) {
            validExample.setRequiredField("some updated string");
        }
        saveOrUpdateRecords(savedExamples);
    }

    @When("^the collection is sent over in bulk to be deleted$")
    public void the_collection_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        validationExampleMaintenanceDelegate.bulkDelete(savedExamples);

    }

    @When("^the valid and invalid data is sent over in bulk to be created$")
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {

        try {
            savedExamples = validationExampleMaintenanceDelegate.bulkSaveOrUpdate(allMixedExamples);
        } catch (WebApplicationException e) {
            bulkCreateOrUpdateSuccess = false;
            assertEquals("Invalid error status", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be updated$")
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        for (ValidationExample test : savedExamples) {
            test.setRequiredField(null);
        }

        try {
            validationExampleMaintenanceDelegate.bulkSaveOrUpdate(savedExamples);
        } catch (WebApplicationException e) {
            bulkCreateOrUpdateSuccess = false;
            assertEquals("Invalid error status", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
        }
    }

    @When("^the empty data is sent over in bulk to be created$")
    public void the_empty_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
        try {
            validationExampleMaintenanceDelegate.bulkSaveOrUpdate(emptyExamples);
        } catch (WebApplicationException e) {
            errorCaught = true;
        }
    }

    @When("^two objects are bulk updated with an invalid field$")
    public void two_objects_are_bulk_updated_with_an_invalid_field() throws Throwable {

        for (ValidationExample persisted: savedExamples) {
            ValidationExample copy = new ValidationExample();
            copy.setId(persisted.getId());
            copy.setRequiredField(null);
            invalidExamplesForErrorValidation.add(copy);
        }
        try {
            validationExampleMaintenanceDelegate.bulkSaveOrUpdate(invalidExamplesForErrorValidation);
        } catch (WebApplicationException e) {
            errorCaught = true;
            assertEquals("Invalid error status", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorServiceResponse = ((ValueServiceResponse) e.getResponse().getEntity());
        }
    }

    @When("^the object is bulk updated with an invalid field$")
    public void the_object_is_bulk_updated_with_an_invalid_field() throws Throwable {
        exampleTest.setRequiredField(null);
        Collection<ValidationExample> updateList = new ArrayList<>();
        updateList.add(exampleTest);
        try {
            validationExampleMaintenanceDelegate.bulkSaveOrUpdate(updateList);
        } catch (WebApplicationException e) {
            errorCaught = true;
            assertEquals("Invalid error status", Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            errorServiceResponse = ((ValueServiceResponse) e.getResponse().getEntity());
        }
    }

    @Then("^each data value is successfully validated and created$")
    public void each_data_value_is_successfully_validated_and_persisted() throws Throwable {

        checkSaveSuccess(savedExamples);
    }

    @Then("^each data value is successfully validated and updated$")
    public void each_data_value_is_successfully_validated_and_updated() throws Throwable {

        checkSaveSuccess(savedExamples);

        for (ValidationExample validExample : savedExamples) {
            assertEquals("the valid data was not actually updated", "some updated string",
                    validExample.getRequiredField());
        }
    }

    @When("^each data value is successfully deleted$")
    public void each_data_value_is_successfully_deleted() throws Throwable {
        ValidationExample deleted = null;
        for (ValidationExample value : savedExamples) {
            try {
                deleted = validationExampleMaintenanceDelegate.findByPrimaryKey(value.getId());
            } catch (WebApplicationException e) {
                bulkDeleteSuccess = true;
            }
            if (deleted != null) {
                bulkDeleteSuccess = false;
            }
        }
        assertTrue("Bulk delete is not successful", bulkDeleteSuccess);
        savedExamples.clear();
    }

    @Then("^each data value is not saved and an error is thrown$")
    public void each_data_value_is_not_saved_and_an_error_is_thrown() throws Throwable {
        assertFalse("Bulk create or update did not fail for invalid data", bulkCreateOrUpdateSuccess);
        assertTrue("Response should come with error messages, but it didnt.",
                errorServiceResponse.getMessages().hasErrors());
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ClientBulkDataloadSteps.class);

    }

    @Then("^each data value is not updated and an error is thrown$")
    public void each_data_value_is_not_updated_and_an_error_is_thrown() throws Throwable {
        assertFalse("Bulk create or update did not fail for invalid data", bulkCreateOrUpdateSuccess);
        assertTrue("Response should come with error messages, but it didnt.",
                errorServiceResponse.getMessages().hasErrors());

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ClientBulkDataloadSteps.class);
    }

    @Then("^a HTTP (\\d+) error is returned$")
    public void a_HTTP_error_is_returned(int arg1) throws Throwable {
        assertTrue("An error was not thrown when attempting to pass in empty data", errorCaught);
    }

    @Then("^a message is created with the object's primary key$")
    public void a_message_is_created_with_the_object_s_primary_key() throws Throwable {
        assertTrue("An error was not thrown when attempting to pass invalid data", errorCaught);
        Collection<Message> errors = getErrorMessages(errorServiceResponse);
        for (Message error : errors) {
            String errorText = error.getDisplayText();
            assertTrue("Bulk error did not contain the primary key: " + exampleTest.getId(),
                    errorText.contains(exampleTest.getId().toString()));
        }
    }

    @Then("^a message is created with the objects' primary keys$")
    public void a_message_is_created_with_the_objects_primary_keys() throws Throwable {
        assertTrue("An error was not thrown when attempting to pass invalid data", errorCaught);
        Collection<Message> errors = getErrorMessages(errorServiceResponse);
        for (Message error : errors) {
            String errorText = error.getDisplayText();
            for (ValidationExample object : invalidExamplesForErrorValidation) {
                if (object.getRequiredField() == null) {
                    assertTrue("Bulk update error did not contain the primary key: " + object.getId(),
                            errorText.contains(object.getId() + ""));
                }
            }
        }
    }

    private void saveOrUpdateRecords(Collection<ValidationExample> examples) {
        savedExamples = validationExampleMaintenanceDelegate.bulkSaveOrUpdate(examples);
    }

    private void checkSaveSuccess(Collection<ValidationExample> examples) {
        MessageTestUtils.assertNoErrorMessages();

        for (ValidationExample value : savedExamples) {
            ValidationExample persisted = validationExampleMaintenanceDelegate.findByPrimaryKey(value.getId());
            assertNotNull("Should  have found any ValidationExample records", persisted);
        }
    }

    private Collection<Message> getErrorMessages(ServiceResponse serviceResponse) {
        Messages messages = new Messages();
        if (serviceResponse.getMessages().getErrorCount() > 0) {
            messages = serviceResponse.getMessages();
        } else {
            fail("No error messages found");
        }
        return messages.getErrors();
        
    }

}

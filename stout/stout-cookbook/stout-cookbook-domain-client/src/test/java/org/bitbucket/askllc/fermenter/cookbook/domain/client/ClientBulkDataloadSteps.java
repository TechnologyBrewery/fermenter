package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.ValidationExampleMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private boolean bulkDeleteSuccess = false;
    private boolean bulkCreateOrUpdateSuccess = false;
    private boolean errorCaught = false;

    private ValidationExample exampleTest = new ValidationExample();

    @Inject
    private ValidationExampleMaintenanceDelegate validationExampleMaintenanceDelegate;

    @Before("@clientBulkDataload")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
        MessageManagerInitializationDelegate.cleanupMessageManager();
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
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be deleted$")
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        Collection<ValidationExample> invalidExamplesForDeletion = new ArrayList<>();
        savedExamples.stream().forEach(persisted -> {
            ValidationExample copy = new ValidationExample();
            copy.setId(persisted.getId());
            copy.setRequiredField(persisted.getRequiredField());
            invalidExamplesForDeletion.add(copy);
        });
        for (ValidationExample entity : invalidExamplesForDeletion) {
            entity.setId(UUID.randomUUID());
        }
        try {
            validationExampleMaintenanceDelegate.bulkDelete(invalidExamplesForDeletion);
        } catch (WebApplicationException e) {
            bulkDeleteSuccess = false;
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

    @When("^the empty data is sent over in bulk to be deleted$")
    public void the_empty_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        try {
            validationExampleMaintenanceDelegate.bulkDelete(emptyExamples);
        } catch (WebApplicationException e) {
            errorCaught = true;
        }
    }

    @When("^the object is bulk deleted with an invalid field$")
    public void the_object_is_bulk_deleted_with_an_invalid_field() throws Throwable {
        exampleTest.setRequiredField(null);
        Collection<ValidationExample> updateList = new ArrayList<>();
        updateList.add(exampleTest);
        try {
            validationExampleMaintenanceDelegate.bulkDelete(updateList);
        } catch (WebApplicationException e) {
            errorCaught = true;
        }
    }

    @When("^two objects are bulk updated with an invalid field$")
    public void two_objects_are_bulk_updated_with_an_invalid_field() throws Throwable {

        Collection<ValidationExample> invalidExamples = new ArrayList<>();
        savedExamples.stream().forEach(persisted -> {
            ValidationExample copy = new ValidationExample();
            copy.setId(persisted.getId());
            copy.setRequiredField(null);
            invalidExamples.add(copy);
        });
        try {
            validationExampleMaintenanceDelegate.bulkSaveOrUpdate(invalidExamples);
        } catch (WebApplicationException e) {
            errorCaught = true;
        }
    }

    @When("^two objects are bulk deleted after they are no longer in the database$")
    public void two_objects_are_bulk_deleted_after_they_are_no_longer_in_the_database() throws Throwable {

        savedExamples.stream().forEach(object -> validationExampleMaintenanceDelegate.delete(object.getId()));
        try {
            validationExampleMaintenanceDelegate.bulkDelete(savedExamples);
        } catch (WebApplicationException e) {
            errorCaught = true;
        }
        savedExamples.clear();
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
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ClientBulkDataloadSteps.class);

    }

    @Then("^each data value is not updated and an error is thrown$")
    public void each_data_value_is_not_updated_and_an_error_is_thrown() throws Throwable {
        assertFalse("Bulk create or update did not fail for invalid data", bulkCreateOrUpdateSuccess);
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ClientBulkDataloadSteps.class);
    }

    @Then("^each data value is not deleted and an error is thrown$")
    public void each_data_value_is_not_deleted_and_an_error_is_thrown() throws Throwable {
        assertFalse("Bulk delete is successful when expecting failure", bulkDeleteSuccess);
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), ClientBulkDataloadSteps.class);

    }

    @Then("^a HTTP (\\d+) error is returned$")
    public void a_HTTP_error_is_returned(int arg1) throws Throwable {
        assertTrue("An error was not thrown when attempting to pass in empty data", errorCaught);
    }

    @Then("^a message is created with the object's primary key$")
    public void a_message_is_created_with_the_object_s_primary_key() throws Throwable {
        assertTrue("An error was not thrown when attempting to pass invalid data", errorCaught);
    }

    @Then("^a message is created with the objects' primary keys$")
    public void a_message_is_created_with_the_objects_primary_keys() throws Throwable {
        assertTrue("An error was not thrown when attempting to pass invalid data", errorCaught);
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

}

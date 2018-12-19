package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;
import org.hibernate.Session;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.PendingException;
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
    private Collection<ValidationExampleBO> mixedExamples = new ArrayList<>();
    private Collection<ValidationExampleBO> goodThenBadExamples = new ArrayList<>();
    private ValueServiceResponse<Collection<ValidationExampleBO>> valueResponseValidData;
    private VoidServiceResponse voidResponse;
    @PersistenceContext
    private EntityManager entityManager;

    @Before("@bulkDataload")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After("@bulkDataload")
    public void cleanupMsgMgr() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        ValidationExampleBO.deleteAllValidationExamples();
        allValidExamples.clear();
        mixedExamples.clear();
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
        valueResponseValidData = validationExampleMaintenanceService.bulkSaveOrUpdate(allValidExamples);
    }

    @Then("^each data value is created and saved$")
    public void each_data_value_is_created_and_saved() throws Throwable {
        checkSaveSuccess(allValidExamples);
    }

    @When("^the valid data is sent over in bulk to be updated$")
    public void the_valid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        Collection<ValidationExampleBO> savedRecords = valueResponseValidData.getValue();
        for (ValidationExampleBO validExample : savedRecords) {
            validExample.setRequiredField("some updated string");
        }
        saveOrUpdateRecords(savedRecords);
    }

    @Then("^each data value is updated and saved$")
    public void each_data_value_is_updated_and_saved() throws Throwable {
        checkSaveSuccess(allValidExamples);
        Collection<ValidationExampleBO> updatedExamples = valueResponseValidData.getValue();
        for (ValidationExampleBO validExample : updatedExamples) {
            assertEquals("the valid data was not actually updated", "some updated string", validExample.getRequiredField());
        }
    }

    @When("^the valid data is sent over to be deleted$")
    public void the_valid_data_is_sent_over_to_be_deleted() throws Throwable {
        saveOrUpdateRecords(allValidExamples);
        voidResponse = validationExampleMaintenanceService.bulkDelete(valueResponseValidData.getValue());
    }

    @Then("^each data value is deleted$")
    public void each_data_value_is_deleted() throws Throwable {
        for (ValidationExampleBO value : valueResponseValidData.getValue()) {
            ValidationExampleBO persisted = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNull("Should not have found any ValidationExampleBO records", persisted);
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be created$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_created() throws Throwable {
        try {
            valueResponseValidData = validationExampleMaintenanceService.bulkSaveOrUpdate(mixedExamples);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            // get entity back and look at the messages
            // set it equal to the value response valid data
            // make sure the messages has error information
            // look at each entity and make sure you get a message with each entity
        }
    }

    @Then("^each data value is not saved and an error is thrown$")
    public void each_data_value_is_not_saved_and_an_error_is_thrown() throws Throwable {
        List<ValidationExampleBO> examples = ValidationExampleBO.grabAllWithRequiredField();
        assertEquals("No ValidationExampleBO record should have been persisted", examples.size(), 0);
    }

    @When("^the valid and invalid data is sent over in bulk to be updated$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_updated() throws Throwable {
        valueResponseValidData = validationExampleMaintenanceService.bulkSaveOrUpdate(allValidExamples);
        for (ValidationExampleBO validExample : valueResponseValidData.getValue()) {
            validExample.setRequiredField(null);
            break;
        }
        valueResponseValidData = validationExampleMaintenanceService
                .bulkSaveOrUpdate(valueResponseValidData.getValue());

        // need this here because when the session does a flush, it says 'Error during
        // managed flush [org.hibernate.PropertyValueException: not-null property
        // references a null or transient value :
        // org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO.requiredField]'
        for (ValidationExampleBO validExample : valueResponseValidData.getValue()) {
            validExample.setRequiredField("filler info");
        }
    }

    @Then("^each data value is not updated and an error is thrown$")
    public void each_data_value_is_not_updated_and_an_error_is_thrown() throws Throwable {
        assertNotNull("Response should come with error messages, but it didnt.", valueResponseValidData.getMessages());
        for (ValidationExampleBO value : valueResponseValidData.getValue()) {
            ValidationExampleBO found = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNotNull("This ValidationExampleBO record should NOT have been updated to null",
                    found.getRequiredField());
        }
    }

    @When("^the valid and invalid data is sent over in bulk to be deleted$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_valid_and_invalid_data_is_sent_over_in_bulk_to_be_deleted() throws Throwable {
        valueResponseValidData = validationExampleMaintenanceService.bulkSaveOrUpdate(allValidExamples);
        goodThenBadExamples.addAll(valueResponseValidData.getValue());
        
        UUID originalId = null;
        ValidationExampleBO originalValEx = null;
        
        ValidationExampleBO bad = new ValidationExampleBO();
        bad.setKey(UUID.randomUUID());
        goodThenBadExamples.add(bad);
        
        
//        for (ValidationExampleBO validExample : goodThenBadExamples) {
//            // can't make ID null because we have a check for that and a diff type of error will get thrown
//            // validExample.setKey(null);
////            originalId = validExample.getKey();
////            originalValEx = validExample;
//            validExample.setKey(UUID.randomUUID());
//            break;
//        }
        voidResponse = validationExampleMaintenanceService.bulkDelete(goodThenBadExamples);
//        originalValEx.setKey(originalId);
    }

    @Then("^each data value is not deleted and an error is thrown$")
    public void each_data_value_is_not_deleted_and_an_error_is_thrown() throws Throwable {
        assertNotNull("Response should come with error messages, but it didnt.", voidResponse.getMessages());
        for (ValidationExampleBO value : valueResponseValidData.getValue()) {
            ValidationExampleBO persisted = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNotNull("This ValidationExampleBO record should have been found and NOT deleted", persisted);
        }
    }

    private void saveOrUpdateRecords(Collection<ValidationExampleBO> examples) {
        valueResponseValidData = validationExampleMaintenanceService.bulkSaveOrUpdate(examples);
    }

    private void checkSaveSuccess(Collection<ValidationExampleBO> examples) {
        assertEquals("the valid bulk set of data did not save properly", valueResponseValidData.getValue().size(),
                examples.size());

        for (ValidationExampleBO value : valueResponseValidData.getValue()) {
            ValidationExampleBO persisted = ValidationExampleBO.findByPrimaryKey(value.getKey());
            assertNotNull("Should have found ValidationExampleBO record for " + value.getKey(), persisted);
        }
    }
}

package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.NonUUIDKeyEntityBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.NonUUIDKeyEntityMaintenanceService;
import org.bitbucket.fermenter.stout.mock.MockRequestScope;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
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
public class EntityMaintenanceValidationSteps {
    @Inject
    private NonUUIDKeyEntityMaintenanceService nonUUIDKeyEntityMaintenanceService;

    private ValueServiceResponse<NonUUIDKeyEntityBO> valueServiceResponse;
    private boolean errorThrown = false;

    @Inject
    private MockRequestScope mockRequestScope;

    @Before("@entityValidation")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After("@entityValidation")
    public void cleanUp() {
        mockRequestScope.cleanMessageManager();
        NonUUIDKeyEntityBO.deleteAllNonUUIDKeyEntities();
    }

    @Given("^an entity exists in the system with the id \"([^\"]*)\"$")
    public void an_entity_exists_in_the_system_with_the_id(String idInput) throws Throwable {
        NonUUIDKeyEntityBO entity = new NonUUIDKeyEntityBO();
        entity.setKey(idInput);
        entity.save();

        entity = NonUUIDKeyEntityBO.findByPrimaryKey(idInput);

        assertNotNull("attempt to save entity with id " + idInput + " did not work", entity);
    }

    @When("^the system attempts to grab the entity by the id \"([^\"]*)\"$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_system_attempts_to_grab_the_entity_by_the_id(String idInput) throws Throwable {
        try {
            valueServiceResponse = nonUUIDKeyEntityMaintenanceService.findByPrimaryKey(idInput);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            valueServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
            errorThrown = true;
        }
    }
    
    @When("^the system attempts to grab an entity by a null id$")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void the_system_attempts_to_grab_an_entity_by_a_null_id() throws Throwable {
        try {
            valueServiceResponse = nonUUIDKeyEntityMaintenanceService.findByPrimaryKey(null);
        } catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            valueServiceResponse = (ValueServiceResponse) e.getResponse().getEntity();
            errorThrown = true;
        }
    }

    @Then("^the entity with the id \"([^\"]*)\" is returned without any errors$")
    public void the_entity_with_the_id_is_returned_without_any_errors(String idInput) throws Throwable {
        NonUUIDKeyEntityBO persistedEntity = valueServiceResponse.getValue();
        assertFalse("error messages were encountered when finding an entity with the primary key " + idInput,
                valueServiceResponse.getMessages().hasErrors());
        assertNotNull("an entity with the id " + idInput + " is not returned", persistedEntity);
        assertEquals("the entity returned does not have the id " + idInput, idInput, persistedEntity.getKey());
    }

    @Then("^an error message and HTTP Status (\\d+) is thrown$")
    public void an_error_message_and_HTTP_Status_is_thrown(String status) throws Throwable {    
        assertTrue("error messages should have been encountered",
                valueServiceResponse.getMessages().hasErrors());
        assertTrue("an error was not thrown when findByPrimaryKey returned with null", errorThrown);
    }
}

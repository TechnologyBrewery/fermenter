package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.technologybrewery.fermenter.cookbook.domain.service.rest.SecurityTestService;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.service.ValueServiceResponse;
import org.technologybrewery.fermenter.stout.test.MessageTestUtils;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SecuritySteps {

    private static final String USERNAME = "testUser";

    @Inject
    private SecurityTestService securityTestService;

    private String returnedUsername;
    private boolean encountedAuthorizationException;
    
    @After("@authentication")
    public void logout() {
        AuthenticationTestUtils.logout();
    }

    @Given("^an authenticated user$")
    public void an_authenticated_user() throws Throwable {
        AuthenticationTestUtils.login(USERNAME);
    }

    @When("^authentication information is requested from within a service$")
    public void authentication_information_is_requested_from_within_a_service() throws Throwable {
        ValueServiceResponse<String> response = securityTestService.echoUsername();
        MessageTestUtils.assertNoErrorMessages(response);
        returnedUsername = response.getValue();
    }

    @When("^logout is invoked before authentication information is requested from within a service$")
    public void logout_is_invoked_before_authentication_information_is_requested_from_within_a_service()
            throws Throwable {
        ValueServiceResponse<String> response = securityTestService.logoutThenEchoUsername();
        MessageTestUtils.assertNoErrorMessages(response);
        returnedUsername = response.getValue();
    }

    @When("^logout is invoked before an operation that requires authorization is invoked$")
    public void logout_is_invoked_before_an_operation_that_requires_authorization_is_invoked() throws Throwable {
        try {
            securityTestService.logoutThenInvokeAuthorizationProtectedOperation();
        } catch (javax.ws.rs.NotAuthorizedException e) {
            encountedAuthorizationException = true;
        }
    }

    @Then("^the username is returned$")
    public void the_username_is_returned() throws Throwable {
        assertEquals("Unexpected username returned!", USERNAME, returnedUsername);
    }

    @Then("^no username is returned$")
    public void no_username_is_returned() throws Throwable {
        assertNull("Username should have been null!", returnedUsername);
    }

    @Then("^an authorization error is returned$")
    public void an_authorization_error_is_returned() throws Throwable {
        assertTrue("Authorization exception should have been encountered!", encountedAuthorizationException);
    }

}

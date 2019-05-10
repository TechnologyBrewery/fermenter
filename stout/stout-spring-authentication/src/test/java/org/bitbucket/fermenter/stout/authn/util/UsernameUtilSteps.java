package org.bitbucket.fermenter.stout.authn.util;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml" })
public class UsernameUtilSteps {

    private String actualUsername = null;

    @Given("^a username \"([^\"]*)\" is saved as a \"([^\"]*)\"$")
    public void a_username_is_saved_as_a(String username, String type) throws Throwable {

        if ("User".equals(type)) {
            bootstrapWithUser(username);
        } else {
            bootstrapWithUsername(username);
        }
    }

    @When("^I retrieve the username$")
    public void i_retrieve_the_username() throws Throwable {
        actualUsername = UsernameUtil.getLoggedInUsername();
    }

    @Then("^I get username \"([^\"]*)\"$")
    public void i_get_username(String expectedUsername) throws Throwable {
        assertEquals("The username pulled from the SecurityContext did not match expected", expectedUsername,
                actualUsername);
    }

    /**
     * Any project using the stout-spring-authentication will have the username
     * set as a Spring {@link User} object. However, several projects set the
     * context with a string for integration tests or when doing service to
     * service calls, so we want to be able to handle both formats without class
     * cast exceptions.
     * 
     * @param username
     */
    private void bootstrapWithUser(String username) {
        User user = new User(username, "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(user, "user"));
    }

    private void bootstrapWithUsername(String username) {
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(username, "user"));
    }

}

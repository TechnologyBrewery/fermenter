package org.bitbucket.fermenter.stout.authz;

import static org.junit.Assert.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AuthorizationSteps {

    private String resource;
    private String action;
    private String subject;
    private PolicyDecision decision;

    private PolicyDecisionPoint pdp = PolicyDecisionPoint.getInstance();

    @Given("^a resource \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void a_resource_and_subject(String resource, String subject) throws Throwable {
        this.resource = resource;
        this.action = "ballInPlay";
        this.subject = subject;
    }

    @Given("^a resource action \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void a_resource_action_and_subject(String action, String subject) throws Throwable {
        this.resource = "twoStrikeAtBat";
        this.action = action;
        this.subject = subject;
    }

    @Given("^a resource \"([^\"]*)\", action \"([^\"]*)\" and subject \"([^\"]*)\"$")
    public void a_resource_action_and_subject(String resource, String action, String subject) throws Throwable {
        this.resource = resource;
        this.action = action;
        this.subject = subject;
    }

    @When("^a policy decision is requested$")
    public void a_policy_decision_is_requested() throws Throwable {
        decision = pdp.isAuthorized(subject, resource, action);
    }

    @Then("^a \"([^\"]*)\" decision is returned$")
    public void a_decision_is_returned(String expectedDecision) throws Throwable {
        assertEquals(PolicyDecision.valueOf(expectedDecision), this.decision);

    }

}

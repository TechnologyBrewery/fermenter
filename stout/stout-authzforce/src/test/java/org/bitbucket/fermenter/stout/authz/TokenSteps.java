package org.bitbucket.fermenter.stout.authz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.bitbucket.fermenter.stout.authz.json.JsonWebTokenUtil;
import org.bitbucket.fermenter.stout.authz.json.PolicyRequest;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class TokenSteps {

    private String token;
    private String subject;
    private String audience;
    
    @After("@jwtToken")
    public void tearDown() {
        token = null;
        subject = null;
        audience = null;
    }
    
    @When("^a token is requested for \"([^\"]*)\" and \"([^\"]*)\"$")
    public void a_token_is_requested_for_and(String subject, String audience) throws Throwable {
        this.subject = subject;
        this.audience = audience;
        token = JsonWebTokenUtil.createToken(subject, audience, null);
    }
    
    @When("^the following claims:$")
    public void a_token_is_requested_for_and_and_the_following_claims(List<PolicyRequest> claims) throws Throwable {        
        token = JsonWebTokenUtil.createToken(subject, audience, claims);
        
    }    

    @Then("^the token contains claims for \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
    public void the_token_contains_claims_for_and(String expectedSubject, String expectedAudience, String expectedIssuer) throws Throwable {
        Jws<Claims> jwt = JsonWebTokenUtil.parseLocalToken(token);
        assertNotNull("token could not be parsed!", jwt);
        
        Claims claims = jwt.getBody();
        assertEquals("Unexpected subject!", expectedSubject, claims.getSubject());
        assertEquals("Unexpected audience!", expectedAudience, claims.getAudience());
        assertEquals("Unexpected issuer!", expectedIssuer, claims.getIssuer());
    }    

    @Then("^a claim with not before time skew of (\\d+) seconds from the issue time$")
    public void a_claim_with_not_before_time_skew_of_seconds_from_the_issue_time(int expectedSkewSeconds) throws Throwable {
        Jws<Claims> jwt = JsonWebTokenUtil.parseLocalToken(token);
        assertNotNull("token could not be parsed!", jwt);
        
        Claims claims = jwt.getBody();
        Date issuedAt = claims.getIssuedAt();
        Date notBefore = claims.getNotBefore();
        long actualNotBeforeSkew = (issuedAt.getTime() - notBefore.getTime()) / 1000;
        assertEquals("Unexpected not before skew encountered!", expectedSkewSeconds, actualNotBeforeSkew);
    }

    @Then("^a claim with an expiration time (\\d+) seconds from the issue time with an addition (\\d+) seconds for skew$")
    public void a_claim_with_an_expiration_time_seconds_from_the_issue_time_with_an_addition_seconds_for_skew(int expectedExpiration, int expectedSkew) throws Throwable {
        Jws<Claims> jwt = JsonWebTokenUtil.parseLocalToken(token);
        assertNotNull("token could not be parsed!", jwt);
        
        Claims claims = jwt.getBody();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        long actualExpirationTime = (expiration.getTime() - issuedAt.getTime()) / 1000;
        long expectedTotalTimeInFuture = expectedExpiration + expectedSkew; 
        assertEquals("Unexpected not before skew encountered!", expectedTotalTimeInFuture, actualExpirationTime);
    }
    
    @Then("^a claim is returned with the following rule and decision pairings:$")
    public void a_claim_is_returned_with_the_following_rule_and_decision_pairings(List<TokenDataInput> expectedResults) throws Throwable {
        Jws<Claims> jwt = JsonWebTokenUtil.parseLocalToken(token);
        assertNotNull("token could not be parsed!", jwt);
        
        for (TokenDataInput expectedResult : expectedResults) {
            PolicyRequest key = new PolicyRequest();
            key.setName(expectedResult.getName());
            key.setResource(expectedResult.getResource());
            key.setAction(expectedResult.getAction());
            
            String result = jwt.getBody().get(key.toString(), String.class);
            
            assertEquals("Unexpected decision encountered! ", expectedResult.getResult(), result); 
        }
    }    
    
}

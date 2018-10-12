package org.bitbucket.askllc.fermenter.cookbook.domain;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.After;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientEntityExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.IdentityKeyedEntityBO;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
        "classpath:h2-spring-ds-context.xml" })
@Transactional
public class ReflectionStringSteps {
    private ArrayList<String> fieldNames;
    private Object springObject;
    private String enhancedString;

    @After("@enhancedToString")
    public void cleanup() throws Exception {
        springObject = null;
        enhancedString = null;
        fieldNames = new ArrayList<String>();

    }

    @Given("^the object is type \"([^\"]*)\"$")
    public void the_object_is_type(String currentObjectType) throws Throwable {
        if(currentObjectType.equals("IdentityKeyedEntityBO")){
            springObject = new IdentityKeyedEntityBO();
        }else if (currentObjectType.equals("TransientEntityExampleBO")){
            springObject = new TransientEntityExampleBO();
        }
        assertNotNull("Incorrect type declaration: " + currentObjectType, springObject);

    }

    @When("^the system checks the fields of the toStringOutput$")
    public void the_system_checks_the_fields_of_the_toStringOutput() throws Throwable {
        enhancedString = springObject.toString();
        assertNotNull("toString should not return null", enhancedString);
    }

    @Then("^the toStringOutput should contain all of the object fields \"([^\"]*)\"$")
    public void the_toStringOutput_should_contain_all_of_the_object_fields(String fieldNames) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String[] fieldNamesArr = fieldNames.trim().split("");
        for(String temp : fieldNamesArr){
            assertTrue("Expected field not encountered: " + temp, enhancedString.contains(temp));
        }
    }

}

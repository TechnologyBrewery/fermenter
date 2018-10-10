package org.bitbucket.askllc.fermenter.cookbook.domain;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.After;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientEntityExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.IdentityKeyedEntityBO;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
        "classpath:h2-spring-ds-context.xml" })
@Transactional
public class ReflectionStringSteps {
    private HashMap<String, ArrayList<String>> fieldNamesByObjectType = new HashMap<String, ArrayList<String>>();
    private Object reflectionObject;
    private String reflectedString;
    private String objectType;

    @After("@apacheReflectionToString")
    public void cleanup() throws Exception {
        reflectionObject = null;
        reflectedString = null;
        objectType = null;

    }
    @Given("^the following object types exist with the following field names$")
    public void the_following_object_types_exist_with_the_following_field_names(List<List<String>> objectsAndFields) throws Throwable {
        for(int i = 0; i < objectsAndFields.size(); i++){
            ArrayList<String> fieldNames = new ArrayList<String>();
            String[] test = objectsAndFields.get(i).get(1).trim().split(":");
            for(int k = 0; k < test.length; k++){
                fieldNames.add(test[k]);
            }

            fieldNamesByObjectType.put(objectsAndFields.get(i).get(0), fieldNames);
        }
    }

    @Given("^the object type is \"([^\"]*)\"$")
    public void the_object_type_is(String type) throws Throwable {
        objectType = type;
        if(objectType.equals("IdentityKeyedEntityBO")) {
            reflectionObject = new IdentityKeyedEntityBO();
        }else if(objectType.equals("TransientEntityExampleBO")){
            reflectionObject = new TransientEntityExampleBO();
        }else{
            reflectionObject = null;
        }

    }

    @When("^the system returns the reflection string$")
    public void the_system_returns_the_reflection_string() throws Throwable {
        assertNotNull(objectType
                + " is not in the background data", reflectionObject);
        reflectedString = reflectionObject.toString();
    }

    @Then("^the reflection string should contain all field names$")
    public void the_reflection_string_should_contain_all_field_names() throws Throwable {
        for(String fieldName : fieldNamesByObjectType.get(objectType)){
            assertTrue("Expected field " + fieldName + " does not exist in the reflected string: "
                        + reflectedString + " / " + reflectionObject.getClass().toString(), reflectedString.contains(fieldName));
        }
    }

}

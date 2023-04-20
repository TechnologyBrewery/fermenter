package org.technologybrewery.fermenter.stout.authz;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.technologybrewery.fermenter.stout.authz.json.StoutAttribute;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;

public class AttributeSteps {
    
    private StoutAttribute stoutAttribute;
    private AttributeDesignatorType xacmlAttribute;
    
    @Before
    public void setUp() {
        stoutAttribute = null;
        xacmlAttribute = null;
    }

    @Given("^an attribute with stout type \"([^\"]*)\"$")
    public void an_attribute_with_stout_type(String stoutType) throws Throwable {
        stoutAttribute = new StoutAttribute();
        stoutAttribute.setType(stoutType);
        
        //default other needed values:
        stoutAttribute.setId(RandomStringUtils.randomAlphanumeric(10));
        stoutAttribute.setCategory("action");
        stoutAttribute.setRequired(false);
        
    }
    
    @Given("^an attribute with stout category \"([^\"]*)\"$")
    public void an_attribute_with_stout_category(String stoutCategory) throws Throwable {
        stoutAttribute = new StoutAttribute();
        stoutAttribute.setCategory(stoutCategory);
        
        //default other needed values:
        stoutAttribute.setId(RandomStringUtils.randomAlphanumeric(10));
        stoutAttribute.setRequired(false);
        
    }   

    @When("^the attribute is read$")
    public void the_attribute_is_read() throws Throwable {
        xacmlAttribute = StoutAttributeUtils.translateAttributeToXacmlFormat(stoutAttribute);
    }

    @Then("^the fully qualified type \"([^\"]*)\" is returned$")
    public void the_fully_qualified_type_is_returned(String fullyQualifiedType) throws Throwable {
        assertEquals(fullyQualifiedType, xacmlAttribute.getDataType());
    }

    @Then("^the fully qualified category \"([^\"]*)\" is returned$")
    public void the_fully_qualified_category_is_returned(String fullyQualifiedCategory) throws Throwable {
        assertEquals(fullyQualifiedCategory, xacmlAttribute.getCategory());
    }     

}

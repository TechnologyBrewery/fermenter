package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
		"classpath:h2-spring-ds-context.xml" })
@Transactional
public class FieldValidationSteps {

	private String userString;
	private String userStringRegex;
	private String userStringReqField;
	private String userStringChildReqField;
	private Long userLong;
	private int userInt;
	private BigDecimal userBigDecimal;
	private ValidationExampleBO example;
	private ValidationExampleChildBO exampleChild;

	@After("@fieldValidation")
	public void cleanupMsgMgr() throws Exception {
		MessageManagerInitializationDelegate.cleanupMessageManager();
		ValidationExampleBO.deleteAllValidationExamples();
	}

	/*
	 * validation steps for maxLength and minLength of a String
	 */

	@Given("^a \"([^\"]*)\" to validate against the validation example string example field$")
	public void a_to_validate_against_the_validation_example_string_example_field(String value) throws Throwable {
		this.userString = value;

		example = new ValidationExampleBO();
		example.setStringExample(userString);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that value$")
	public void field_level_validation_is_performed_on_that_value() throws Throwable {

		example.validate();

	}

	@Then("^the validation returns no errors$")
	public void the_validation_returns_no_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				org.bitbucket.fermenter.stout.messages.Messages.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Then("^the validation returns errors$")
	public void the_validation_returns_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
				org.bitbucket.fermenter.stout.messages.Messages.class);
		assertFalse("Should have encountered messages!", MessageManager.hasErrorMessages());

	}

	/*
	 * validation steps for maxValue and minValue of a Long
	 */

	@Given("^a (\\d+) to validate against the validation example long example field$")
	public void a_to_validate_against_the_validation_example_long_example_field(long value) throws Throwable {
		this.userLong = value;

		example = new ValidationExampleBO();
		example.setLongExample(userLong);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that long value$")
	public void field_level_validation_is_performed_on_that_long_value() throws Throwable {

		example.validate();

	}

	@Then("^the long validation returns no errors$")
	public void the_long_validation_returns_no_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Then("^the long validation returns errors$")
	public void the_long_validation_returns_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Given("^a negative -(\\d+) to validate against the validation example long example field$")
	public void a_negative_to_validate_against_the_validation_example_long_example_field(long value) throws Throwable {
		this.userLong = value;

		example = new ValidationExampleBO();
		example.setLongExample(userLong);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	/*
	 * validation steps for maxValue and minValue of an Integer
	 */

	@Given("^an (\\d+) to validate against the validation example integer example field$")
	public void an_to_validate_against_the_validation_example_integer_example_field(int value) throws Throwable {
		this.userInt = value;

		example = new ValidationExampleBO();
		example.setIntegerExample(userInt);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that integer value$")
	public void field_level_validation_is_performed_on_that_integer_value() throws Throwable {

		example.validate();

	}

	@Then("^the integer validation returns no errors$")
	public void the_integer_validation_returns_no_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Then("^the integer validation returns errors$")
	public void the_integer_validation_returns_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Given("^a negative -(\\d+) to validate against the validation example integer example field$")
	public void a_negative_to_validate_against_the_validation_example_integer_example_field(int value)
			throws Throwable {
		this.userInt = value;

		example = new ValidationExampleBO();
		example.setIntegerExample(userInt);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}
	
	/*
	 * Validation steps for maxValue and minValue of a BigDecimal
	 */

	@Given("^a (\\d+)\\.(\\d+) to validate against the validation example BigDecimal example field$")
	public void a_to_validate_against_the_validation_example_BigDecimal_example_field(double value1, double value2)
			throws Throwable {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			if (value2 / 10 > 0) {
				value2 = value2 / 10;
				count++;
			}
		}
		count = count * 10;

		double value = value1 + (value2 / count);

		this.userBigDecimal = BigDecimal.valueOf(value);

		example = new ValidationExampleBO();
		example.setBigDecimalExample(userBigDecimal);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}

	@When("^field level validation is performed on that BigDecimal value$")
	public void field_level_validation_is_performed_on_that_BigDecimal_value() throws Throwable {

		example.validate();

	}

	@Then("^the BigDecimal validation returns no errors$")
	public void the_BigDecimal_validation_returns_no_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());

	}
	
	@Then("^the BigDecimal validation returns errors$")
	public void the_BigDecimal_validation_returns_errors() throws Throwable {

		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());

	}

	@Given("^a negative -(\\d+)\\.(\\d+) to validate against the validation example BigDecimal example field$")
	public void a_negative_to_validate_against_the_validation_example_BigDecimal_example_field(double value1, double value2) throws Throwable {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			if (value2 / 10 > 0) {
				value2 = value2 / 10;
				count++;
			}
		}
		count = count * 10;

		double value = value1 + (value2 / count);

		this.userBigDecimal = BigDecimal.valueOf(value);

		example = new ValidationExampleBO();
		example.setBigDecimalExample(userBigDecimal);
		example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}
	
	/*
	 * Validation steps for the regular expression (regex) String format 
	 */
	
	@Given("^a \"([^\"]*)\" to validate against the regEx example String example field$")
	public void a_to_validate_against_the_regEx_example_String_example_field(String value) throws Throwable {
	    this.userStringRegex = value;
	    
	    example = new ValidationExampleBO();
	    example.setRegexExample(userStringRegex);
	    example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

	}
	
	@When("^field level validation is performed on that regEx String value$")
	public void field_level_validation_is_performed_on_that_regEx_String_value() throws Throwable {
	    
		example.validate();
		
	}
	
	@Then("^the regEx String validation returns no errors$")
	public void the_regEx_String_validation_returns_no_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
		
	}
	
	@Then("^the regEx String validation returns errors$")
	public void the_regEx_String_validation_returns_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
		
	}
	
	/*
     * Validation steps for the regular expression (regex) Zipcode format 
     */
    
    @Given("^a \"([^\"]*)\" to validate against the regEx example Zipcode example field$")
    public void a_to_validate_against_the_regEx_example_Zipcode_example_field(String value) throws Throwable {
        this.userStringRegex = value;
        
        example = new ValidationExampleBO();
        example.setRegexZipcodeExample(userStringRegex);
        example.setRequiredField(RandomStringUtils.randomAlphanumeric(10));

    }
    
    @When("^field level validation is performed on that regEx Zipcode value$")
    public void field_level_validation_is_performed_on_that_regEx_Zipcode_value() throws Throwable {
        
        example.validate();
        
    }
    
    @Then("^the regEx Zipcode validation returns no errors$")
    public void the_regEx_Zipcode_validation_returns_no_errors() throws Throwable {
        
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
        
    }
    
    @Then("^the regEx Zipcode validation returns errors$")
    public void the_regEx_Zipcode_validation_returns_errors() throws Throwable {
        
        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        
    }
	
	/*
	 * Validation steps for the required field String example
	 */
	
	@Given("^a \"([^\"]*)\" to validate against the required field String example field$")
	public void a_to_validate_against_the_required_field_String_example_field(String value) throws Throwable {
	    this.userStringReqField = value;
		
	    example = new ValidationExampleBO();
	    example.setRequiredField(userStringReqField);
		
	}
	
	@When("^a field validation is performed on the required field String value$")
	public void a_field_validation_is_performed_on_the_required_field_String_value() throws Throwable {
	    
		example.validate();
		
	}

	@Then("^the required field returns a value with no errors$")
	public void the_required_field_returns_a_value_with_no_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
		
	}
	
	@Given("^a null to validate against the required field String example field$")
	public void a_null_to_validate_against_the_required_field_String_example_field() throws Throwable {
	    
		example = new ValidationExampleBO();
		example.setRequiredField(null);
		
	}
	
	@Then("^the required field returns a null with errors$")
	public void the_required_field_returns_a_null_with_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
		
	}
	
	/*
	 * Validation steps for the child required field String example
	 */

	@Given("^a \"([^\"]*)\" to validate against the child required field String example field$")
	public void a_to_validate_against_the_child_required_field_String_example_field(String value) throws Throwable {
	    this.userStringChildReqField = value;
		
	    example = new ValidationExampleBO();
		exampleChild = new ValidationExampleChildBO();
		example.setRequiredField(RandomStringUtils.randomAlphabetic(5));
		exampleChild.setRequiredField(userStringChildReqField);
	    example.addValidationExampleChild(exampleChild);
	    
	}
	
	@When("^a field validation is performed on the child required field String value$")
	public void a_field_validation_is_performed_on_the_child_required_field_String_value() throws Throwable {
	    	
		example.validate();
		
	}
	
	@Then("^the child required field returns a value with no errors$")
	public void the_child_required_field_returns_a_value_with_no_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
		
	}
	
	@Given("^a null to validate against the child required field String example field$")
	public void a_null_to_validate_against_the_child_required_field_String_example_field() throws Throwable {
	    
		example = new ValidationExampleBO();
		exampleChild = new ValidationExampleChildBO();
		exampleChild.setRequiredField(null);
		example.addValidationExampleChild(exampleChild);
		
	}
	
	@Then("^the child required field returns a null with errors$")
	public void the_child_required_field_returns_a_null_with_errors() throws Throwable {
	    
		MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
		assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
		
	}
}

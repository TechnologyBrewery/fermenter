package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.technologybrewery.fermenter.stout.test.MessageTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.technologybrewery.fermenter.stout.messages.Messages;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class FieldValidationSteps {

    private String userString;
    private String userStringRegex;
    private String userStringReqField;
    private String userStringChildReqField;
    private Long userLong;
    private int userInt;
    private int scale;
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
                Messages.class);
        assertFalse("Should not have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
    }

    @Then("^the validation returns errors$")
    public void the_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(),
                Messages.class);
        assertFalse("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
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
        cleanUpMessages();
    }

    @Then("^the long validation returns errors$")
    public void the_long_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
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
        cleanUpMessages();
    }

    @Then("^the integer validation returns errors$")
    public void the_integer_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
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
        cleanUpMessages();
    }

    @Then("^the BigDecimal validation returns errors$")
    public void the_BigDecimal_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
    }

    @Given("^a negative -(\\d+)\\.(\\d+) to validate against the validation example BigDecimal example field$")
    public void a_negative_to_validate_against_the_validation_example_BigDecimal_example_field(double value1,
            double value2) throws Throwable {
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
        cleanUpMessages();
    }

    @Then("^the regEx String validation returns errors$")
    public void the_regEx_String_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
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
        assertFalse("Should NOT have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
    }

    @Then("^the regEx Zipcode validation returns errors$")
    public void the_regEx_Zipcode_validation_returns_errors() throws Throwable {

        MessageTestUtils.logErrors("Error Messages", MessageManager.getMessages(), FieldValidationSteps.class);
        assertTrue("Should have encountered messages!", MessageManager.hasErrorMessages());
        cleanUpMessages();
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
        cleanUpMessages();
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
        cleanUpMessages();
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
        cleanUpMessages();
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
        cleanUpMessages();
    }

    @Given("^BigDecimal field default scale is (\\d+)$")
    public void default_scale_is(int defaultScale) throws Throwable {
        // information only: scale of 5 is used if no scale is specified
    }

    @Given("^RoundingMode is HALF_EVEN$")
    public void roundingmode_is_HALF_EVEN() throws Throwable {
        // information only: rounding mode of HALF_EVEN is hardcoded in the velocity
        // template
    }

    @When("^a \"([^\"]*)\" value without scale specification is added$")
    public void a_value_without_scale_specification_is_added(String valueString) throws Throwable {
        example = TestUtils.createRandomValidationExample();

        BigDecimal newNum = new BigDecimal(valueString);
        example.setBigDecimalExample(newNum);

        scale = example.getBigDecimalExample().scale();
        userBigDecimal = example.getBigDecimalExample();

        example.validate();
    }

    @When("^a \"([^\"]*)\" value with scale (\\d+) is added$")
    public void a_value_with_scale_is_added(String valueString, int initialScale) throws Throwable {
        BigDecimal newNum = new BigDecimal(valueString);
        example = TestUtils.createRandomValidationExample();

        switch (initialScale) {
        case 2:
            example.setBigDecimalExampleWithLargeScaleInteger(newNum);
            scale = example.getBigDecimalExampleWithLargeScaleInteger().scale();
            userBigDecimal = example.getBigDecimalExampleWithLargeScaleInteger();
            break;
        case 3:
            example.setBigDecimalExampleWithScale(newNum);
            scale = example.getBigDecimalExampleWithScale().scale();
            userBigDecimal = example.getBigDecimalExampleWithScale();
            break;
        case 10:
            example.setBigDecimalExampleWithLargeScale(newNum);
            scale = example.getBigDecimalExampleWithLargeScale().scale();
            userBigDecimal = example.getBigDecimalExampleWithLargeScale();
            break;
        }

        example.validate();
    }

    @Then("^the BigDecimal has scale of (\\d+)$")
    public void the_BigDecimal_has_the_scale_of(int expectedScale) throws Throwable {
        assertEquals("BigDecimal scale is incorrect!", expectedScale, scale);
        cleanUpMessages();
    }

    @Then("^the BigDecimal value is \"([^\"]*)\"$")
    public void the_BigDecimal_value_is(String expectedStringValue) throws Throwable {
        String actualStringValue = userBigDecimal.toString();
        assertEquals("BigDecimal value is incorrect!", expectedStringValue, actualStringValue);
        cleanUpMessages();
    }
    
    @Given("^a String value with extra white space for a field$")
    public void a_String_value_with_extra_white_space_for_a_field() throws Throwable {
        example = new ValidationExampleBO();
        example.setRequiredField("hello world    ");
    }

    @When("^the String value for the field is set and retrieved$")
    public void the_String_value_for_the_field_is_set_and_retrieved() throws Throwable {
        // do nothing
    }

    @Then("^the white space has been removed$")
    public void the_white_space_has_been_removed() throws Throwable {
        assertEquals("hello world", example.getRequiredField());
    }
    
    public void cleanUpMessages() {
        MessageManagerInitializationDelegate.cleanupMessageManager();
    }
}

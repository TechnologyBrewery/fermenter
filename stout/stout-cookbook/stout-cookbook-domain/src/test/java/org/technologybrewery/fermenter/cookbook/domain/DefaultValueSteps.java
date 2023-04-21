package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.DefaultValueExampleBO;
import org.technologybrewery.fermenter.cookbook.input.DefaultValueInput;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class DefaultValueSteps {
	
	private DefaultValueExampleBO defaultValueExample;
	
	@After("@defaultValueTest") 
	public void cleanUp() {
		defaultValueExample.delete();
	}
	
	@Given("^a DefaultValueExample object$")
	public void a_DefaultValueExample_object() throws Throwable {
		defaultValueExample = new DefaultValueExampleBO();
		defaultValueExample.save();
	}

	@When("^the user checks the field value for every <fieldName>$")
	public void the_user_checks_the_field_value_for_every_fieldName() throws Throwable {
		
	}

	@Then("^the following <fieldValue> is returned$")
	public void the_following_fieldValue_is_returned(List<DefaultValueInput> inputList) throws Throwable {
		for (DefaultValueInput currentInput : inputList) {
			switch (currentInput.getFieldName()) {
				case "Name":
					assertEquals("Default name did not contain correct default value", currentInput.getFieldValue(), defaultValueExample.getName());
					break;
				case "Type":
					assertEquals("Default type did not contain correct default value", currentInput.getFieldValue(), defaultValueExample.getType());
					break;
				case "Long":
					Long inputLongValue = (long) Integer.parseInt(currentInput.getFieldValue());
					assertEquals("Default long did not contain correct default value", inputLongValue, defaultValueExample.getTheLong1());
					break;
				case "Decimal":
					BigDecimal inputDecimalValue = new BigDecimal(currentInput.getFieldValue());
					assertEquals("Default BigDecimal did not contain correct default value", inputDecimalValue.toBigInteger(), defaultValueExample.getBigDecimalValue().toBigInteger());
					break;
				case "Boolean":
					assertEquals("Default boolean did not contain correct default value", currentInput.getFieldValue(), Boolean.toString(defaultValueExample.getStandardBoolean().booleanValue()));
					break;
				case "ConfusingJson":
					assertEquals("Default confusingJson did not contain correct default value", currentInput.getFieldValue(), defaultValueExample.getAMESSYNameThatConfusesJson());
					break;
				case "Integer":
					assertEquals("Default integer did not contain correct default value", Integer.parseInt(currentInput.getFieldValue()), defaultValueExample.getSomeInteger().intValue());
					break;
				case "Timestamp":
					Date inputDateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentInput.getFieldValue());
					Timestamp inputTimestampValue = new Timestamp(inputDateValue.getTime());
					assertEquals("Default timestamp did not contain correct default value", inputTimestampValue, defaultValueExample.getSomeTimestamp());
					break;
				case "Date":
					Date inputDateValueAgain = new SimpleDateFormat("yyyy-MM-dd").parse(currentInput.getFieldValue());
					assertEquals("Default date did not contain correct default value", inputDateValueAgain, defaultValueExample.getSomeDate());
					break;
				default:
					break;
			}
		}
	}

	
	 public static Date parseDate(String date) {
	     try {
	         return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }
}

package org.bitbucket.fermenter.stout.exceptions;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Test harness to run Gherkin Business Driven Development specifications.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/specifications", plugin="json:target/cucumber-html-reports/cucumber.json")
public class TestSpecifications {

}
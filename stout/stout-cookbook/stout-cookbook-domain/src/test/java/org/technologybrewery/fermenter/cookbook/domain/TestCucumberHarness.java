package org.technologybrewery.fermenter.cookbook.domain;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Test harness to run Gherkin Business Driven Development specifications.
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = "org.technologybrewery.fermenter.cookbook.domain", features="src/test/resources/specifications/unit", plugin="json:target/cucumber-html-reports/cucumber.json")
public class TestCucumberHarness {

}

package org.technologybrewery.fermenter.cookbook.local.referencing.domain;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Harness for all Cucumber unit tests.
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = {"org.technologybrewery.fermenter.cookbook.local.referencing.domain"}, 
    features = "src/test/resources/specifications", plugin = "json:target/cucumber-html-reports/cucumber.json", tags = "~@manual")
public class CucumberUnitTest {

}


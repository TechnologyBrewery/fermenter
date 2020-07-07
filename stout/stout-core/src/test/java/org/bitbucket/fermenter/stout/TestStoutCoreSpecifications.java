package org.bitbucket.fermenter.stout;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Test harness to run Gherkin Business Driven Development specifications.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/specifications", plugin = "json:target/cucumber-html-reports/cucumber.json")
public class TestStoutCoreSpecifications {

}
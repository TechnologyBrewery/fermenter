package org.bitbucket.fermenter.stout.content;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "org.bitbucket.fermenter.stout.content", features = "src/test/resources/specifications", plugin = "json:target/cucumber-html-reports/cucumber.json")
public class TestRunner {

}

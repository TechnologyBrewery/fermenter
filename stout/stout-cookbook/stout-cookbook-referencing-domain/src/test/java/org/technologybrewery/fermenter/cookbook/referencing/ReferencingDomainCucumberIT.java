package org.technologybrewery.fermenter.cookbook.referencing;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.runtime.arquillian.CukeSpace;

@RunWith(CukeSpace.class)
@CucumberOptions(glue = "org.technologybrewery.fermenter.cookbook.referencing", features = "src/test/resources/specifications")
@SpringClientConfiguration({ "application-test-context.xml" })
public class ReferencingDomainCucumberIT extends RunTestsOutsideOfArquillianWar {

}

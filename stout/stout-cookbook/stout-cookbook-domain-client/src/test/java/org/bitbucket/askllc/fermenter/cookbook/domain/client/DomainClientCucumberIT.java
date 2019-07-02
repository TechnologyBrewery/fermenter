package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.runtime.arquillian.CukeSpace;

@RunWith(CukeSpace.class)
@CucumberOptions(glue = "org.bitbucket.askllc.fermenter.cookbook.domain.client", features = "src/test/resources/specifications/it")
@SpringClientConfiguration({ "application-test-context.xml" })
public class DomainClientCucumberIT extends RunTestsOutsideOfArquillianWar {

}

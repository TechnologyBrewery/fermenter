package org.bitbucket.askllc.fermenter.cookbook.referencing;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.runtime.arquillian.CukeSpace;

@RunWith(CukeSpace.class)
@CucumberOptions(glue = "org.bitbucket.askllc.fermenter.cookbook.referencing", features = "src/test/resources/specifications")
public class ReferencingDomainCucumberIT extends RunTestsOutsideOfArquillianWar {

}

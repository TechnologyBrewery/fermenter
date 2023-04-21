package org.technologybrewery.fermenter.cookbook.referencing;

import org.technologybrewery.fermenter.cookbook.domain.AbstractArquillianTestSupport;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

@ArquillianSuiteDeployment
public class RunTestsOutsideOfArquillianWar extends AbstractArquillianTestSupport {
	
	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return AbstractArquillianTestSupport.createDeployment();
	}
	
}

package org.technologybrewery.fermenter.cookbook.domain;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

@ArquillianSuiteDeployment
public class RunTestsWithinArquillianWar extends AbstractArquillianTestSupport {

	@Deployment()
    public static WebArchive createDeployment() {
		return AbstractArquillianTestSupport.createDeployment();
	}

}

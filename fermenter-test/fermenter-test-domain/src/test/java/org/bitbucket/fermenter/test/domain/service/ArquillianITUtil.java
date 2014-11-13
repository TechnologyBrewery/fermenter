package org.bitbucket.fermenter.test.domain.service;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public final class ArquillianITUtil {

	private ArquillianITUtil() {
	}

	public static Archive<?> createFermenterTestDomainDeployment() {
		File[] mavenDependencies = Maven.configureResolver().workOffline()
				.loadPomFromFile("./pom.xml", "integration-test").importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();
		WebArchive war = ShrinkWrap.create(WebArchive.class, "maintenance-service-integration-test.war");
		war.addAsLibraries(mavenDependencies);
		war.addClass(TestUtils.class);
		war.addPackages(true, "org.bitbucket.fermenter.test.domain");
		war.addAsWebInfResource(new File("./src/generated/resources/META-INF/beans.xml"));
		war.merge(
				ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
						.importDirectory("src/generated/resources").as(GenericArchive.class), "WEB-INF/classes",
				Filters.includeAll());
		war.addAsWebInfResource(new File("./src/test/resources/web.xml"));
		war.addAsWebInfResource(new File("./src/test/resources/jboss-deployment-structure.xml"));
		war.addAsWebInfResource(new File("./src/test/resources/jbossas-ds.xml"));

		return war;
	}
}

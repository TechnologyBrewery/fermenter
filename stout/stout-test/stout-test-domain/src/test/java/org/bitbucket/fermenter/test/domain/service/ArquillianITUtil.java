package org.bitbucket.fermenter.test.domain.service;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public final class ArquillianITUtil {

	private ArquillianITUtil() {
	}

	public static Archive<?> createFermenterTestDomainDeployment() {
		File[] mavenDependencies  = null;
		try {
			ConfigurableMavenResolverSystem resolver = Maven.configureResolver();
			//resolver = resolver.workOffline();
			PomEquippedResolveStage stage = resolver.loadPomFromFile("./pom.xml", "integration-test");
			mavenDependencies = stage.importRuntimeAndTestDependencies().resolve()
					.withTransitivity().asFile();
			
		} catch (Exception e) {
			throw new RuntimeException("Problem resolving from Arquillian Maven!", e);
			
		}
		
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

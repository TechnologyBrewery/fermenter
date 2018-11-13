package org.bitbucket.askllc.fermenter.cookbook.domain;

import java.io.File;

import javax.ws.rs.client.WebTarget;

import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.JacksonObjectMapperResteasyProvider;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public abstract class AbstractArquillianTestSupport {

    public static WebArchive createDeployment() {
        File[] mavenDependencies = Maven.configureResolver().loadPomFromFile("pom.xml", "integration-test")
                .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class, "cookbook-domain.war");
        war.addAsLibraries(mavenDependencies);
        war.addPackages(true, "org.bitbucket.askllc.fermenter.cookbook.domain");
        war.addClass(TestUtils.class);
        File generatedResources = new File("./src/generated/resources");
        if (generatedResources.exists()) {
            war.merge(
                ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory("src/generated/resources").as(GenericArchive.class),
                "WEB-INF/classes", Filters.include("^.*\\.properties$"));
        }
        war.addAsWebInfResource("base-stout-cookbook-domain-application-context.xml");
        war.addAsWebInfResource("stout-cookbook-domain-application-context.xml");
        war.addAsWebInfResource("h2-tomcat-ds-context.xml", "context.xml");
        war.addAsWebResource("log4j2.xml");
        war.setWebXML("web.xml");
        return war;
    }

	/**
	 * Registers the given JAX-RS {@link WebTarget} (typically injected into a test via
	 * {@link ArquillianResteasyResource}) with the generated {@link JacksonObjectMapperResteasyProvider} such that the
	 * (de)serialization of generated business objects is transparently supported.
	 * 
	 * @param webTarget
	 * @return
	 */
	protected <T extends WebTarget> T initWebTarget(T webTarget) {
	    webTarget.register(new AddHeadersRequestFilter("testUser"));
		return (T) webTarget.register(JacksonObjectMapperResteasyProvider.class);
	}
}

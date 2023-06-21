package org.technologybrewery.fermenter.mda;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.technologybrewery.fermenter.mda.element.Target;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;

import java.io.File;

public class GenerationContextSteps {
    private MojoTestCaseWrapper mojoTestCase = new MojoTestCaseWrapper();
    protected File mavenProjectBaseDir;
    protected GenerateSourcesMojo generateSourcesMojo;
    protected GenerationContext context;

    @Before("@generationContext")
    public void configureMavenPluginTestHarness() throws Exception {
        mojoTestCase.configurePluginTestHarness();
    }

    @After("@generationContext")
    public void tearDownMavenPluginTestHarness() throws Exception {
        mojoTestCase.tearDownPluginTestHarness();
    }

    /*
     This is an approximation of using a submodule whose base directory is different from the base directory set in test maven session. While not an exact replica, it highlights that we aren't giving a local base directory from the loaded pom.
     */
    @Given("^generation in a Maven submodule$")
    public void generation_in_a_Maven_submodule() throws Throwable {
        mavenProjectBaseDir = new File("src/test/resources/plugin-testing-harness-pom-files/java-default-config");
        generateSourcesMojo = (GenerateSourcesMojo) mojoTestCase.lookupConfiguredMojo(new File(mavenProjectBaseDir, "pom.xml"), "generate-sources");
        generateSourcesMojo.updateMojoConfigsBasedOnLanguage();
        generateSourcesMojo.validateMojoConfigs();
    }

    @When("^the generation context is created$")
    public void the_generation_context_is_created() throws Throwable {
        Target target = new Target();
        context = generateSourcesMojo.createGenerationContext(target);
    }

    @Then("^access to the root module's base directory is available$")
    public void access_to_the_root_module_s_base_directory_is_available() throws Throwable {
        Assert.assertNotNull(context.getExecutionRootDirectory());
    }

}

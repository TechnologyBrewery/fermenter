package org.technologybrewery.fermenter.mda;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.technologybrewery.fermenter.mda.element.CommonSteps;
import org.technologybrewery.fermenter.mda.element.Target;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenerationContextSteps {
    private MojoTestCaseWrapper mojoTestCase = new MojoTestCaseWrapper();
    protected File mavenProjectBaseDir;
    protected GenerateSourcesMojo generateSourcesMojo;
    protected GenerationContext context;

    @Before("@generationContext")
    public void configureMavenPluginTestHarness() throws Exception {
        mojoTestCase.configurePluginTestHarness();
        CommonSteps.performCommonBeforeTasks();
    }

    @After("@generationContext")
    public void tearDownMavenPluginTestHarness() throws Exception {
        mojoTestCase.tearDownPluginTestHarness();
        CommonSteps.performCommonAfterTasks();
    }

    /*
     This is an approximation of using a submodule whose base directory is different from the base directory set in test maven session. While not an exact replica, it highlights that we aren't giving a local base directory from the loaded pom.
     */
    @Given("generation in a Maven submodule")
    public void generation_in_a_Maven_submodule() throws Throwable {
        mavenProjectBaseDir = new File("src/test/resources/plugin-testing-harness-pom-files/java-default-config");
        generateSourcesMojo = (GenerateSourcesMojo) mojoTestCase.lookupConfiguredMojo(new File(mavenProjectBaseDir, "pom.xml"), "generate-sources");
        generateSourcesMojo.updateMojoConfigsBasedOnLanguage();
        generateSourcesMojo.validateMojoConfigs();
        generateSourcesMojo.buildModelInstanceRepository();
    }

    @When("the generation context is created")
    public void the_generation_context_is_created() throws Throwable {
        Target target = new Target();
        context = generateSourcesMojo.createGenerationContext(target);
    }

    @Then("access to the root module's base directory is available")
    public void access_to_the_root_module_s_base_directory_is_available() throws Throwable {
        assertNotNull(context.getExecutionRootDirectory());
    }

    @Then("access to the root module's artifact ID is available")
    public void access_to_the_root_module_s_artifact_ID_is_available() throws Throwable {
        assertEquals(context.getRootArtifactId(),"java-default-config");
    }

    @Then("access to the model instance repository implementation is available")
    public void access_to_the_model_instance_repository_implementation_is_available() throws Throwable {
        assertNotNull(context.getModelInstanceRepository());
    }
}

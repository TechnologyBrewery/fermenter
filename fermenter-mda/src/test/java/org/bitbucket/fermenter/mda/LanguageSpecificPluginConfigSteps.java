package org.bitbucket.fermenter.mda;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

public class LanguageSpecificPluginConfigSteps {

    private MojoTestCaseWrapper mojoTestCase = new MojoTestCaseWrapper();

    protected File mavenProjectBaseDir;

    protected GenerateSourcesMojo generateSourcesMojo;

    private boolean validationFailure;

    @Before("@languageSpecificPluginConfig")
    public void configureMavenPluginTestHarness() throws Exception {
        mojoTestCase.configurePluginTestHarness();
    }

    @After("@languageSpecificPluginConfig")
    public void tearDownMavenPluginTestHarness() throws Exception {
        mojoTestCase.tearDownPluginTestHarness();
    }

    @Given("^a Maven project named \"([^\"]*)\" with Fermenter Maven plugin configuration$")
    public void a_Maven_project_named_with_Fermenter_Maven_plugin_configuration(String projectName) throws Throwable {
        mavenProjectBaseDir = new File("src/test/resources/plugin-testing-harness-pom-files/", projectName);
    }

    @When("^the project's pom\\.xml is processed$")
    public void the_project_s_pom_xml_is_processed() throws Throwable {
        generateSourcesMojo = (GenerateSourcesMojo) mojoTestCase.lookupConfiguredMojo(new File(mavenProjectBaseDir, "pom.xml"), "generate-sources");
        generateSourcesMojo.updateMojoConfigsBasedOnLanguage();
    }

    @When("^the Fermenter Maven plugin configuration is validated$")
    public void the_Fermenter_Maven_plugin_configuration_is_validated() throws Throwable {
        generateSourcesMojo.validateMojoConfigs();
    }

    @Then("^Fermenter Maven plugin uses \"([^\"]*)\" as the main source root folder, \"([^\"]*)\" as the generated source root folder, and \"([^\"]*)\" as the base package$")
    public void fermenter_Maven_plugin_uses_as_the_main_source_root_folder_as_the_generated_source_root_folder_and_as_the_base_package(String mainSourceRoot, String generatedSourceRoot, String namespace) throws Throwable {
        validatePluginConfig(generateSourcesMojo.getMainSourceRoot(),
            new File(mavenProjectBaseDir, mainSourceRoot), "mainSourceRoot");
        validatePluginConfig(generateSourcesMojo.getGeneratedSourceRoot(),
            new File(mavenProjectBaseDir, generatedSourceRoot), "generatedSourceRoot");
        Assert.assertEquals("Namespace/base package did not match the expected value", namespace,
            generateSourcesMojo.getBasePackage());
    }

    @Then("^Fermenter Maven plugin uses \"([^\"]*)\" as the local metadata root folder$")
    public void fermenter_Maven_plugin_uses_as_the_local_metadata_root_folder(String localMetadataRoot) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        validatePluginConfig(generateSourcesMojo.getLocalMetadataRoot(),
            new File(mavenProjectBaseDir, localMetadataRoot), "localMetadataRoot");
    }

    protected void validatePluginConfig(File actualPluginConfigFile, File expectedPluginConfigFile, String configDescription) throws IOException {
        Assert.assertNotNull(String.format("%s was unexpectedly null", configDescription), actualPluginConfigFile);
        Assert.assertEquals(String.format("%s path did not equal expected value", configDescription),
            expectedPluginConfigFile.getCanonicalPath(), actualPluginConfigFile.getCanonicalPath());
    }

    @When("^the invalid Fermenter Maven plugin configuration is attempted to be validated$")
    public void the_invalid_Fermenter_Maven_plugin_configuration_is_attempted_to_be_validated() throws Throwable {
        try {
            generateSourcesMojo.validateMojoConfigs();
            validationFailure = false;
        } catch (MojoExecutionException e) {
            validationFailure = true;
        }
    }

    @Then("^a validation error is detected$")
    public void a_validation_error_is_detected() throws Throwable {
        Assert.assertEquals("No validation error was detected", true, validationFailure);
    }

}

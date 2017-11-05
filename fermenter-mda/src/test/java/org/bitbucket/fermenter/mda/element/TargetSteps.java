package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.util.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TargetSteps {

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected File targetFile;
    protected Target target;
    protected GenerationException encounteredException;

    @Given("^a target described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_target_described_by(String name, String generator, String templateName, String outputFile,
            String overwritable) throws Throwable {
        Target newTarget = new Target();
        if (StringUtils.isNotBlank(name)) {
            newTarget.setName(name);
        }
        if (StringUtils.isNotBlank(generator)) {
            newTarget.setGenerator(generator);
        }
        if (StringUtils.isNotBlank(templateName)) {
            newTarget.setTemplateName(templateName);
        }
        if (StringUtils.isNotBlank(outputFile)) {
            newTarget.setOutputFile(outputFile);
        }
        if (StringUtils.isNotBlank(overwritable)) {
            newTarget.setOverwritable(Boolean.valueOf(overwritable));
        }

        targetFile = new File(FileUtils.getTempDirectory(), templateName + "-target.json");
        objectMapper.writeValue(targetFile, newTarget);
        assertTrue("Target not written to file!", targetFile.exists());

    }

    @When("^targets are read$")
    public void targets_are_read() throws Throwable {
        encounteredException = null;
        
        try {
            target = JsonUtils.readAndValidateJson(targetFile, Target.class);
            assertNotNull("Could not read target file!", target);
            
        } catch (GenerationException e) {
            encounteredException = e;
        }
        

    }

    @Then("^a valid target is available can be looked up name \"([^\"]*)\"$")
    public void a_valid_target_is_available_can_be_looked_up_name(String expectedName) throws Throwable {
        assertEquals(expectedName, target.getName());
    }

    @Then("^the generator throws an exception about invalid metadata$")
    public void the_generator_throws_an_exception_about_invalid_metadata() throws Throwable {
        assertNotNull("A GenerationException should have been thrown!", encounteredException);
    }
    
}

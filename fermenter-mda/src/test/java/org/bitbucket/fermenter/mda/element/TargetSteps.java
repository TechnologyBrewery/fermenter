package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.bitbucket.fermenter.mda.util.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TargetSteps {

	protected ObjectMapper objectMapper = new ObjectMapper();
	protected File targetFile;
	protected Target target;

	@Given("^a target described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
	public void a_target_described_by(String name, String generator, String templateName, String outputFile,
			String overwriteable) throws Throwable {
		Target newTarget = new Target();
		newTarget.setName(name);
		newTarget.setGenerator(generator);
		newTarget.setTemplateName(templateName);
		newTarget.setOutputFile(outputFile);

		targetFile = new File(FileUtils.getTempDirectory(), templateName + "-target.json");
		objectMapper.writeValue(targetFile, newTarget);
		assertTrue("Target not written to file!", targetFile.exists());

	}

	@When("^targets are read$")
	public void targets_are_read() throws Throwable {
		target = JsonUtils.readAndValidateJson(targetFile, Target.class);
		assertNotNull("Could not read target file!", target);

	}

	@Then("^a valid target is available can be looked up name \"([^\"]*)\"$")
	public void a_valid_target_is_available_can_be_looked_up_name(String expectedName) throws Throwable {
		assertEquals(expectedName, target.getName());
	}

}

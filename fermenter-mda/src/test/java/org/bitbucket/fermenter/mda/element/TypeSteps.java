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

public class TypeSteps {

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected File typeFile;
    protected Type type;
    protected GenerationException encounteredException;

    @Given("^a type described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_type_described_by(String name, String fullyQualifiedImplementation, String shortImplementation) throws Throwable {
        Type newType = new Type();
        if (StringUtils.isNotBlank(name)) {
            newType.setName(name);
        }
        if (StringUtils.isNotBlank(fullyQualifiedImplementation)) {
            newType.setFullyQualifiedImplementation(fullyQualifiedImplementation);
        }
        if (StringUtils.isNotBlank(shortImplementation)) {
            newType.setShortImplementation(shortImplementation);
        }

        typeFile = new File(FileUtils.getTempDirectory(), name + "-types.json");
        objectMapper.writeValue(typeFile, newType);
        assertTrue("Type not written to file!", typeFile.exists());

    }

    @When("^types are read$")
    public void types_are_read() throws Throwable {
        encounteredException = null;

        try {
            type = JsonUtils.readAndValidateJson(typeFile, Type.class);
            assertNotNull("Could not read target file!", type);

        } catch (GenerationException e) {
            encounteredException = e;
        }

    }

    @Then("^a valid type is available can be looked up name \"([^\"]*)\"$")
    public void a_valid_type_is_available_can_be_looked_up_name(String expectedName) throws Throwable {
        assertEquals("Unexpected name encountered!", expectedName, type.getName());
    }
    

    @Then("^the generator throws an exception about invalid type metadata$")
    public void the_generator_throws_an_exception_about_invalid_type_metadata() throws Throwable {
        assertNotNull("A GenerationException should have been thrown!", encounteredException);
    }    

}

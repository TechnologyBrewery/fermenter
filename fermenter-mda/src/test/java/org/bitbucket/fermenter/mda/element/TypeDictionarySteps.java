package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.util.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TypeDictionarySteps {

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected File typeDictionaryFile;
    protected TypeDictionary typeDictionary;
    protected GenerationException encounteredException;

    @After("@typeDictionary")
    public void cleanUp() {
        typeDictionary = null;
        typeDictionaryFile = null;
        encounteredException = null;
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_dictionary_type_described_by(String name, String type) throws Throwable {
        createDictionaryType(name, type, null);
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_dictionary_type_described_by(String name, String type, List<String> formats) throws Throwable {
        createDictionaryType(name, type, formats);

    }

    @When("^dictionary type file is read$")
    public void dictionary_type_file_is_read() throws Throwable {
        encounteredException = null;
        
        try {
            typeDictionary = JsonUtils.readAndValidateJson(typeDictionaryFile, TypeDictionary.class);
            assertNotNull("Could not read type dictionary file!", typeDictionary);
            
            if (null != typeDictionary.getFormat()) {
                validateFormat(typeDictionary);
            }
            
        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^a valid dictionary type is available and can be looked up by name \"([^\"]*)\"$")
    public void a_valid_dictionary_type_is_available_and_can_be_looked_up_by_name(String expectedName)
            throws Throwable {
        assertEquals(expectedName, typeDictionary.getName());
    }

    @Then("^the generator throws an exception about invalid dictionary type metadata$")
    public void the_generator_throws_an_exception_about_invalid_dictionary_type_metadata() throws Throwable {
        assertNotNull("A GenerationException should have been thrown!", encounteredException);
    }

    /**
     * Helper method to create the type dictionary for fields available.
     * 
     * @param name
     *            The name of the dictionary type
     * @param type
     *            The type of the dictionary type
     * @param formats
     *            The regex formats of the type
     */
    private void createDictionaryType(String name, String type, List<String> formats)
            throws Throwable {
        TypeDictionary newTypeDictionary = new TypeDictionary();
        
        if (StringUtils.isNotBlank(name)) {
            newTypeDictionary.setName(name);
        }
        if (StringUtils.isNotBlank(type)) {
            newTypeDictionary.setType(type);
        }
        if (null != formats) {
            newTypeDictionary.setFormat(formats);
        }
        
        typeDictionaryFile = new File(FileUtils.getTempDirectory(), "typeDictionary.json");
        objectMapper.writeValue(typeDictionaryFile, newTypeDictionary);
        assertTrue("Dictionary type not written to file!", typeDictionaryFile.exists());
    }

    /**
     * Checks to make sure we have at least a format specified.
     * 
     * @param typeDictionary
     *            The dictionary type read in to validate against
     */
    private void validateFormat(TypeDictionary typeDictionary) {
        
        for (String format: typeDictionary.getFormat()) {
            if (StringUtils.isNotBlank(format)) {
                return;
            }
        }
        encounteredException = new GenerationException();
    }
}

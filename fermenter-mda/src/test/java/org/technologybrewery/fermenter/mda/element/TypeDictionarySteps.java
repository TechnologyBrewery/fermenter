package org.technologybrewery.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.generator.GenerationException;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.MetamodelConfig;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceUrl;
import org.technologybrewery.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.technologybrewery.fermenter.mda.metamodel.element.DictionaryType;
import org.technologybrewery.fermenter.mda.metamodel.element.DictionaryTypeElement;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TypeDictionarySteps {
    
    private static final MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);
    
    private MessageTracker messageTracker = MessageTracker.getInstance();
    private ObjectMapper objectMapper = new ObjectMapper();
    private File typeDictionaryFile;
    private GenerationException encounteredException;
    private DefaultModelInstanceRepository metadataRepo;
    private File dictionaryTypeDirectory = new File("target/temp-metadata", config.getDictionaryTypesRelativePath());

    // Also uses CommonSteps for setup and tear down    
    
    @After("@typeDictionary")
    public void cleanUp() {
        typeDictionaryFile = null;
        encounteredException = null;
        messageTracker.clear();
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_dictionary_type_described_by(String name, String type) throws Throwable {
        createDictionaryType(name, type, null, null, null);
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_dictionary_type_described_by(String name, String type, List<String> formats) throws Throwable {
        createDictionaryType(name, type, null, null, formats);
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void a_dictionary_type_described_by(String name, String type, String minLength, String maxLength)
            throws Throwable {
        createDictionaryType(name, type, minLength, maxLength, null);
    }

    @Given("^a dictionary type described by \"([^\"]*)\", \"([^\"]*)\" with a blank format$")
    public void a_dictionary_type_described_by_with_a_null_format(String name, String type) throws Throwable {
        List<String> emptyFormat = new ArrayList<>();

        // simulate blank format here because cucumber strips empty spaces from input values
        emptyFormat.add("       ");

        createDictionaryType(name, type, null, null, emptyFormat);
    }

    @Given("^dictionary files that are described by the following different names$")
    public void dictionary_files_that_are_described_by_the_following_different_names(List<String> inputTypes)
            throws Throwable {
        createDictionaryTypes(inputTypes);
    }

    @When("^dictionary type file is read$")
    public void dictionary_type_file_is_read() throws Throwable {
        encounteredException = null;
        try {
            ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
            config.setArtifactId("fermenter-mda");
            Map<String, ModelInstanceUrl> metadataUrlMap = config.getMetamodelInstanceLocations();

            File sourceParentDirectory = dictionaryTypeDirectory.getParentFile();
            URI sourceDirectoryURI = sourceParentDirectory.toURI();
            metadataUrlMap.put("fermenter-mda", new ModelInstanceUrl("fermenter-mda", sourceDirectoryURI.toString()));
            metadataRepo = new DefaultModelInstanceRepository(config);
            metadataRepo.load();
            metadataRepo.validate();
        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^a valid dictionary type is available and can be looked up by name \"([^\"]*)\"$")
    public void a_valid_dictionary_type_is_available_and_can_be_looked_up_by_name(String name) throws Throwable {
        DictionaryType loadedDictionaryType = metadataRepo.getDictionaryType(name);
        assertEquals("Unexpected dictionary type name!", name, loadedDictionaryType.getName());
    }

    @Then("^the generator throws an exception about invalid dictionary type metadata$")
    public void the_generator_throws_an_exception_about_invalid_dictionary_type_metadata() throws Throwable {
        assertNotNull("A GenerationException should have been thrown!", encounteredException);
    }

    @Then("^there should be five dictionary types available$")
    public void there_should_be_five_dictionary_types_available() throws Throwable {
        assertEquals("Dictionary size doesn't match the number of types available!", 5,
                metadataRepo.getDictionaryTypes().size());
    }

    /**
     * Helper method to create the type dictionary for fields available.
     * 
     * @param name
     *            The name of the dictionary type.
     * @param type
     *            The type of the dictionary type.
     * @param minLength
     *            The minimal length of the dictionary type.
     * @param maxLength
     *            The maximal length of the dictionary type.
     * @param formats
     *            The regex formats of the type.
     */
    private void createDictionaryType(String name, String type, String minLength, String maxLength,
            List<String> formats) throws Throwable {
        DictionaryTypeElement newDictionaryType = new DictionaryTypeElement();
        
        // setup the type dictionary for read and validation
        if (StringUtils.isNotBlank(name)) {
            newDictionaryType.setName(name);
        }
        if (StringUtils.isNotBlank(type)) {
            newDictionaryType.setType(type);
        }
        if (StringUtils.isNotBlank(minLength)) {
            newDictionaryType.setMinLength(Integer.valueOf(minLength));
        }
        if (StringUtils.isNotBlank(maxLength)) {
            newDictionaryType.setMaxLength(Integer.valueOf(maxLength));
        }
        if (null != formats) {
            newDictionaryType.setFormats(formats);
        }

        // make sure the directory for saving the json exists
        dictionaryTypeDirectory.mkdirs();

        // write the json file to the dictionary location
        typeDictionaryFile = new File(dictionaryTypeDirectory, name + ".json");
        objectMapper.writeValue(typeDictionaryFile, newDictionaryType);
        assertTrue("Dictionary type not written to file!", typeDictionaryFile.exists());
    }

    /**
     * Helper method to create a list of types available.
     * 
     * @param names
     *            The names of the dictionary types to create.
     */
    private void createDictionaryTypes(List<String> names) throws Throwable {

        // make sure the directory for saving the json exists
        dictionaryTypeDirectory.mkdirs();

        // clear out json files from previous tests
        for (File jsonFile : dictionaryTypeDirectory.listFiles()) {
            jsonFile.delete();
        }

        for (String name : names) {
            DictionaryTypeElement newDictionaryType = new DictionaryTypeElement();
            newDictionaryType.setName(name);
            newDictionaryType.setType(RandomStringUtils.randomAlphanumeric(5));

            typeDictionaryFile = new File(dictionaryTypeDirectory, name + ".json");
            objectMapper.writeValue(typeDictionaryFile, newDictionaryType);
            assertTrue("Dictionary type not written to file!", typeDictionaryFile.exists());
        }
    }
}

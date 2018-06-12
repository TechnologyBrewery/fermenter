package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.MetadataUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EnumerationSteps {

    private ObjectMapper objectMapper = new ObjectMapper();
    private MessageTracker messageTracker = MessageTracker.getInstance();
    private File enumerationsDirectory = new File("target/temp-metadata", "enumerations");

    private String currentBasePackage;
    
    private File enumerationFile;
    private Enumeration loadedEnumeration;
    protected GenerationException encounteredException;
    protected DefaultModelInstanceRepository metadataRepo;

    @After("@enumeration")
    public void cleanUp() {
        if (enumerationFile != null) {
            enumerationFile.delete();
            enumerationFile = null;
        }

        loadedEnumeration = null;

        messageTracker.clear();

        enumerationsDirectory.delete();
        
        currentBasePackage = null;
    }

    @Given("^an enumeration named \"([^\"]*)\" in \"([^\"]*)\" and enum constants \"([^\"]*)\"$")
    public void an_enumeration_named_in_and_enum_constants(String name, String packageValue, List<String> constants)
            throws Throwable {
        createEnumerations(name, packageValue, constants, null);
    }

    private void createEnumerations(String name, String packageValue, List<String> constantNames,
            List<String> constantValues) throws IOException, JsonGenerationException, JsonMappingException {
        EnumerationElement enumeration = new EnumerationElement();
        enumeration.setName(name);
        enumeration.setPackage(packageValue);

        int index = 0;
        for (String constant : constantNames) {
            EnumElement newEnumConstant = new EnumElement();
            newEnumConstant.setName(constant);
            if (constantValues != null) {
                newEnumConstant.setValue(Integer.valueOf(constantValues.get(index)));
            }
            enumeration.addEnums(newEnumConstant);

            index++;
        }

        enumerationsDirectory.mkdirs();
        enumerationFile = new File(enumerationsDirectory, name + ".json");
        objectMapper.writeValue(enumerationFile, enumeration);
        assertTrue("Enumeration not written to file!", enumerationFile.exists());
        
        currentBasePackage = packageValue;
    }

    @Given("^an enumeration named \"([^\"]*)\" in \"([^\"]*)\" and enum constant names \"([^\"]*)\" and values \"([^\"]*)\"$")
    public void an_enumeration_named_in_and_enum_constant_names_and_values(String name, String packageValue,
            List<String> constantNames, List<String> constantValues) throws Throwable {
        createEnumerations(name, packageValue, constantNames, constantValues);
    }

    @When("^enumerations a read$")
    public void enumerations_a_read() throws Throwable {
        encounteredException = null;

        try {
            ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
            config.setCurrentApplicationName("fermenter-mda");
            config.setBasePackage(currentBasePackage);
            Map<String, MetadataUrl> metadataUrlMap = config.getMetamodelInstanceLocations();
            metadataUrlMap.put("fermenter-mda", new MetadataUrl("fermenter-mda", enumerationsDirectory.getParentFile().toURI().toString()));

            metadataRepo = new DefaultModelInstanceRepository(config);
            metadataRepo.load();
            metadataRepo.validate();

        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^an enumeration metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the enum constants \"([^\"]*)\"$")
    public void an_enumeration_metamodel_instance_is_returned_for_the_name_in_with_the_enum_constants(String name,
            String packageName, List<String> constants) throws Throwable {
        validateLoadedConstants(name, packageName, constants, null);
    }

    @Then("^NO enumeration metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\"$")
    public void no_enumeration_metamodel_instance_is_returned_for_the_name_in(String name, String packageName)
            throws Throwable {
        if (encounteredException != null) {
            throw encounteredException;
        }

        Map<String, Enumeration> packageEnumerations = metadataRepo.getEnumerations(packageName);
        loadedEnumeration = (packageEnumerations != null) ? packageEnumerations.get(name) : null;
        assertNull(loadedEnumeration);
    }

    @Then("^an enumeration metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the enum constants \"([^\"]*)\" and matching values \"([^\"]*)\"$")
    public void an_enumeration_metamodel_instance_is_returned_for_the_name_in_with_the_enum_constants_and_matching_values(
            String name, String packageName, List<String> constantNames, List<String> constantValues) throws Throwable {
        validateLoadedConstants(name, packageName, constantNames, constantValues);
    }

    @Then("^the enumeration is of type \"([^\"]*)\"$")
    public void the_enumeration_is_of_type(String enumerationType) throws Throwable {
        if ("named".equalsIgnoreCase(enumerationType)) {
            assertTrue("Should have been a named enumeration!", loadedEnumeration.isNamed());
            assertFalse("Should have been a named enumeration!", loadedEnumeration.isValued());
            
        } else {
            assertFalse("Should have been a valued enumeration!", loadedEnumeration.isNamed());
            assertTrue("Should have been a valued enumeration!", loadedEnumeration.isValued());
            
        }
    }

    private void validateLoadedConstants(String name, String packageName, List<String> constantNames,
            List<String> constantValues) {
        if (encounteredException != null) {
            throw encounteredException;
        }

        loadedEnumeration = metadataRepo.getEnumerations(packageName).get(name);
        assertEquals("Unexpected enumeration name!", name, loadedEnumeration.getName());
        assertEquals("Unexpected enumeration package!", packageName, loadedEnumeration.getPackage());

        List<Enum> loadedConstants = loadedEnumeration.getEnums();
        assertEquals("Did not find the expected number of enum constants!", constantNames.size(),
                loadedConstants.size());
        Map<String, Enum> loadedConstantMap = loadedConstants.stream().collect(Collectors.toMap(Enum::getName, x -> x));

        int index = 0;
        for (String constant : constantNames) {
            Enum loadedConstantInstance = loadedConstantMap.get(constant);
            assertNotNull("Could not find enum constant " + constant + "!", loadedConstantMap.get(constant));
            if (constantValues != null) {
                assertEquals("Constant value unexpected!", Integer.valueOf(constantValues.get(index)),
                        loadedConstantInstance.getValue());
            }

            index++;
        }
    }

}

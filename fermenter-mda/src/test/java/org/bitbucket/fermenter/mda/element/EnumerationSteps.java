package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;
import org.bitbucket.fermenter.mda.util.JsonUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EnumerationSteps {

    private ObjectMapper objectMapper = new ObjectMapper();
    private MessageTracker messageTracker = MessageTracker.getInstance();
    
    private File enumerationFile;
    private EnumerationElement loadedEnumeration;
    protected GenerationException encounteredException;

    @After("@enumeration")
    public void cleanUp() {
        if (enumerationFile != null) {
            enumerationFile.delete();
            enumerationFile = null;
        }
        
        messageTracker.clear();
    }

    @Given("^an enumeration named \"([^\"]*)\" in \"([^\"]*)\" and enum constants \"([^\"]*)\"$")
    public void an_enumeration_named_in_and_enum_constants(String name, String packageValue, List<String> constants)
            throws Throwable {
        EnumerationElement enumeration = new EnumerationElement();
        enumeration.setName(name);
        enumeration.setPackage(packageValue);

        for (String constant : constants) {
            EnumElement newEnumConstant = new EnumElement();
            newEnumConstant.setName(constant);
            enumeration.addEnums(newEnumConstant);
        }

        enumerationFile = new File(FileUtils.getTempDirectory(), name + ".json");
        objectMapper.writeValue(enumerationFile, enumeration);
        assertTrue("Enumeration not written to file!", enumerationFile.exists());
    }

    @When("^enumerations a read$")
    public void enumerations_a_read() throws Throwable {
        encounteredException = null;

        try {
            loadedEnumeration = JsonUtils.readAndValidateJson(enumerationFile, EnumerationElement.class);
            assertNotNull("Could not read enumeration file!", loadedEnumeration);

        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^an enumeration metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the enum constants \"([^\"]*)\"$")
    public void an_enumeration_metamodel_instance_is_returned_for_the_name_in_with_the_enum_constants(String name,
            String packageName, List<String> constants) throws Throwable {
        if (encounteredException != null) {
            throw encounteredException;
        }

        assertEquals("Unexpected enumeration name!", name, loadedEnumeration.getName());
        assertEquals("Unexpected enumeration package!", packageName, loadedEnumeration.getPackage());

        List<Enum> loadedConstants = loadedEnumeration.getEnums();
        assertEquals("Did not find the expected number of enum constants!", constants.size(), loadedConstants.size());
        Map<String, Enum> loadedConstantMap = loadedConstants.stream().collect(Collectors.toMap(Enum::getName, x -> x));
        for (String constant : constants) {
            assertNotNull("Could not find enum constant " + constant + "!", loadedConstantMap.get(constant));
        }
    }

}

package org.technologybrewery.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.MetamodelConfig;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceUrl;
import org.technologybrewery.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.technologybrewery.fermenter.mda.metamodel.element.Message;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageElement;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroup;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroupElement;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MessageGroupSteps {

    private static final MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private MessageTracker messageTracker = MessageTracker.getInstance();
    private File messageGroupsDirectory = new File("target/temp-metadata", config.getMessageGroupsRelativePath());

    private String currentBasePackage;

    private File messageGroupFile;
    protected GenerationException encounteredException;
    protected DefaultModelInstanceRepository metamodelRepo;
    
    // Also uses CommonSteps for setup and tear down
    
    @After("@messageGroup")
    public void cleanUp() {
        messageTracker.clear();
    }

    @Given("^a message group named \"([^\"]*)\" in \"([^\"]*)\" and the messages:$")
    public void a_message_group_named_in_and_the_messages(String name, String packageValue,
            List<MessageElement> messages) throws Throwable {
        MessageGroupElement messageGroup = new MessageGroupElement();
        messageGroup.setName(name);
        messageGroup.setPackage(packageValue);
        messageGroup.getMessages().addAll(messages);

        loadMessageGroup(name, packageValue, messageGroup);
    }

    @Given("^a message group named \"([^\"]*)\" in \"([^\"]*)\" and a least one valid message$")
    public void a_message_group_named_in_and_a_least_one_valid_message(String name, String packageValue)
            throws Throwable {
        MessageGroupElement messageGroup = new MessageGroupElement();
        messageGroup.setName(name);
        messageGroup.setPackage(packageValue);

        loadMessageGroup(name, packageValue, messageGroup);
    }

    @When("^message groups are read$")
    public void message_groups_are_read() throws Throwable {
        try {
            ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
            config.setArtifactId("fermenter-mda");
            config.setBasePackage(currentBasePackage);
            Map<String, ModelInstanceUrl> metadataUrlMap = config.getMetamodelInstanceLocations();
            metadataUrlMap.put("fermenter-mda",
                    new ModelInstanceUrl("fermenter-mda", messageGroupsDirectory.getParentFile().toURI().toString()));

            metamodelRepo = new DefaultModelInstanceRepository(config);
            metamodelRepo.load();
            metamodelRepo.validate();

        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^a meessage group is returned for the name \"([^\"]*)\" in \"([^\"]*)\" and the messages:$")
    public void a_meessage_group_is_returned_for_the_name_in_and_the_messages(String expectedName,
            String expectedPackageValue, List<MessageElement> expectedMessages) throws Throwable {
        MessageGroup messageGroup = metamodelRepo.getMessageGroup(expectedPackageValue, expectedName);
        assertNotNull("Could not find expected message group!", messageGroup);

        Map<String, Message> messagesByKey = Maps.uniqueIndex(messageGroup.getMessages(), Message::getName);

        for (Message expectedMessage : expectedMessages) {
            Message foundMessage = messagesByKey.get(expectedMessage.getName());
            assertNotNull("Could not find expected message!", foundMessage);
            assertEquals("Unexpected message text encountered!", expectedMessage.getText(), foundMessage.getText());
        }

    }

    @Then("^the generator throws an exception about a invalid mssage group$")
    public void the_generator_throws_an_exception_about_a_invalid_mssage_group() throws Throwable {
        assertNotNull("Expected to have encountered an error!", encounteredException);
    }

    protected void loadMessageGroup(String name, String packageValue, MessageGroupElement messageGroup)
            throws IOException, JsonGenerationException, JsonMappingException {

        messageGroupFile = new File(messageGroupsDirectory, name + ".json");
        objectMapper.writeValue(messageGroupFile, messageGroup);
        assertTrue("Message group not written to file!", messageGroupFile.exists());

        currentBasePackage = packageValue;
    }

}

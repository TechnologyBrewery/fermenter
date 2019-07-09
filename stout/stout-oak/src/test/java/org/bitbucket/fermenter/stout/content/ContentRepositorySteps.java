package org.bitbucket.fermenter.stout.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml" })
public class ContentRepositorySteps {

    @Inject
    private ContentRepository contentRepository;

    private String folderName;
    private String fileName;
    private String fileContent;

    @Given("^content that should be saved in \"([^\"]*)\" as \"([^\"]*)\"$")
    public void content_that_should_be_saved_in_as(String folderName, String fileName) throws Throwable {
        this.folderName = folderName;
        this.fileName = fileName;
        this.fileContent = RandomStringUtils.randomAlphabetic(2000);
    }

    @Given("^content that has been saved in \"([^\"]*)\" as \"([^\"]*)\"$")
    public void content_that_has_been_saved_in_as(String folderName, String fileName) throws Throwable {
        this.folderName = folderName;
        this.fileName = fileName;
        this.fileContent = RandomStringUtils.randomAlphabetic(2000);

        InputStream is = new ByteArrayInputStream(fileContent.getBytes());
        contentRepository.saveFile(folderName, fileName, is);
    }

    @When("^the content is persisted$")
    public void the_content_is_persisted() throws Throwable {
        assertNotNull("ContentRepository is not available (likely that injection failed)!", contentRepository);

        InputStream is = new ByteArrayInputStream(fileContent.getBytes());
        contentRepository.saveFile(folderName, fileName, is);
    }

    @When("^the content is deleted$")
    public void the_content_is_deleted() throws Throwable {
        contentRepository.deleteFile(folderName, fileName);
    }

    @Then("^the content can be retrieved from \"([^\"]*)\" as \"([^\"]*)\" without change$")
    public void the_content_can_be_retrieved_from_as_without_change(String folderName, String fileName)
            throws Throwable {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        contentRepository.loadFile(folderName, fileName, os);
        String retrievedContent = new String(os.toByteArray());

        assertEquals("Unexpected content retrieved!", fileContent, retrievedContent);
    }

    @Then("^the content can no longer be retrieved from \"([^\"]*)\" as \"([^\"]*)\"$")
    public void the_content_can_no_longer_be_retrieved_from_as(String folderName, String fileName) throws Throwable {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        contentRepository.loadFile(folderName, fileName, os);
        String retrievedContent = new String(os.toByteArray());

        assertEquals("Unexpected content retrieved!", 0, retrievedContent.length());
    }

}

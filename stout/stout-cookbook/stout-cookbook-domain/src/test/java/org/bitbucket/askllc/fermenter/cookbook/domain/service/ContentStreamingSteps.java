package org.bitbucket.askllc.fermenter.cookbook.domain.service;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.NonUUIDKeyEntityBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ContentRepositoryExampleService;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.content.ContentRepositoryStream;
import org.bitbucket.fermenter.stout.mock.MockRequestScope;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContentStreamingSteps {

    @Inject
    private ContentRepositoryExampleService contentRepositoryExampleService;

    @Inject
    private MockRequestScope mockRequestScope;
    
    private String resultingContent;

    @Before("@clientContentStreaming")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
    }

    @After("@clientContentStreaming")
    public void cleanUp() {
        mockRequestScope.cleanMessageManager();
        NonUUIDKeyEntityBO.deleteAllNonUUIDKeyEntities();
        
        AuthenticationTestUtils.logout();
    }

    @When("^I request a file stream from the content respository$")
    public void i_request_a_file_stream_from_the_content_respository() throws Throwable {
        assertNotNull("Service not available!", contentRepositoryExampleService);

        Response response = contentRepositoryExampleService.getInspectionChecklist();
        
        // in practice, you would invoked this via a REST service.  But just ensuring that all
        // the streaming wires up correctly in this case:
        ContentRepositoryStream stream = (ContentRepositoryStream)response.getEntity();
        ByteArrayOutputStream resultingStream = new ByteArrayOutputStream();
        stream.write(resultingStream);
        resultingContent = resultingStream.toString();
    }

    @Then("^I get a file with content$")
    public void i_get_a_file_with_content() throws Throwable {        
        assertNotNull("Expected content to be returned!", resultingContent);
    }

}

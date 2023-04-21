package org.technologybrewery.fermenter.cookbook.domain.service;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.NonUUIDKeyEntityBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.ContentRepositoryExampleService;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.content.ContentRepositoryStream;
import org.technologybrewery.fermenter.stout.mock.MockRequestScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
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

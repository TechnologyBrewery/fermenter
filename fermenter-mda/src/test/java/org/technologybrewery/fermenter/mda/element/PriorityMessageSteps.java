package org.technologybrewery.fermenter.mda.element;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import org.technologybrewery.fermenter.mda.MojoTestCaseWrapper;
import org.technologybrewery.fermenter.mda.util.PriorityMessage;
import org.technologybrewery.fermenter.mda.util.PriorityMessageExecution;
import org.technologybrewery.fermenter.mda.util.PriorityMessageService;

import org.apache.maven.execution.MavenSession;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

public class PriorityMessageSteps {
    private MojoTestCaseWrapper mojoTestCase = new MojoTestCaseWrapper();
    private static final Logger logger = (Logger) LoggerFactory.getLogger(PriorityMessageExecution.class);
    private ListAppender<ILoggingEvent> listAppender;
    private static final String DIVIDER = "***********************************************************************\n";
    private static final String HEADER = "*** Fermenter High Priority Messages                                ***\n";
    private static final String EMPTY_LINE = "\n";
    private MavenSession session;
    private PriorityMessageExecution priorityMessageExecution;
    private PriorityMessageService priorityMessageService;
    private List<PriorityMessage> priorityMessages;

    @Before("@priorityMessage")
    public void configureMavenTestSession() throws Exception {
        //setup the new maven session and necessary classes for priority messages
        this.session = mojoTestCase.newMavenSession();
        this.priorityMessageService = new PriorityMessageService(this.session);
        this.priorityMessageExecution = new PriorityMessageExecution();
        this.priorityMessages = new ArrayList<PriorityMessage>();

        //tool used for collecting the logging output
        this.listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @After("@priorityMessage")
    public void tearDownMavenPluginTestHarness() throws Exception {
        mojoTestCase.tearDownPluginTestHarness();
    }

    @When("^the priority message\\(s\\) is\\(are\\) added to the maven session$")
    public void the_priority_message_s_is_added_to_the_maven_session() throws Throwable {
        //adds all the priority messages to the session using the service
        priorityMessages.forEach(priorityMessageService::addPriorityMessage);
    }

    @Given("^a priority message without metadata$")
    public void a_priority_message_without_metadata() throws Throwable {
        PriorityMessage message = new PriorityMessage("Test Message");
        priorityMessages.add(message);
    }

    @Then("^a priority message without metadata should be displayed at session end$")
    public void a_priority_message_without_metadata_should_be_displayed_at_session_end() throws Throwable {
        //***********************************************************************
        //*** Fermenter High Priority Messages                                ***
        //***********************************************************************
        //Test Message
        //
        //***********************************************************************
        //***********************************************************************

        //build the correct output
        StringBuilder correctLog = new StringBuilder();
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(HEADER);
        correctLog.append(DIVIDER);
        correctLog.append("Test Message\n");
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(DIVIDER);

        //run the end of session command
        priorityMessageExecution.afterSessionEnd(this.session);

        //verify the log output is correct
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(correctLog.toString(), logsList.get(0).getMessage());
        assertEquals(Level.WARN, logsList.get(0).getLevel());
    }

    @Given("^a priority message with metadata$")
    public void a_priority_message_with_metadata() throws Throwable {
        PriorityMessage message = new PriorityMessage("Test Message", "test/file/path");
        priorityMessages.add(message);
    }

    @Then("^a priority message with metadata should be displayed at session end$")
    public void a_priority_message_with_metadata_should_be_displayed_at_session_end() throws Throwable {
        //***********************************************************************
        //*** Fermenter High Priority Messages                                ***
        //***********************************************************************
        //test/file/path:
        //Test Message
        //
        //***********************************************************************
        //***********************************************************************

        //build the correct output
        StringBuilder correctLog = new StringBuilder();
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(HEADER);
        correctLog.append(DIVIDER);
        correctLog.append("test/file/path:\nTest Message\n");
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(DIVIDER);

        //run the end of session command
        priorityMessageExecution.afterSessionEnd(this.session);

        //verify the log output is correct
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(correctLog.toString(), logsList.get(0).getMessage());
        assertEquals(Level.WARN, logsList.get(0).getLevel());
    }

    @Given("^multiple priority messages$")
    public void multiple_priority_messages() throws Throwable {
        PriorityMessage message1 = new PriorityMessage("Test Message 1", "test/file/path/");
        PriorityMessage message2 = new PriorityMessage("Test Message 2");

        priorityMessages.add(message1);
        priorityMessages.add(message2);
    }

    @Then("^the priority messages should be displayed at session end$")
    public void the_priority_messages_should_be_displayed_at_session_end() throws Throwable {
        //***********************************************************************
        //*** Fermenter High Priority Messages                                ***
        //***********************************************************************
        //test/file/path/:
        //Test Message 1
        //
        //Test Message 2
        //
        //***********************************************************************
        //***********************************************************************

        //build the correct output
        StringBuilder correctLog = new StringBuilder();
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(HEADER);
        correctLog.append(DIVIDER);
        correctLog.append("test/file/path/:\nTest Message 1\n");
        correctLog.append(EMPTY_LINE);
        correctLog.append("Test Message 2\n");
        correctLog.append(EMPTY_LINE);
        correctLog.append(DIVIDER);
        correctLog.append(DIVIDER);

        //run the end of session command
        priorityMessageExecution.afterSessionEnd(this.session);

        //verify the log output is correct
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(correctLog.toString(), logsList.get(0).getMessage());
        assertEquals(Level.WARN, logsList.get(0).getLevel());
    }

    @Given("^no priority messages$")
    public void no_priority_messages() throws Throwable { //no messages, so nothing to do
    }

    @Then("^no priority messages should be displayed at session end$")
    public void no_priority_messages_should_be_displayed_at_session_end() throws Throwable {
        //Should not be any output from the end of session command
        priorityMessageExecution.afterSessionEnd(this.session);

        //verify the log output is correct
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(0, logsList.size());
    }
}

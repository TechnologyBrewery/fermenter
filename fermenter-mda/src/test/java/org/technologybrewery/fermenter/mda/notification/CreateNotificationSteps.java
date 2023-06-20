package org.technologybrewery.fermenter.mda.notification;

import com.google.common.collect.Sets;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.maven.execution.MavenSession;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Assert;
import org.technologybrewery.fermenter.mda.MojoTestCaseWrapper;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.TestGenerator;
import org.technologybrewery.fermenter.mda.generator.TestMultipleNotificationGenerator;
import org.technologybrewery.fermenter.mda.generator.TestSpecificNotificationGenerator;
import org.technologybrewery.fermenter.mda.reporting.StatisticsService;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateNotificationSteps {

    public static final String APPLE_RECORDS = "apple records";
    private List<TestGenerator> generators;
    private NotificationService notificationService;
    private StatisticsService statisticsService;
    private VelocityEngine engine;
    private MojoTestCaseWrapper testCase;

    @Before("@createNotifications")
    public void setup() throws Exception {
        generators = new ArrayList<>();
        testCase = new MojoTestCaseWrapper();
        testCase.configurePluginTestHarness();
        MavenSession session = testCase.newMavenSession();
        notificationService = new NotificationService(session);
        statisticsService = new StatisticsService(session);

        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    @After("@createNotifications")
    public void cleanUp() throws Exception {
        for (TestGenerator eachGenerator : generators) {
            Files.deleteIfExists(eachGenerator.getTemplatePath());
            Files.deleteIfExists(eachGenerator.getOutputPath());
        }

        NotificationCollector.cleanup();

        testCase.tearDownPluginTestHarness();
    }

    @Given("^a configuration that triggers (\\d+) notification for manual action to be taken in (\\d+)$")
    public void a_configuration_that_triggers_notification_for_manual_action_to_be_taken_in(int notificationsPerFile, int numberOfFiles) throws Throwable {
        for (int i = 0; i < numberOfFiles; i++) {
            TestMultipleNotificationGenerator generator = new TestMultipleNotificationGenerator(numberOfFiles, notificationsPerFile);
            generators.add(generator);
        }
    }

    @Given("^a configuration that triggers a notification for manual action with a \"([^\"]*)\", \"([^\"]*)\", and programmatic value$")
    public void a_configuration_that_triggers_a_notification_for_manual_action_with_a_and_programmatic_value(String key, String items) throws Throwable {
        String[] itemArray = items.split(",");
        Set<String> itemSet = Sets.newHashSet(itemArray);

        TestSpecificNotificationGenerator generator = new TestSpecificNotificationGenerator(key, itemSet, APPLE_RECORDS);
        generators.add(generator);
    }

    @When("^the MDA plugin runs$")
    public void the_MDA_plugin_runs() throws Throwable {
        GenerationContext context = new GenerationContext();
        context.setEngine(engine);
        context.setStatisticsService(statisticsService);
        for (AbstractGenerator eachGenerator : generators) {
            eachGenerator.generate(context);
        }
    }

    @Then("^(\\d+) are registered for output for (\\d+) files$")
    public void are_registered_for_output_for_files(int notificationsPerFile, int numberOfFiles) throws Throwable {
        Map<String, Map<String, Notification>> notificationsByFilenameMap = getNotificationByFilename();
        Assert.assertEquals("Unexpected number of notification files found!", numberOfFiles, notificationsByFilenameMap.size());

        for (Map<String, Notification> notificationMap : notificationsByFilenameMap.values()) {
            Assert.assertEquals("Unexpected number of notifications found!", notificationsPerFile, notificationMap.size());

            for (Notification notification : notificationMap.values()) {
                Assert.assertTrue(String.format("Unexpected output in notification message: %s", notification.getNotificationAsString(),
                    notification.getKey()), notification.getNotificationAsString().contains(notification.getKey()));
            }
        }
    }

    @Then("^the resulting message contains the \"([^\"]*)\", \"([^\"]*)\", and programmatic value$")
    public void the_resulting_message_contains_the_and_programmatic_value(String expectedKey, String expectedItems) throws Throwable {
        Map<String, Map<String, Notification>> notificationsByFilenameMap = getNotificationByFilename();

        Map<String, Notification> notificationMap = notificationsByFilenameMap.values().iterator().next();
        Assert.assertNotNull("Notifications should have been found!", notificationMap);

        Notification notification = notificationMap.values().iterator().next();
        Assert.assertNotNull("Notification should have been found!", notification);

        String notificationMessage = notification.getNotificationAsString();

        Assert.assertTrue("Key was not found in message!", notificationMessage.contains(expectedKey));
        for (String expectedInsert : expectedItems.split(",")) {
            Assert.assertTrue("Item was not found in message!", notificationMessage.contains(expectedInsert));
        }
        Assert.assertTrue("Programmatic value was not found in message!", notificationMessage.contains(APPLE_RECORDS));

    }

    private static Map<String, Map<String, Notification>> getNotificationByFilename() {
        Map<String, Map<String, Notification>> notificationsByFilenameMap = NotificationCollector.getNotifications();
        Assert.assertNotNull("Notifications map was not found!", notificationsByFilenameMap);
        return notificationsByFilenameMap;
    }

}

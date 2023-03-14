package org.bitbucket.fermenter.mda.reporting;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.maven.execution.MavenSession;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.bitbucket.fermenter.mda.MojoTestCaseWrapper;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.TestGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class GenerationStatsSteps {
    private static final Logger logger = LoggerFactory.getLogger(GenerationStatsSteps.class);
    private static final int MAX_SIZE = 1000;

    private List<TestGenerator> generators;
    private StatisticsService statisticsService;
    private VelocityEngine engine;
    private MojoTestCaseWrapper testCase;

    @Before("@generationStats")
    public void setup() throws Exception {
        generators = new ArrayList<>();
        testCase = new MojoTestCaseWrapper();
        testCase.configurePluginTestHarness();
        MavenSession session = testCase.newMavenSession();
        statisticsService = new StatisticsService(session);

        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    @After("@generationStats")
    public void cleanUp() throws Exception {
        for (TestGenerator eachGenerator : generators) {
            Files.deleteIfExists(eachGenerator.getTemplatePath());
            Files.deleteIfExists(eachGenerator.getOutputPath());
        }
        testCase.tearDownPluginTestHarness();
    }

    @Given("^statistics are enabled$")
    public void statisticsAreEnabled() {
        statisticsService.setStatsReportingEnabled(true);
    }

    @Given("^statistics are not enabled$")
    public void statisticsAreNotEnabled() {
        statisticsService.setStatsReportingEnabled(false);
    }

    @Given("^an overwritable target is selected$")
    public void anOverwritableTargetIsSelected() {
        generators.add(new TestGenerator(randomSize(), true));
    }

    @Given("^a non-overwritable target is selected$")
    public void aNonOverwritableTargetIsSelected() {
        generators.add(new TestGenerator(randomSize(), false));
    }

    @Given("^the target does not exist$")
    public void theTargetDoesNotExist() throws IOException {
        for (TestGenerator eachGenerator : generators) {
            Files.deleteIfExists(eachGenerator.getOutputPath());
        }
    }

    @Given("^the target already exists$")
    public void theTargetAlreadyExists() throws IOException {
        for (TestGenerator eachGenerator : generators) {
            Files.writeString(eachGenerator.getOutputPath(), "temp");
        }
    }

    @Given("^multiple targets are selected$")
    public void multipleTargetsAreSelected() {
        generators.add(new TestGenerator(randomSize(), true));
        generators.add(new TestGenerator(randomSize(), true));
        generators.add(new TestGenerator(randomSize(), true));
    }

    @When("^the generators are executed$")
    public void theGeneratorsAreExecuted() {
        GenerationContext context = new GenerationContext();
        context.setEngine(engine);
        context.setStatisticsService(statisticsService);
        for (AbstractGenerator eachGenerator : generators) {
            eachGenerator.generate(context);
        }
    }

    @Then("^the total file size of the generated files is captured$")
    public void theTotalFileSizeOfTheGeneratedFilesIsCaptured() {
        long expectedSize = generators.stream()
            .mapToLong(TestGenerator::getFileSize)
            .sum();
        long recordedSize = statisticsService.calculateFinalStats().getTotalSize();
        assertEquals("Recorded generated file size differs from expected", expectedSize, recordedSize);
    }

    @Then("^the file size is not recorded$")
    public void theFileSizeIsNotRecorded() {
        long recordedSize = statisticsService.calculateFinalStats().getTotalSize();
        assertEquals("Recorded generated file size differs from expected", 0L, recordedSize);
    }

    private static int randomSize() {
        return ThreadLocalRandom.current().nextInt(0, MAX_SIZE);
    }
}

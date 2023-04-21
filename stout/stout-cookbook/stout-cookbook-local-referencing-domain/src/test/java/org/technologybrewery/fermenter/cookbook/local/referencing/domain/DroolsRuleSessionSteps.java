package org.technologybrewery.fermenter.cookbook.local.referencing.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.technologybrewery.fermenter.cookbook.domain.transfer.BeerExampleEntity;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class DroolsRuleSessionSteps {
    private static final Logger logger = LoggerFactory.getLogger(LocalInvocationSteps.class);
    private KieContainer kContainer;
    private KieSession kieSession;
    private BeerExampleEntity beerExampleEntity;
    
    @Before("@droolsRuleProcessing")
    public void setUp() throws Exception {
    }

    @After("@droolsRuleProcessing")
    public void cleanUp() {
    }
    
    @Given("^sessions named SessionA and SessionB are defined in the model$")
    public void sessions_named_SessionA_and_SessionB_are_defined_in_the_model() throws Throwable {
        KieServices kieServices = KieServices.Factory.get();
        kContainer = kieServices.getKieClasspathContainer();
    }

    @When("^I request the processing of SessionA and SessionB$")
    public void i_request_the_processing_of_SessionA_and_SessionB() throws Throwable {
        assertNotNull(this.kContainer);
        beerExampleEntity = new BeerExampleEntity(); 
        beerExampleEntity.setBeerType("IPA");

        // Three separated and isolated rule groups applied one at a time
        KieBase kBase1 = this.kContainer.getKieBase("KBase1");
        this.kieSession = this.kContainer.newKieSession("KSessionDefault");
        this.kieSession.insert(beerExampleEntity);
        this.kieSession.fireAllRules();

        KieBase kBase2 = this.kContainer.getKieBase("KBase2");
        this.kieSession = this.kContainer.newKieSession("KSessionA");
        this.kieSession.insert(beerExampleEntity);
        this.kieSession.fireAllRules();

        KieBase kBase3 = this.kContainer.getKieBase("KBase3");
        this.kieSession = this.kContainer.newKieSession("KSessionB");
        this.kieSession.insert(beerExampleEntity);
        this.kieSession.fireAllRules();
    }

    @Then("^I get a successful message$")
    public void i_get_a_successful_message() throws Throwable {
        assertNotNull(this.kContainer);
        assertEquals("B: B: A: A: I: IPA", this.beerExampleEntity.getBeerType());
    }
    
    
}

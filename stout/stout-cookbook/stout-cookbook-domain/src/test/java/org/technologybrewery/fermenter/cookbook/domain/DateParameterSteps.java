package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomUtils;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.technologybrewery.fermenter.stout.service.ValueServiceResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class DateParameterSteps {
    private static final String PRIOR_TO = "prior to";
    private static final String FOR = "for";
    private static final String AFTER = "after";
    
    List<SimpleDomainBO> records = new ArrayList<>();
    Collection<SimpleDomainBO> retrievedBOs;
    Date today = getCalendarCurrentDateInstance().getTime();
    
    @Inject
    private SimpleDomainManagerService simpleDomainMgr;
    
    @Before("@dateParameter")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
    }
    
    @After("@dateParameter")
    public void cleanupMsgMgr() throws Exception {
        SimpleDomainBO.deleteAllSimpleDomain();
        MessageManagerInitializationDelegate.cleanupMessageManager();
        
        AuthenticationTestUtils.logout();
    }

    @Given("^there are simple domains with dates in the past, present, and future$")
    public void there_are_simple_domains_with_dates_in_the_past_present_and_future() throws Throwable {
        int numberOfObjects = RandomUtils.nextInt(10, 20);

        Calendar calendar = getCalendarCurrentDateInstance();

        // create simple domains with dates in the present and future
        for (int i = 0; i < numberOfObjects; i++) {
            SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
            // add one day from the date
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();

            simpleDomain.setTheDate1(date);
            simpleDomain.save();
        }

        calendar = getCalendarCurrentDateInstance();

        // create simple domains with dates in the past and future
        for (int i = 0; i < numberOfObjects; i++) {
            SimpleDomainBO simpleDomain = TestUtils.createRandomSimpleDomain();
            // take one day from the date
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date date = calendar.getTime();

            simpleDomain.setTheDate1(date);
            simpleDomain.save();
        }
    }

    @When("^I want to get all simple domains \"([^\"]*)\" today$")
    public void i_want_to_get_all_simple_domains_today(String comparisonType) throws Throwable {
        ValueServiceResponse<Collection<SimpleDomainBO>> response = null;

        switch (comparisonType) {

        case PRIOR_TO:
            response = simpleDomainMgr.selectAllSimpleDomainsBeforeDate(today);
            break;
        case FOR:
            response = simpleDomainMgr.selectAllSimpleDomainsByDate(today);
            break;
        case AFTER:
            response = simpleDomainMgr.selectAllSimpleDomainsAfterDate(today);
            break;
        default:
            fail("invalid comparison type");
        }

        assertNotNull(response);
        retrievedBOs = response.getValue();
    }

    @Then("^I should get only simple domains \"([^\"]*)\" today$")
    public void i_should_get_only_simple_domains_today(String comparisonType) throws Throwable {
        for (SimpleDomainBO simpleDomainBO : retrievedBOs) {
            Date actualDate = simpleDomainBO.getTheDate1();
            switch (comparisonType) {
            case PRIOR_TO:
                assertTrue("Actual date should be before current date", today.compareTo(actualDate) > 0);
                break;
            case FOR:
                assertEquals("Actual date and current date should be the same", today, actualDate);
                break;
            case AFTER:
                assertTrue("Actual date should be later than current date ", today.compareTo(actualDate) < 0);
                break;
            default:
                fail("invalid comparison type");
            }
        }
    }

    private Calendar getCalendarCurrentDateInstance() {
        // get a calendar instance, which defaults to "now"
        // remove time and only leave the actual date portion
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;

    }

}

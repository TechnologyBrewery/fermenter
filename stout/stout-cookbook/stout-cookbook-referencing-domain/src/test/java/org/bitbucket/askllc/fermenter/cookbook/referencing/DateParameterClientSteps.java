package org.bitbucket.askllc.fermenter.cookbook.referencing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.SimpleDomainMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.SimpleDomainManagerDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.stereotype.Component;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Component
public class DateParameterClientSteps {

    @Inject
    private SimpleDomainManagerDelegate delegate;

    @Inject
    private SimpleDomainMaintenanceDelegate maintenanceDelegate;

    Collection<SimpleDomain> simpleDomains = null;
    Date today = getCalendarCurrentDateInstance().getTime();

    @Before("@dateParameterClient")
    public void setUp() {
        assertNotNull("Missing needed delegate!", delegate);
        MessageManagerInitializationDelegate.cleanupMessageManager();
    }

    @After("@dateParameterClient")
    public void cleanUp() throws Exception {
        delegate.deleteAllSimpleDomains();
        MessageManagerInitializationDelegate.cleanupMessageManager();
    }

    @Given("^a simple domain with today's date$")
    public void a_simple_domain_with_today_s_date() throws Throwable {
        SimpleDomain domain = new SimpleDomain();
        domain.setTheDate1(today);
        maintenanceDelegate.create(domain);
    }

    @When("^the simple domain for today's date is retrieved using \"([^\"]*)\"$")
    public void the_simple_domain_for_today_s_date_is_retrieved_using(String dateType) throws Throwable {
        if (dateType.equals("java.util.Date")) {
            System.out.println("Integration test: " + today.toString());
            simpleDomains = delegate.selectAllSimpleDomainsByDate(today);
        } else if (dateType.equals("java.sql.Date")) {
            // create a java.sql.Date from the java.util.Date
            java.sql.Date sqlToday = new java.sql.Date(today.getTime());
            System.out.println("Integration test: " + sqlToday.toString());
            simpleDomains = delegate.selectAllSimpleDomainsByDate(sqlToday);
        } else {
            simpleDomains = null;
        }
    }

    @Then("^the simple domain is retrieved successfully$")
    public void the_simple_domain_is_retrieved_successfully() throws Throwable {
        MessageTestUtils.assertNoErrorMessages();
        assertNotNull(simpleDomains);
        assertTrue(simpleDomains.size() > 0);
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

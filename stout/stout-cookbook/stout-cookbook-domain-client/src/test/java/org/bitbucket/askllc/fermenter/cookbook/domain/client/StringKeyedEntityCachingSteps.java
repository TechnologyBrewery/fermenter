package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.cache.StringKeyedEntityCache;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.StringKeyedEntityMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.StringKeyedEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
public class StringKeyedEntityCachingSteps {

    @Inject
    StringKeyedEntityMaintenanceDelegate maintenanceDelegate;

    StringKeyedEntity stringKeyExample;
    StringKeyedEntity stringKeyFirstLookUp;
    StringKeyedEntity stringKeySecondLookUp;

    @Before("@stringKeyedEntityCaching")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        stringKeyExample = new StringKeyedEntity();
    }

    @After("@stringKeyedEntityCaching")
    public void cleanUp() {
        if (stringKeyExample != null) {
            maintenanceDelegate.delete(stringKeyExample.getId());
        }
        StringKeyedEntityCache.invalidateCache();
    }

    @Given("^a referenced entity with a string key with blank spaces exists in the foreign domain$")
    public void a_referenced_entity_with_a_string_key_with_blank_spaces_exists_in_the_foreign_domain()
            throws Throwable {
        String key = RandomStringUtils.randomAlphanumeric(10);
        int randomNumBlankSpaces = RandomUtils.nextInt(1, 20);
        String paddedKey = StringUtils.rightPad(key, key.length() + randomNumBlankSpaces);
        stringKeyExample.setId(paddedKey);
        stringKeyExample = maintenanceDelegate.create(stringKeyExample);
    }

    @Given("^a referenced entity with a string key exists in the foreign domain$")
    public void a_referenced_entity_with_a_string_key_exists_in_the_foreign_domain() throws Throwable {
        stringKeyExample.setId(RandomStringUtils.randomAlphanumeric(10));
        stringKeyExample = maintenanceDelegate.create(stringKeyExample);
    }

    @Given("^I look up the string keyed reference with the maintenance rest client$")
    public void i_look_up_the_string_keyed_reference_with_the_maintenance_rest_client() throws Throwable {
        stringKeyFirstLookUp = maintenanceDelegate.findByPrimaryKey(stringKeyExample.getId().trim());
    }

    @When("^I look up the string keyed reference again with the maintenance rest client$")
    public void i_look_up_the_string_keyed_reference_again_with_the_maintenance_rest_client() throws Throwable {
        stringKeySecondLookUp = maintenanceDelegate.findByPrimaryKey(stringKeyExample.getId().trim());
    }

    @When("^I look up the same reference again with a key with (\\d+) with the maintenance rest client$")
    public void i_look_up_the_same_reference_again_with_a_key_with_with_the_maintenance_rest_client(int numberBlankSpaces)
            throws Throwable {
        String key = StringUtils.trim(stringKeyExample.getId());
        String paddedKey = StringUtils.rightPad(key, key.length() + numberBlankSpaces);
        stringKeySecondLookUp = maintenanceDelegate.findByPrimaryKey(paddedKey);
    }

    @Then("^the cached string keyed referenced is used$")
    public void the_cached_string_keyed_referenced_is_used() throws Throwable {
        assertEquals("Entity didn't match looked up value", stringKeyExample, stringKeySecondLookUp);
        assertTrue("Entity object was not same object", stringKeyExample == stringKeySecondLookUp);
    }

}

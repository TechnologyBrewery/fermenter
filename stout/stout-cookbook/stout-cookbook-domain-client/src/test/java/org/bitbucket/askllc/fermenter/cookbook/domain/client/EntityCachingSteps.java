package org.bitbucket.askllc.fermenter.cookbook.domain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.client.cache.CachedEntityExampleCache;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.CachedEntityExampleMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.SimpleDomainMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.CachedEntityExample;
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
public class EntityCachingSteps {

    @Inject
    CachedEntityExampleMaintenanceDelegate maintenanceDelegate;

    @Inject
    SimpleDomainMaintenanceDelegate simpleDomainMaintenanceDelegate;

    CachedEntityExample exampleEntity;
    CachedEntityExample firstLookUp;
    CachedEntityExample secondLookUp;

    @Before("@referenceCaching")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "somePassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        exampleEntity = new CachedEntityExample();
        exampleEntity.setName("TEST NAME");
    }

    @After("@referenceCaching")
    public void cleanUp() {
        if (exampleEntity != null) {
            maintenanceDelegate.delete(exampleEntity.getId());
        }
        CachedEntityExampleCache.invalidateCache();
    }

    @Given("^a referenced entity exists in the foreign domain$")
    public void aReferencedEntityExistsInTheForeignDomain() {
        exampleEntity = maintenanceDelegate.create(exampleEntity);
        CachedEntityExampleCache.invalidateCache();
    }

    @Given("^I look up a reference with the maintenance rest client$")
    public void iLookUpAReferenceWithTheMaintenanceRestClient() {
        firstLookUp = maintenanceDelegate.findByPrimaryKey(exampleEntity.getId());
    }

    @When("^I look up the same reference again with the maintenance rest client$")
    public void iLookUpTheSameReferenceAgainWithTheMaintenanceRestClient() {
        secondLookUp = maintenanceDelegate.findByPrimaryKey(exampleEntity.getId());
    }

    @When("^I delete an existing entity through the maintenance rest client$")
    public void iDeleteAnExistingEntityThroughTheMaintenanceRestClient() {
        maintenanceDelegate.delete(exampleEntity.getId());
    }

    @Then("^the reference is cached for later calls$")
    public void theReferenceIsCachedForLaterCalls() {
        CachedEntityExample cachedValue = CachedEntityExampleCache.getFromCache(firstLookUp.getId());
        assertEquals("Cached value didn't equal lookup value", firstLookUp, cachedValue);
    }

    @Then("^the cached referenced is used$")
    public void theCachedReferencedIsUsed() {
        assertEquals("Entity didn't match looked up value", exampleEntity, secondLookUp);
    }

    @Then("^the cached entity is removed$")
    public void theCachedEntityIsRemoved() {
        CachedEntityExample cachedValue = CachedEntityExampleCache.getFromCache(exampleEntity.getId());
        assertNull("Value was not removed from cache", cachedValue);
    }
}

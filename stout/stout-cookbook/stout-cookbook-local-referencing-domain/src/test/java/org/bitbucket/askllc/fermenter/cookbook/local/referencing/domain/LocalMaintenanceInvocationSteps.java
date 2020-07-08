package org.bitbucket.askllc.fermenter.cookbook.local.referencing.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.BusinessKeyDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.client.service.BusinessKeyedExampleMaintenanceDelegate;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.BusinessKeyedExample;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.page.PageWrapper;
import org.bitbucket.fermenter.stout.page.json.FindByExampleCriteria;
import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.bitbucket.fermenter.stout.sort.SortWrapper;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class LocalMaintenanceInvocationSteps {

    @Inject
    private BusinessKeyedExampleMaintenanceDelegate maintenanceDelegate;

    @Inject
    private BusinessKeyDelegate businessDelegate;

    private String businessKey;
    private Collection<BusinessKeyedExample> createdInstances = new ArrayList<>();
    private List<String> originalBusinessKeys = new ArrayList<>();

    private int numberOfBulkItems;
    private static final String SELECTOR = RandomStringUtils.randomAlphanumeric(2);

    @Before("@localInvocationOfRemoteMaintenanceService")
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "abc123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertNotNull("Could not access maintenance delegate!", maintenanceDelegate);
        
        MessageManagerInitializationDelegate.initializeMessageManager();
    }

    @After("@localInvocationOfRemoteMaintenanceService")
    public void cleanUp() {
        if (CollectionUtils.isNotEmpty(createdInstances)) {
            maintenanceDelegate.bulkDelete(createdInstances);
        }

        MessageManagerInitializationDelegate.cleanupMessageManager();

    }

    @Given("^an existing instance of an entity$")
    public void an_existing_instance_of_an_entity() throws Throwable {
        createInstance();

    }

    @Given("^existing instances of an entity$")
    public void existing_instances_of_an_entity() throws Throwable {
        createManyInstances();
    }

    @When("^a create service is invoked$")
    public void a_create_service_is_invoked() throws Throwable {
        createInstance();

    }

    @When("^the entity is updated via the maintainence service$")
    public void the_entity_is_updated_via_the_maintainence_service() throws Throwable {
        BusinessKeyedExample currentRecord = findByBusinessKey();
        currentRecord.setBusinessKey(RandomStringUtils.randomAlphanumeric(10));
        maintenanceDelegate.update(currentRecord.getPrimaryKey(), currentRecord);
    }

    @When("^the entity is deleted via the maintainence service$")
    public void the_entity_is_deleted_via_the_maintainence_service() throws Throwable {
        findByBusinessKey();

        BusinessKeyedExample instance = createdInstances.iterator().next();

        maintenanceDelegate.delete(instance.getPrimaryKey());
        MessageTestUtils.assertNoErrorMessages();

        // clear out instance so we don't try to delete this again in cleanup:
        createdInstances.clear();

    }

    @When("^a bulk create service is invoked$")
    public void a_bulk_create_service_is_invoked() throws Throwable {
        createManyInstances();
    }

    @When("^the entities are updated via the bulk maintainence service$")
    public void the_entities_are_updated_via_the_bulk_maintainence_service() throws Throwable {
        PageWrapper<BusinessKeyedExample> foundInstances = findInstancesBySelector();        
        for (BusinessKeyedExample instance : foundInstances.getContent()) {
            originalBusinessKeys.add(instance.getBusinessKey());
        }        

        Collection<BusinessKeyedExample> instancesToUpdate = foundInstances.getContent();
        for (BusinessKeyedExample instance : instancesToUpdate) {
            instance.setBusinessKey(RandomStringUtils.randomAlphanumeric(10));
        }

        maintenanceDelegate.bulkSaveOrUpdate(instancesToUpdate);
    }
    
    @When("^the entities are deleted via the bulk maintainence service$")
    public void the_entities_are_deleted_via_the_bulk_maintainence_service() throws Throwable {
        PageWrapper<BusinessKeyedExample> foundInstances = findInstancesBySelector(); 
        
        maintenanceDelegate.bulkDelete(foundInstances.getContent());
        
        // clear out our delete tracking so we don't try to delete already deleted records during cleanup:
        createdInstances.clear();
    } 

    @Then("^the newly created instance can be retrieved$")
    public void the_newly_created_instance_can_be_retrieved() throws Throwable {
        BusinessKeyedExample foundEntity = findByBusinessKey();
        assertNotNull("Could not retrieve the expected entity!", foundEntity);
    }

    @Then("^the update can be retrieved$")
    public void the_update_can_be_retrieved() throws Throwable {
        BusinessKeyedExample instance = createdInstances.iterator().next();

        BusinessKeyedExample foundEntity = maintenanceDelegate.findByPrimaryKey(instance.getPrimaryKey());
        assertNotEquals("Business key should have been updated from the original value!", foundEntity.getBusinessKey(),
                businessKey);
    }

    @Then("^the entity can not longer be retrieved$")
    public void the_entity_can_not_longer_be_retrieved() throws Throwable {
        BusinessKeyedExample resultingInstance = businessDelegate.findByBusinessKey(businessKey);
        assertNull("Should not have found an instance!", resultingInstance);
    }

    @Then("^the newly created instances can be retrieved via find by example$")
    public void the_newly_created_instances_can_be_retrieved_via_find_by_example() throws Throwable {
        PageWrapper<BusinessKeyedExample> results = findInstancesBySelector();

        assertEquals("Unexpected record count returned!", numberOfBulkItems, results.getTotalResults().intValue());
        assertEquals("Unexpected number of results returned!", numberOfBulkItems, results.getContent().size());
    }
    
    @Then("^the newly created instances can be retrieved via find by example CONTAINS call$")
    public void the_newly_created_instances_can_be_retrieved_via_find_by_example_CONTAINS_call() throws Throwable {
        Collection<BusinessKeyedExample> results = maintenanceDelegate.findByExampleContains(null);

        assertEquals("Unexpected number of results returned!", numberOfBulkItems, results.size());
    }

    @Then("^the updates can be retrieved$")
    public void the_updates_can_be_retrieved() throws Throwable {
        PageWrapper<BusinessKeyedExample> updatedInstances = findInstancesBySelector();
        for (BusinessKeyedExample updatedInstance : updatedInstances.getContent()) {
            assertFalse("Should not have found an original business key value after bulk update!",
                    originalBusinessKeys.contains(updatedInstance.getBusinessKey()));
        }

    }
    
    @Then("^then none of the entities can be retrieved$")
    public void then_none_of_the_entities_can_be_retrieved() throws Throwable {
        PageWrapper<BusinessKeyedExample> foundInstances = findInstancesBySelector();
        assertEquals("No instances should have been found!", 0, foundInstances.getContent().size());
    }
    

    private void createInstance() {
        BusinessKeyedExample inboundEntity = newTestEntity();
        maintenanceDelegate.create(inboundEntity);
        MessageTestUtils.assertNoErrorMessages();
        businessKey = inboundEntity.getBusinessKey();
    }

    private void createManyInstances() {
        numberOfBulkItems = RandomUtils.nextInt(5);

        List<BusinessKeyedExample> entitiesToCreate = new ArrayList<>();
        for (int i = 0; i < numberOfBulkItems; i++) {
            BusinessKeyedExample newInstance = newTestEntity();
            entitiesToCreate.add(newInstance);
        }

        maintenanceDelegate.bulkSaveOrUpdate(entitiesToCreate);
    }

    private BusinessKeyedExample findByBusinessKey() {
        BusinessKeyedExample createdInstance = businessDelegate.findByBusinessKey(businessKey);
        createdInstances.add(createdInstance);

        BusinessKeyedExample foundEntity = maintenanceDelegate.findByPrimaryKey(createdInstance.getPrimaryKey());
        return foundEntity;
    }

    private PageWrapper<BusinessKeyedExample> findInstancesBySelector() {
        BusinessKeyedExample probe = new BusinessKeyedExample();
        probe.setSelector(SELECTOR);

        FindByExampleCriteria<BusinessKeyedExample> criteria = new FindByExampleCriteria<>();
        criteria.setProbe(probe);
        criteria.setContainsMatch(Boolean.FALSE);
        SortWrapper sort = new SortWrapper(OrderWrapper.ASC, "businessKey");
        criteria.setSortWrapper(sort);
        criteria.setCount(100);
        criteria.setStartPage(0);

        PageWrapper<BusinessKeyedExample> results = maintenanceDelegate.findByExample(criteria);
        createdInstances.addAll(results.getContent());

        return results;
    }

    private BusinessKeyedExample newTestEntity() {
        BusinessKeyedExample newEntity = new BusinessKeyedExample();
        newEntity.setBusinessKey(RandomStringUtils.randomAlphabetic(5));
        newEntity.setSelector(SELECTOR);

        return newEntity;
    }

}

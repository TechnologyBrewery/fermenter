package org.technologybrewery.fermenter.cookbook.referencing;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.technologybrewery.fermenter.cookbook.domain.client.service.CachedEntityExampleBusinessDelegate;
import org.technologybrewery.fermenter.cookbook.domain.client.service.CachedEntityExampleMaintenanceDelegate;
import org.technologybrewery.fermenter.cookbook.domain.client.service.NonUUIDKeyEntityMaintenanceDelegate;
import org.technologybrewery.fermenter.cookbook.domain.client.service.impl.DelegateMaintenanceTransactionSynchronization;
import org.technologybrewery.fermenter.cookbook.domain.transfer.CachedEntityExample;
import org.technologybrewery.fermenter.cookbook.domain.transfer.NonUUIDKeyEntity;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Component
public class LevelOneCacheRestClientSteps {

    @Inject
    private CachedEntityExampleMaintenanceDelegate entityMaintenanceDelegate;

    @Inject
    private CachedEntityExampleBusinessDelegate businessDelegate;

    @Inject
    private NonUUIDKeyEntityMaintenanceDelegate nonUuidKeyEntityDelegate;

    @Inject
    private JtaTransactionManager txManager;

    private Collection<CachedEntityExample> entities = new HashSet<>();
    private Collection<String> entityNames = new HashSet<>();
    private Integer originalEntityCalculatedValue;
    private Integer updateEntityCalculatedValue;
    private CachedEntityExample retrievedEntity;
    private NonUUIDKeyEntity businessKeyedEntity;
    private boolean isBusinessKeyed;

    @Before("@levelOneRestCache")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
        assertNotNull("Missing needed maintenance delegate!", entityMaintenanceDelegate);

    }

    @After("@levelOneRestCache")
    public void cleanUp() throws Exception {               
        entityMaintenanceDelegate.bulkDelete(entities);
        MessageManagerInitializationDelegate.cleanupMessageManager();
        entities.clear();
        entityNames.clear();
        isBusinessKeyed = false;

        businessKeyedEntity = null;
        
        AuthenticationTestUtils.logout();
        
    }

    @Given("^a new entity created via a rest client within a transaction$")
    public void a_request_to_create_a_new_entity_via_a_rest_client() throws Throwable {
        txManager.getUserTransaction().begin();
        CachedEntityExample entity = new CachedEntityExample();
        entity.setName(RandomStringUtils.randomAlphabetic(10));
        entityMaintenanceDelegate.create(entity);
        entityNames.add(entity.getName());
    }

    @Given("^the entity is validated to not exist outside the current transaction$")
    public void the_entity_is_validated_to_not_exist_outside_the_current_transaction() throws Throwable {
        assertNameShouldNotBeFoundOutsideTransaction(entityNames.iterator().next());
    }

    @Given("^a existing entity that is updated within a transaction$")
    public void a_existing_entity_that_is_updated_within_a_transaction() throws Throwable {
        CachedEntityExample entity = createAndValidateKeyExistsForEntity();
        originalEntityCalculatedValue = entity.getCalculatedField();

        txManager.getUserTransaction().begin();
        String newName = entity.getName() + "-updated";
        entity.setName(newName);
        entity = entityMaintenanceDelegate.update(entity.getId(), entity);
        entityNames.add(entity.getName());
        entities.add(entity);
    }

    @Given("^the update is validated to not exist outside the current transaction$")
    public void the_update_is_validated_to_not_exist_outside_the_current_transaction() throws Throwable {
        assertNameShouldNotBeFoundOutsideTransaction(entityNames.iterator().next());
    }

    @Given("^a existing entity that is deleted within a transaction$")
    public void a_existing_entity_that_is_deleted_within_a_transaction() throws Throwable {
        createAndValidateKeyExistsForEntity();

        txManager.getUserTransaction().begin();
        CachedEntityExample originalEntity = entities.iterator().next();
        entityMaintenanceDelegate.delete(originalEntity.getId());

    }

    @Given("^the delete is validated to not exist outside the current transaction$")
    public void the_delete_is_validated_to_not_exist_outside_the_current_transaction() throws Throwable {
        CachedEntityExample originalEntity = entities.iterator().next();
        CachedEntityExample retrievedEntity = findOutsideCurrentTransaction(originalEntity.getName());
        assertNotNull("Entity unexpectedly exists outside the current transaction!", retrievedEntity);
    }

    @Given("^multiple new entities created via a bulk rest client within a transaction$")
    public void multiple_new_entities_created_via_a_bulk_rest_client_within_a_transaction() throws Throwable {
        txManager.getUserTransaction().begin();

        Collection<CachedEntityExample> entitiesToCreate = new ArrayList<CachedEntityExample>();
        for (int i = 0; i < RandomUtils.nextInt(2, 5); i++) {
            CachedEntityExample entity = new CachedEntityExample();
            entity.setName(RandomStringUtils.randomAlphabetic(10));
            entitiesToCreate.add(entity);
        }

        entitiesToCreate = entityMaintenanceDelegate.bulkSaveOrUpdate(entitiesToCreate);
        entities.addAll(entitiesToCreate);
    }

    @Given("^the entities are validated to not exist outside the current transaction$")
    public void the_entities_are_validated_to_not_exist_outside_the_current_transaction() throws Throwable {
        for (CachedEntityExample entity : entities) {
            CachedEntityExample outsideCurrentTxResult = findOutsideCurrentTransaction(entity.getName());
            assertNull("Should NOT be found before the transaction has committed - read isolation broken!",
                    outsideCurrentTxResult);
        }
    }

    @Given("^multiple entities updated via a bulk rest client within a transaction$")
    public void multiple_entities_updated_via_a_bulk_rest_client_within_a_transaction() throws Throwable {
        for (int i = 0; i < RandomUtils.nextInt(2, 5); i++) {
            createAndValidateKeyExistsForEntity();
        }

        txManager.getUserTransaction().begin();

        Collection<CachedEntityExample> entitiesToUpdate = new ArrayList<CachedEntityExample>();
        for (CachedEntityExample entity : entities) {
            String newName = entity.getName() + "-bulk-updated";
            entity.setName(newName);
            entitiesToUpdate.add(entity);
        }

        entityMaintenanceDelegate.bulkSaveOrUpdate(entitiesToUpdate);

    }

    @Given("^the updates are validated to not exist outside the current transaction$")
    public void the_updates_are_validated_to_not_exist_outside_the_current_transaction() throws Throwable {
        for (CachedEntityExample entity : entities) {
            assertNameShouldNotBeFoundOutsideTransaction(entity.getName());
        }
    }

    @Given("^multiple entities deleted via a bulk rest client within a transaction$")
    public void multiple_entities_deleted_via_a_bulk_rest_client_within_a_transaction() throws Throwable {
        for (int i = 0; i < RandomUtils.nextInt(2, 5); i++) {
            createAndValidateKeyExistsForEntity();
        }

        txManager.getUserTransaction().begin();
        entityMaintenanceDelegate.bulkDelete(entities);
    }

    @Given("^the deletes are validated to exist outside the current transaction$")
    public void the_deletes_are_validated_to_exist_outside_the_current_transaction() throws Throwable {
        for (CachedEntityExample entity : entities) {
            CachedEntityExample retrievedEntity = findOutsideCurrentTransaction(entity.getName());
            assertNotNull("Entity should still exist outside the transaction!", retrievedEntity);
        }
    }

    @Given("^a new business keyed entity with a key of \"([^\"]*)\" is created via a rest client within a transaction$")
    public void a_new_business_keyed_entity_with_a_key_of_is_created_via_a_rest_client_within_a_transaction(
            String businessKey) throws Throwable {
        txManager.getUserTransaction().begin();
        NonUUIDKeyEntity businessKeyedEntity = new NonUUIDKeyEntity();
        businessKeyedEntity.setId(businessKey);
        nonUuidKeyEntityDelegate.create(businessKeyedEntity);
        
        originalEntityCalculatedValue = businessKeyedEntity.getCalculatedField();
        
        isBusinessKeyed = true;
        
    }

    @When("^the transaction completes$")
    public void the_transaction_completes() throws Throwable {
        txManager.getUserTransaction().commit();
    }
    
    @When("^the cache is flushed$")
    public void the_cache_is_flushed() throws Throwable {
        DelegateMaintenanceTransactionSynchronization sync = new DelegateMaintenanceTransactionSynchronization();
        sync.flush();
    }    

    @When("^a findByPrimary key is performed on that entity$")
    public void a_findByPrimary_key_is_performed_on_that_entity() throws Throwable {
        CachedEntityExample entity = entities.iterator().next();
        retrievedEntity = entityMaintenanceDelegate.findByPrimaryKey(entity.getId());
    }

    @When("^the entity is requested by key \"([^\"]*)\" within the current transaction$")
    public void the_entity_is_requirest_by_key_within_the_current_transaction(String businessKey) throws Throwable {
        businessKeyedEntity = nonUuidKeyEntityDelegate.findByPrimaryKey(businessKey);
        
        updateEntityCalculatedValue = businessKeyedEntity.getCalculatedField();
        
        txManager.getUserTransaction().commit();
    }

    @Then("^the entity can be retrieved outside the original transaction$")
    public void the_instance_can_be_retrieved_outside_the_original_transaction() throws Throwable {
        assertNameIsFound();
    }

    @Then("^the update can be retrieved outside the original transaction$")
    public void the_update_can_retrieved_outside_the_original_transaction() throws Throwable {
        assertNameIsFound();
    }

    @Then("^the entity no longer exists outside the original transaction$")
    public void the_entity_no_longer_exists_outside_the_original_transaction() throws Throwable {
        CachedEntityExample originalEntity = entities.iterator().next();
        CachedEntityExample foundEntity = businessDelegate.findByName(originalEntity.getName());
        assertNull("Entity SHOULD have been deleted!", foundEntity);

        // now that this entity is confirmed to have been removed, take them away from cleanup routines:
        entities.remove(originalEntity);
        entityNames.remove(originalEntity.getName());

    }

    @Then("^the updated instance is returned$")
    public void the_updated_instance_is_returned() throws Throwable {
        CachedEntityExample updatedEntity = entities.iterator().next();
        assertTrue("Should have received the SAME instance of the object!", updatedEntity == retrievedEntity);       
                
        txManager.getUserTransaction().commit();

    }

    @Then("^the calculated field update is included$")
    public void the_calculated_field_update_is_included() throws Throwable {
        if (!isBusinessKeyed) {
            CachedEntityExample findByPkEntity = entityMaintenanceDelegate.findByPrimaryKey(retrievedEntity.getId());
            updateEntityCalculatedValue = findByPkEntity.getCalculatedField();
            txManager.getUserTransaction().commit();
        }                         
        
        assertNotEquals("Field calculated DURING the service call not reflected in the L1 cache!",
                originalEntityCalculatedValue, updateEntityCalculatedValue);

    }  

    @Then("^no entity is returned$")
    public void no_entity_is_returned() throws Throwable {
        assertNull("No object should have been returned within the transaction!", retrievedEntity);
        txManager.getUserTransaction().commit();

        // if we get this far, all entities have already been removed:
        entities.clear();

    }

    @Then("^the entities can be retrieved outside the original transaction$")
    public void the_entities_can_be_retrieved_outside_the_original_transaction() throws Throwable {
        Collection<CachedEntityExample> entitiesWithIds = new ArrayList<>();
        for (CachedEntityExample entity : entities) {
            CachedEntityExample savedEntity = businessDelegate.findByName(entity.getName());
            assertNotNull("Should have found a saved entity for name: " + entity.getName(), savedEntity);
            entitiesWithIds.add(savedEntity);
        }

        entities = entitiesWithIds;
    }

    @Then("^the updates can be retrieved outside the original transaction$")
    public void the_updates_can_be_retrieved_outside_the_original_transaction() throws Throwable {
        for (CachedEntityExample entity : entities) {
            CachedEntityExample savedEntity = businessDelegate.findByName(entity.getName());
            assertNotNull("Should have found an updated entity for name: " + entity.getName(), savedEntity);
        }
    }

    @Then("^the entities no longer exists outside the original transaction$")
    public void the_entities_no_longer_exists_outside_the_original_transaction() throws Throwable {
        for (CachedEntityExample originalEntity : entities) {
            CachedEntityExample foundEntity = businessDelegate.findByName(originalEntity.getName());
            assertNull("Entity SHOULD have been deleted!", foundEntity);

        }

        // if we get this far, we have validated they are all already gone:
        entities.clear();

    }

    @Then("^the new business keyed entity is returned$")
    public void the_new_business_keyed_entity_is_returned() throws Throwable {
        assertNotNull("A business keyed entity should be immediately available for reselect!", businessKeyedEntity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CachedEntityExample findOutsideCurrentTransaction(String name) {
        return businessDelegate.findByName(name);
    }

    protected void assertNameShouldNotBeFoundOutsideTransaction(String name) {
        CachedEntityExample outsideCurrentTxResult = findOutsideCurrentTransaction(name);
        assertNull("Should NOT be found before the transaction has committed - read isolation broken!",
                outsideCurrentTxResult);
    }

    protected void assertNameIsFound() {
        String name = entityNames.iterator().next();
        CachedEntityExample savedEntity = businessDelegate.findByName(name);
        assertNotNull("Should have found a saved entity for name: " + name, savedEntity);
        entities.add(savedEntity);
    }

    protected CachedEntityExample createAndValidateKeyExistsForEntity() {
        CachedEntityExample entity = new CachedEntityExample();
        entity.setName(RandomStringUtils.randomAlphabetic(10));
        entity = entityMaintenanceDelegate.create(entity);
        assertNotNull("Should have an id assigned after create is invoked!", entity.getId());

        entities.add(entity);

        return entity;
    }

}

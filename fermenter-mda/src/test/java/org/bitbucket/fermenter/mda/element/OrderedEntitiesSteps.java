package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.ModelContext;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;
import org.bitbucket.fermenter.mda.metamodel.element.ReferenceElement;
import org.bitbucket.fermenter.mda.metamodel.element.RelationElement;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OrderedEntitiesSteps extends AbstractEntitySteps {

    private static final String TEST_PACKAGE_NAME = "entity.ordering";

    private List<String> orderedEntityNames;

    @After("@orderedEntities")
    public void cleanUp() throws IOException {
        super.cleanUp();
        
        if (orderedEntityNames != null) {
            orderedEntityNames.clear();
        }

    }

    @Given("^the following entities:$")
    public void the_following_entities(List<EntityTestInfo> entityInfos) throws Throwable {
        createEntities(entityInfos);
    }

    @Given("^the following entities and their references:$")
    public void the_following_entities_and_their_references(List<EntityTestInfo> entityInfos) throws Throwable {
        createEntities(entityInfos);
    }
    
    @Given("^the following entities and their relations:$")
    public void the_following_entities_and_their_relations(List<EntityTestInfo> entityInfos) throws Throwable {
        createEntities(entityInfos);
    }    

    @When("^the entities are loaded$")
    public void the_entities_are_loaded() throws Throwable {
        this.readEntities();

        Set<Entity> entitiesByDependency = metadataRepo.getEntitiesByDependencyOrder(ModelContext.LOCAL.toString());

        orderedEntityNames = new ArrayList<>();
        for (Entity entity : entitiesByDependency) {
            orderedEntityNames.add(entity.getName());
        }

    }

    @Then("^the values are listed in the following order:$")
    public void the_values_are_listed_in_the_following_order(List<String> expectedOrder) throws Throwable {
        for (String expectedString : expectedOrder) {
            int expectedLocation = expectedOrder.indexOf(expectedString);
            int actualLocation = orderedEntityNames.indexOf(expectedString);
            assertEquals("Order not expected for value '" + expectedString + "'!", expectedLocation, actualLocation);
        }
    }

    @Then("^\"([^\"]*)\" is a precursor of \"([^\"]*)\"$")
    public void is_a_precursor_of(String precursor, String value) throws Throwable {
        int indexOfPrecursor = orderedEntityNames.indexOf(precursor);
        int indexOfValue = orderedEntityNames.indexOf(value);

        assertTrue("precursor is NOT before the value as expected!", indexOfPrecursor < indexOfValue);

    }

    protected void createEntities(List<EntityTestInfo> entityInfos)
            throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {
        for (EntityTestInfo entityInfo : entityInfos) {
            EntityElement entity = new EntityElement();
            entity.setName(entityInfo.entityName);
            entity.setPackage(TEST_PACKAGE_NAME);

            addReferencesToEntity(entityInfo, entity);
            addRelationsToEntity(entityInfo, entity);

            createEntityElement(entity);
        }
    }

    protected void addReferencesToEntity(EntityTestInfo entityInfo, EntityElement entity) {
        if (StringUtils.isNotBlank(entityInfo.references)) {
            List<String> references = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(entityInfo.references, ",");
            while (st.hasMoreTokens()) {
                references.add(st.nextToken().trim());
            }
            
            for (String referenceName : references) {
                ReferenceElement reference = new ReferenceElement();
                reference.setName(referenceName);
                reference.setType(referenceName);
                reference.setPackage(TEST_PACKAGE_NAME);
                entity.addReference(reference);
            }
        }
    }
    
    protected void addRelationsToEntity(EntityTestInfo entityInfo, EntityElement entity) {
        if (StringUtils.isNotBlank(entityInfo.relations)) {
            List<String> relations = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(entityInfo.relations, ",");
            while (st.hasMoreTokens()) {
                relations.add(st.nextToken().trim());
            }
            
            for (String referenceName : relations) {
                RelationElement relation = new RelationElement();
                relation.setType(referenceName);
                relation.setPackage(TEST_PACKAGE_NAME);
                entity.addRelation(relation);
            }
        }
    }    

    public class EntityTestInfo {
        public String entityName;
        public String references;
        public String relations;
    }

}

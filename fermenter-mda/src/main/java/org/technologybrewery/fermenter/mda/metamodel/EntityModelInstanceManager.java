package org.technologybrewery.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.technologybrewery.fermenter.mda.generator.GenerationException;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.mda.metamodel.element.EntityElement;
import org.technologybrewery.fermenter.mda.metamodel.element.Reference;
import org.technologybrewery.fermenter.mda.metamodel.element.Relation;
import org.technologybrewery.fermenter.mda.metamodel.element.RelationElement;

/**
 * Responsible for maintaining the list of entity model instances elements in the system.
 */
class EntityModelInstanceManager extends AbstractMetamodelManager<Entity> {

    private static ThreadLocal<EntityModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(EntityModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static EntityModelInstanceManager getInstance() {
        return threadBoundInstance.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        threadBoundInstance.remove();
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private EntityModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getEntitiesRelativePath();
    }

    @Override
    protected Class<EntityElement> getMetamodelClass() {
        return EntityElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return Entity.class.getSimpleName();
    }

    /**
     * Returns the set of dependencies in a manner that is ordered by references and relations such that referential
     * integrity will be respected.
     * 
     * @param context
     *            type of generation target context being used
     * @return ordered entities
     */
    public Set<Entity> getNamesByDependencyOrder(String context) {
        Map<String, List<String>> referencedObjects = new HashMap<>();

        Map<String, Entity> rawEntities = this.getMetadataElementByContext(context);

        for (Entity rawEntity : rawEntities.values()) {
            List<Reference> references = rawEntity.getReferences();
            for (Reference reference : references) {
                String type = reference.getType();
                List<String> inboundReferences = referencedObjects.computeIfAbsent(type, f -> new ArrayList<>());
                inboundReferences.add(rawEntity.getName());
            }
        }

        Set<Entity> dependencyOrderedEntities = new TreeSet<>(new EntityComparator(referencedObjects));

        for (Entity rawEntity : rawEntities.values()) {
            dependencyOrderedEntities.add(rawEntity);
        }

        return dependencyOrderedEntities;
    }

    /**
     * Iterate over loaded domains and register each relation on its parent. This enables bi-directional referencing of
     * relations. It is also important to note that while referring to a parent from a child is very similar to a
     * reference, it is typically not similar enough to assume that it will be implemented in this fashion. Separating
     * them into their own collection ensures that they can be dealt with as appropriate for the target implementation.
     */

    protected void postLoadMetamodel() {

        super.postLoadMetamodel();

        EntityElement entity;
        EntityElement childEntity;
        RelationElement relation;
        String relationType;
        List<Relation> relationMap;
        Iterator<Entity> entityMapIterator;
        Iterator<Relation> relationValueInterator;

        // Get the complete metadata map - if I get only get current application, client transfer objects does not get
        // generated with parent references
        Map<String, Entity> entityMap = getTargetMetadataMap();
        entityMapIterator = (entityMap != null) ? entityMap.values().iterator() : Collections.emptyIterator();
        while (entityMapIterator.hasNext()) {
            entity = (EntityElement) entityMapIterator.next();
            relationMap = entity.getRelations();

            relationValueInterator = (relationMap != null) ? relationMap.iterator() : Collections.emptyIterator();
            while (relationValueInterator.hasNext()) {
                relation = (RelationElement) relationValueInterator.next();
                relationType = relation.getType();
                // TODO: check 1-M and 1-1 only:
                childEntity = (EntityElement) entityMap.get(relationType);
                if (childEntity != null) {
                    childEntity.addInverseRelation(entity);
                } else {
                    throw new GenerationException("Could not find a relation to entity: " + relationType);
                }
            }

        }
    }

    private Map<String, Entity> getTargetMetadataMap() {
        Map<String, Entity> entityMap = new HashMap<>();

        List<String> targetedArtifactIds = repoConfiguration.getTargetModelInstances();
        for (String artifactId : targetedArtifactIds) {
            Map<String, Entity> targetedModelMap = getMetadataByArtifactIdMap(artifactId);
            if (targetedModelMap != null) {
                entityMap.putAll(targetedModelMap);
            }
        }
        return entityMap;
    }

}

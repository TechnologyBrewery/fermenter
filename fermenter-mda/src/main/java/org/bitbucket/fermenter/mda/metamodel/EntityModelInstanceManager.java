package org.bitbucket.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;

/**
 * Responsible for maintaining the list of entity model instances elements in the system.
 */
class EntityModelInstanceManager extends AbstractMetamodelManager<Entity> {

    private static final EntityModelInstanceManager instance = new EntityModelInstanceManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static EntityModelInstanceManager getInstance() {
        return instance;
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
     * @param packageName
     *            packageName of entities to retrieve
     * @return ordered entities
     */
    public Set<Entity> getNamesByDependencyOrder(String packageName) {                
        Map<String, List<String>> referencedObjects = new HashMap<>();
        
        Map<String, Entity> rawEntities = this.getMetadataElementByPackage(packageName);

        for (Entity rawEntity : rawEntities.values()) {
            List<Reference> references = rawEntity.getReferences();
            for (Reference reference : references) {
                List<String> inboundReferences = referencedObjects.computeIfAbsent(reference.getType(), f -> new ArrayList<>());
                inboundReferences.add(rawEntity.getName());
            }
        }
        
        Set<Entity> dependencyOrderedEntities = new TreeSet<>(new EntityComparator(referencedObjects));
        
        for (Entity rawEntity : rawEntities.values()) {
            dependencyOrderedEntities.add(rawEntity);
        }

        return dependencyOrderedEntities;
    }

}

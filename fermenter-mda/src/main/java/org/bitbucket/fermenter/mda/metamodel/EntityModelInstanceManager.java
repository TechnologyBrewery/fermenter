package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;

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

}

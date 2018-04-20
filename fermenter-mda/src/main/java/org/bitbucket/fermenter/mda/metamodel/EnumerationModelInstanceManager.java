package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;

/**
 * Responsible for maintaining the list of enumeration metadata elements in the system.
 */
class EnumerationModelInstanceManager extends AbstractMetamodelManager<Enumeration> {

    private static final EnumerationModelInstanceManager INSTANCE = new EnumerationModelInstanceManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static EnumerationModelInstanceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private EnumerationModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getEnumerationsRelationPath();
    }

    @Override
    protected Class<EnumerationElement> getMetamodelClass() {
        return EnumerationElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return Enumeration.class.getSimpleName();
    }

}

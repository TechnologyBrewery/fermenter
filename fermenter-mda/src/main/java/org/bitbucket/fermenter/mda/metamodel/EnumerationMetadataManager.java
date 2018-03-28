package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;

/**
 * Responsible for maintaining the list of enumeration metadata elements in the system.
 */
class EnumerationMetadataManager extends AbstractMetamodelManager<Enumeration> {

    private static final EnumerationMetadataManager INSTANCE = new EnumerationMetadataManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static EnumerationMetadataManager getInstance() {
        return INSTANCE;
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private EnumerationMetadataManager() {
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

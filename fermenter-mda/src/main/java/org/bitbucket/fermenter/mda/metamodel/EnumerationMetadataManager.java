package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;

/**
 * Responsible for maintaining the list of enumeration metadata elements in the system.
 */
class EnumerationMetadataManager extends AbstractMetamodelManager<Enumeration> {

    private static final EnumerationMetadataManager INSTANCE = new EnumerationMetadataManager();

    public static EnumerationMetadataManager getInstance() {
        return INSTANCE;
    }

    private EnumerationMetadataManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getEnumerationsRelationPath();
    }

    @Override
    protected Class<Enumeration> getMetamodelClass() {
        return Enumeration.class;
    }

}

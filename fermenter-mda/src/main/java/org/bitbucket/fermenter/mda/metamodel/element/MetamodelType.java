package org.bitbucket.fermenter.mda.metamodel.element;

import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

/**
 * Defines the different types of metamodels you can have while also encapsulating a mechanism to determine 
 * a type for a given package and name combination.
 */
public enum MetamodelType {

    SIMPLE_TYPE, ENUMERATION, ENTITY;

    private static DefaultModelInstanceRepository modelInstanceRepository = ModelInstanceRepositoryManager
            .getMetadataRepostory(DefaultModelInstanceRepository.class);
    private static MetadataRepository legacyMetadataRepository = ModelInstanceRepositoryManager
            .getMetadataRepostory(MetadataRepository.class);

    /**
     * Returns the metamodel type for the passed package and name combination.
     * 
     * @param packageName
     *            package name
     * @param name
     *            name of instance
     * @return metamodel type
     */
    public static MetamodelType getMetamodelType(String packageName, String name) {
        MetamodelType metamodelType = null;
        
        String lookupPackageName = packageName == null ? modelInstanceRepository.getBasePackage() : packageName; 
        
        if (modelInstanceRepository.getEnumeration(lookupPackageName, name) != null) {
            metamodelType = MetamodelType.ENUMERATION;

            // TODO: update w/ package once entities are migrated:
        } else if (legacyMetadataRepository.getEntity(name) != null) {
            metamodelType = MetamodelType.ENTITY;

        } else if (!"void".equals(name)) {
            metamodelType = MetamodelType.SIMPLE_TYPE;

        }

        return metamodelType;
    }

}

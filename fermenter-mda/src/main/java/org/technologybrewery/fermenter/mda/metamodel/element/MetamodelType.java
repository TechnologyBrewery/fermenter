package org.technologybrewery.fermenter.mda.metamodel.element;

import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

/**
 * Defines the different types of metamodels you can have while also encapsulating a mechanism to determine 
 * a type for a given package and name combination.
 */
public enum MetamodelType {

    SIMPLE_TYPE, ENUMERATION, ENTITY, DICTIONARY_TYPE;

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
        DefaultModelInstanceRepository modelInstanceRepository = getModelInstanceRepository();
        
        String lookupPackageName = packageName == null ? modelInstanceRepository.getBasePackage() : packageName; 
        
        if (modelInstanceRepository.getDictionaryType(name) != null) {
            metamodelType = MetamodelType.DICTIONARY_TYPE;

        } else if (modelInstanceRepository.getEnumeration(lookupPackageName, name) != null) {
            metamodelType = MetamodelType.ENUMERATION;

        } else if (modelInstanceRepository.getEntity(lookupPackageName, name) != null) {
            metamodelType = MetamodelType.ENTITY;

        } else if (!"void".equals(name)) {
            metamodelType = MetamodelType.SIMPLE_TYPE;

        }

        return metamodelType;
    }

    static DefaultModelInstanceRepository getModelInstanceRepository() {
        return ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
    }

}

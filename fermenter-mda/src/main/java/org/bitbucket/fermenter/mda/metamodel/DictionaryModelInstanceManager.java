package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.DictionaryType;
import org.bitbucket.fermenter.mda.metamodel.element.DictionaryTypeElement;

/**
 * Responsible for maintaining the list of dictionary type model instances elements in the system.
 */
class DictionaryModelInstanceManager extends AbstractMetamodelManager<DictionaryType> {

    private static final DictionaryModelInstanceManager instance = new DictionaryModelInstanceManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static DictionaryModelInstanceManager getInstance() {
        return instance;
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private DictionaryModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getDictionaryTypesRelativePath();
    }

    @Override
    protected Class<DictionaryTypeElement> getMetamodelClass() {
        return DictionaryTypeElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return DictionaryType.class.getSimpleName();
    }

}

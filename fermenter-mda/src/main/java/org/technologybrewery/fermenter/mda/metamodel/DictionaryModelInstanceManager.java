package org.technologybrewery.fermenter.mda.metamodel;

import org.technologybrewery.fermenter.mda.metamodel.element.DictionaryType;
import org.technologybrewery.fermenter.mda.metamodel.element.DictionaryTypeElement;

/**
 * Responsible for maintaining the list of dictionary type model instances elements in the system.
 */
class DictionaryModelInstanceManager extends AbstractMetamodelManager<DictionaryType> {

    private static ThreadLocal<DictionaryModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(DictionaryModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static DictionaryModelInstanceManager getInstance() {
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

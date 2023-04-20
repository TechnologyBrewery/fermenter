package org.technologybrewery.fermenter.mda.metamodel;

import org.technologybrewery.fermenter.mda.metamodel.element.Enumeration;
import org.technologybrewery.fermenter.mda.metamodel.element.EnumerationElement;

/**
 * Responsible for maintaining the list of enumeration model instances elements in the system.
 */
class EnumerationModelInstanceManager extends AbstractMetamodelManager<Enumeration> {

    private static ThreadLocal<EnumerationModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(EnumerationModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static EnumerationModelInstanceManager getInstance() {
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
    private EnumerationModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getEnumerationsRelativePath();
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

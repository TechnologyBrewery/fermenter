package org.technologybrewery.fermenter.mda.metamodel;

import org.technologybrewery.fermenter.mda.metamodel.element.Service;
import org.technologybrewery.fermenter.mda.metamodel.element.ServiceElement;

/**
 * Responsible for maintaining the list of service model instances elements in the system.
 */
class ServiceModelInstanceManager extends AbstractMetamodelManager<Service> {

    private static ThreadLocal<ServiceModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(ServiceModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static ServiceModelInstanceManager getInstance() {
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
    private ServiceModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getServicesRelativePath();
    }

    @Override
    protected Class<ServiceElement> getMetamodelClass() {
        return ServiceElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return Service.class.getSimpleName();
    }

}

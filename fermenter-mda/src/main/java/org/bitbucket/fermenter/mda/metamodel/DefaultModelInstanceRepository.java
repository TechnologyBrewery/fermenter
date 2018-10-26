package org.bitbucket.fermenter.mda.metamodel;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.exception.FermenterException;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.element.Service;
import org.bitbucket.fermenter.mda.util.MessageTracker;

/**
 * Default implementation to serve up model instances of a specific metamodel type.
 */
public class DefaultModelInstanceRepository extends AbstractModelInstanceRepository {

    private static final Log log = LogFactory.getLog(DefaultModelInstanceRepository.class);

    private EnumerationModelInstanceManager enumerationManager = EnumerationModelInstanceManager.getInstance();
    private ServiceModelInstanceManager serviceManager = ServiceModelInstanceManager.getInstance();

    /**
     * Creates a new instance w/ the base package of the current project. This package name will become the default
     * package where no other is specified.
     * 
     * @param config
     *            configuration
     */
    public DefaultModelInstanceRepository(ModelRepositoryConfiguration config) {
        super(config);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load() {
        enumerationManager.reset();
        serviceManager.reset();

        Collection<MetadataUrl> metadataUrls = config.getMetamodelInstanceLocations().values();
        for (MetadataUrl metadataUrl : metadataUrls) {
            long start = System.currentTimeMillis();

            enumerationManager.loadMetadata(metadataUrl, config);
            serviceManager.loadMetadata(metadataUrl, config);

            if (log.isInfoEnabled()) {
                long stop = System.currentTimeMillis();
                log.info("Metamodel instances for artifactId '" + metadataUrl.getArtifactId() + "' have been loaded - "
                        + (stop - start) + "ms");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        String basePackage = config.getBasePackage();
        for (Enumeration enumeration : enumerationManager.getMetadataElementByPackage(basePackage).values()) {
            enumeration.validate();
        }

        for (Service service : serviceManager.getMetadataElementByPackage(basePackage).values()) {
            service.validate();
        }
        
        MessageTracker messageTracker = MessageTracker.getInstance();
        messageTracker.emitMessages(log);
        
        if (messageTracker.hasErrors()) {
        	throw new GenerationException("Encountered one or more error!  Please check your Maven output for details.");
        }

    }

    /**
     * Gets an enumeration by name from the current package.
     * 
     * @param name
     *            name of the enumeration to look up
     * @return instance of the {@link Enumeration} or null if none is found with the request name
     */
    public Enumeration getEnumeration(String name) {
        return enumerationManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an enumeration by name from the current package.
     * 
     * @param name
     *            name of the enumeration to look up
     * @param packageName
     *            the package in which to look for the request element
     * @return instance of the {@link Enumeration} or null if none is found with the request name
     */
    public Enumeration getEnumeration(String packageName, String name) {
        return enumerationManager.getMetadataElementByPackageAndName(packageName, name);

    }

    /**
     * Gets all enumerations from the specified package.
     * 
     * @param packageName
     *            the requested package
     * @return all enumerations within the request package, keyed by name
     */
    public Map<String, Enumeration> getEnumerations(String packageName) {
        return enumerationManager.getMetadataElementByPackage(packageName);
    }

    /**
     * Gets all enumerations from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id
     * @return all enumerations within the request artifact id, keyed by name
     */
    public Map<String, Enumeration> getEnumerationsByArtifactId(String artifactId) {
        return enumerationManager.getMetadataByArtifactIdMap(artifactId);
    }
    
    /**
     * Retrieves enumerations based on a generation context.
     * 
     * @param context
     *            type of generation target context being used
     * @return map of enumerations
     */
    public Map<String, Enumeration> getEnumerationsByContext(String context) {
        return enumerationManager.getMetadataElementByContext(context);
    }    

    /**
     * Gets an service by name from the current package.
     * 
     * @param name
     *            name of the service to look up
     * @return instance of the {@link Service} or null if none is found with the request name
     */
    public Service getService(String name) {
        return serviceManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an service by name from the current package.
     * 
     * @param name
     *            name of the service to look up
     * @param packageName
     *            the package in which to look for the request element
     * @return instance of the {@link Service} or null if none is found with the request name
     */
    public Service getService(String packageName, String name) {
        return serviceManager.getMetadataElementByPackageAndName(packageName, name);

    }

    /**
     * Gets all services from the specified package.
     * 
     * @param packageName
     *            the requested package
     * @return all services within the request package, keyed by name
     */
    public Map<String, Service> getServices(String packageName) {
        return serviceManager.getMetadataElementByPackage(packageName);
    }

    /**
     * Gets all services from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id
     * @return all services within the request artifact id, keyed by name
     */
    public Map<String, Service> getServicesByArtifactId(String artifactId) {
        return serviceManager.getMetadataByArtifactIdMap(artifactId);
    }

    /**
     * Retrieves services based on a generation context.
     * 
     * @param context
     *            type of generation target context being used
     * @return map of services
     */
    public Map<String, Service> getServicesByContext(String context) {
        return serviceManager.getMetadataElementByContext(context);
    }

}

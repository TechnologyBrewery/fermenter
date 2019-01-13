package org.bitbucket.fermenter.mda.metamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.element.Service;
import org.bitbucket.fermenter.mda.util.MessageTracker;

/**
 * Default implementation to serve up model instances of a specific metamodel type.
 */
public class DefaultModelInstanceRepository extends AbstractModelInstanceRepository {

    private static final Log log = LogFactory.getLog(DefaultModelInstanceRepository.class);

    private EnumerationModelInstanceManager enumerationManager = EnumerationModelInstanceManager.getInstance();
    private EntityModelInstanceManager entityManager = EntityModelInstanceManager.getInstance();
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
        entityManager.reset();

        Collection<ModelInstanceUrl> modelInstanceUrls = config.getMetamodelInstanceLocations().values();
        for (ModelInstanceUrl modelInstanceUrl : modelInstanceUrls) {
            long start = System.currentTimeMillis();

            enumerationManager.loadMetadata(modelInstanceUrl, config);
            serviceManager.loadMetadata(modelInstanceUrl, config);
            entityManager.loadMetadata(modelInstanceUrl, config);

            if (log.isInfoEnabled()) {
                long stop = System.currentTimeMillis();
                log.info("Metamodel instances for artifactId '" + modelInstanceUrl.getArtifactId() + "' have been loaded - "
                        + (stop - start) + "ms");
            }
        }
    }
    
    public Set<String> getArtifactIds() {
        Set<String> artifactIds = new HashSet<>();
        Collection<ModelInstanceUrl> urls = config.getMetamodelInstanceLocations().values();
        for(ModelInstanceUrl url: urls) {
            artifactIds.add(url.getArtifactId());
        }
        return artifactIds;
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
        
        for (Entity entity : entityManager.getMetadataElementByPackage(basePackage).values()) {
            entity.validate();
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
    
    /**
     * Gets an entity by name from the current package.
     * 
     * @param name
     *            name of the entity to look up
     * @return instance of the {@link Entity} or null if none is found with the request name
     */
    public Entity getEntity(String name) {
        return entityManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an entity by name from the current package.
     * 
     * @param name
     *            name of the entity to look up
     * @param packageName
     *            the package in which to look for the request element
     * @return instance of the {@link Entity} or null if none is found with the request name
     */
    public Entity getEntity(String packageName, String name) {
        return entityManager.getMetadataElementByPackageAndName(packageName, name);

    }

    /**
     * Gets all entities from the specified package.
     * 
     * @param packageName
     *            the requested package
     * @return all entities within the request package, keyed by name
     */
    public Map<String, Entity> getEntities(String packageName) {
        return entityManager.getMetadataElementByPackage(packageName);
    }

    /**
     * Gets all entities from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id
     * @return all entities within the request artifact id, keyed by name
     */
    public Map<String, Entity> getEntitiesByArtifactId(String artifactId) {
        return entityManager.getMetadataByArtifactIdMap(artifactId);
    }

    /**
     * Retrieves entities based on a generation context.
     * 
     * @param context
     *            type of generation target context being used
     * @return map of entities
     */
    public Map<String, Entity> getEntitiesByContext(String context) {
        return entityManager.getMetadataElementByContext(context);
    }    

}

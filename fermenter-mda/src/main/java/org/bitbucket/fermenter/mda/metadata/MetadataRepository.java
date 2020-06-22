package org.bitbucket.fermenter.mda.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.mda.metamodel.ModelContext;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;

public class MetadataRepository extends AbstractMetadataRepository {

    private static final Log LOG = LogFactory.getLog(MetadataRepository.class);

    public MetadataRepository(ModelRepositoryConfiguration config) {
        super(config);

    }

    /**
     * {@inheritDoc}
     */
    public void load() {
        try {
            loadAllMetadata();

        } catch (Exception ex) {
            throw new GenerationException(
                    "Unable to load metadata for application " + config.getArtifactId(), ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void validate() {
        CompositeMetadataManager.getInstance().validate();
        EntityMetadataManager.getInstance().validate();
        ServiceMetadataManager.getInstance().validate();
        EnumerationMetadataManager.getInstance().validate();
    }

    public Entity getEntity(String entityName) {
        Map<String, Entity> entityMap = getAllEntities();
        return entityMap.get(entityName);
    }

    public Entity getEntity(String applicationName, String entityName) {
        Map<String, Entity> entityMap = getAllEntities(applicationName);
        return entityMap.get(entityName);
    }

    /**
     * Returns entities based on the target's metadata context. By default, this will be "local" (the current
     * application) rather than "all", which would return metadata from local and added metadata dependencies.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @return Map of entities keyed by name
     */
    public Map<String, Entity> getEntitiesByMetadataContext(String context, String currentApplication) {
        return getEntitiesByMetadataContext(context, currentApplication, null);
    }

    /**
     * Returns entities based on the target's metadata context and the repository's configuration. If the context is
     * 'targeted', then the respository's generation targets will be used. Otherwise, normal 'local' and 'all' behavior
     * is observed.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @return Map of entities keyed by name
     */
    public Map<String, Entity> getEntitiesByMetadataContext(String context) {
        return getEntitiesByMetadataContext(context, config.getArtifactId(), config.getTargetModelInstances());
    }

    /**
     * Returns entities based on the target's metadata context and the repository's configuration. If the context is
     * 'targeted', then the respository's generation targets will be used. Otherwise, normal 'local' and 'all' behavior
     * is observed.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @param targetedArtifactIds
     *            list of artifact ids for which to find metadata
     * @return Map of entities keyed by name
     */
    public Map<String, Entity> getEntitiesByMetadataContext(String context, String currentApplication,
            List<String> targetedArtifactIds) {
        Map<String, Entity> entityMap;
        if (ModelContext.useLocalModelInstancesOnly(context)) {
            entityMap = getAllEntities(currentApplication);
        } else if (ModelContext.useTargetedModelInstances(context) && targetedArtifactIds != null) {
            entityMap = new HashMap<>();
            for (String artifactId : targetedArtifactIds) {
                Map<String, Entity> targetedEntityMap = getAllEntities(artifactId);
                entityMap.putAll(targetedEntityMap);
                if (targetedEntityMap.size() == 0) {
                    LOG.warn("No entities were found for targeted artifactId '" + artifactId + "'!");
                }
            }
        } else {
            entityMap = getAllEntities();
        }

        return entityMap;
    }

    public Map<String, Entity> getAllEntities() {
        return EntityMetadataManager.getInstance().getCompleteMetadataMap();
    }

    public Map<String, Entity> getAllEntities(String applicationName) {
        return EntityMetadataManager.getEntities(applicationName);
    }
    
    public Set<Entity> getEntitiesByDependencyOrder(String context, String applicationName) {
        Map<String, List<String>> referencedObjects = new HashMap<>();
        
        Map<String, Entity> rawEntities = getEntitiesByMetadataContext(context, applicationName);

        for (Entity rawEntity : rawEntities.values()) {
            Map<String, Reference> references = rawEntity.getReferences();
            for (Reference reference : references.values()) {
                List<String> inboundReferences = referencedObjects.computeIfAbsent(reference.getType(), f -> new ArrayList<>());
                inboundReferences.add(rawEntity.getName());
            }
        }               
        
        Set<Entity> dependencyOrderedEntities = new TreeSet<>(new LegacyEntityComparitor(referencedObjects));

        for (Entity rawEntity : rawEntities.values()) {
            dependencyOrderedEntities.add(rawEntity);
            
        }        

        return dependencyOrderedEntities;
    }

    @Deprecated
    public Service getService(String serviceName) {
        Map<String, Service> serviceMap = getAllServices();
        return serviceMap.get(serviceName);
    }
    
    @Deprecated
    public Service getService(String applicationName, String serviceName) {
        Map<String, Service> serviceMap = getAllServices(applicationName);
        return serviceMap.get(serviceName);
    }

    private void loadAllMetadata() {
        if (config != null) {
        	FormatMetadataManager formatManager = FormatMetadataManager.getInstance();
            formatManager.reset();
            CompositeMetadataManager compositeManager = CompositeMetadataManager.getInstance();
            compositeManager.reset();
            EntityMetadataManager entityManager = EntityMetadataManager.getInstance();
            entityManager.reset();
            ServiceMetadataManager serviceManager = ServiceMetadataManager.getInstance();
            serviceManager.reset();
            EnumerationMetadataManager enumerationManager = EnumerationMetadataManager.getInstance();
            enumerationManager.reset();
            Collection<ModelInstanceUrl> urls = config.getMetamodelInstanceLocations().values();
            for (ModelInstanceUrl url : urls) {
                long start = System.currentTimeMillis();
                compositeManager.loadMetadata(url.getArtifactId(), url.getUrl());
                entityManager.loadMetadata(url.getArtifactId(), url.getUrl());
                serviceManager.loadMetadata(url.getArtifactId(), url.getUrl());
                enumerationManager.loadMetadata(url.getArtifactId(), url.getUrl());
                
                // Load format information for the current project only                
                formatManager.loadMetadata(url.getUrl());

                if (config.getArtifactId().equals(url.getArtifactId())) {
                    // Messages metadata only needs to be loaded for the current project
                    MessagesMetadataManager messagesManager = MessagesMetadataManager.getInstance();
                    messagesManager.reset();
                    MessagesMetadataManager.getInstance().loadMetadata(url.getUrl());
                   
                }
                if (LOG.isInfoEnabled()) {
                    long stop = System.currentTimeMillis();
                    LOG.info("Metadata for application '" + url.getArtifactId() + "' has been loaded - "
                            + (stop - start) + "ms");
                }

            }
        } else {
            LOG.warn("No properties provided, unable to load any metadata!");
        }
    }

    public Map<String, Enumeration> getAllEnumerations() {
        return EnumerationMetadataManager.getInstance().getCompleteMetadataMap();
    }

    public Map<String, Enumeration> getAllEnumerations(String applicationName) {
        return EnumerationMetadataManager.getEnumerations(applicationName);
    }

    /**
     * Returns enumerations based on the target's metadata context. By default, this will be "local" (the current
     * application) rather than "all", which would return metadata from local and added metadata dependencies.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @return Map of enumerations keyed by name
     */
    public Map<String, Enumeration> getEnumerationsByMetadataContext(String context, String currentApplication) {
        Map<String, Enumeration> enumerationMap;
        if (ModelContext.useLocalModelInstancesOnly(context)) {
            enumerationMap = getAllEnumerations(currentApplication);
        } else {
            enumerationMap = getAllEnumerations();
        }

        return enumerationMap;
    }

    public Enumeration getEnumeration(String type) {
        Map<String, Enumeration> enumMap = getAllEnumerations();
        return enumMap.get(type);
    }

    public Enumeration getEnumeration(String applicationName, String type) {
        Map<String, Enumeration> enumMap = getAllEnumerations(applicationName);
        return enumMap.get(type);
    }

    public Map<String, Service> getAllServices() {
        return ServiceMetadataManager.getInstance().getCompleteMetadataMap();
    }

    public Map<String, Service> getAllServices(String applicationName) {
        return ServiceMetadataManager.getServices(applicationName);
    }

    /**
     * Returns services based on the target's metadata context. By default, this will be "local" (the current
     * application) rather than "all", which would return metadata from local and added metadata dependencies.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @return Map of services keyed by name
     */
    @Deprecated
    public Map<String, Service> getServicesByMetadataContext(String context) {
        return getServicesByMetadataContext(context, config.getArtifactId(), config.getTargetModelInstances());
    }

    /**
     * Returns services based on the target's metadata context. By default, this will be "local" (the current
     * application) rather than "all", which would return metadata from local and added metadata dependencies.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @return Map of services keyed by name
     */
    @Deprecated
    public Map<String, Service> getServicesByMetadataContext(String context, String currentApplication) {
        return getServicesByMetadataContext(context, currentApplication, null);
    }

    /**
     * Returns services based on the target's metadata context. By default, this will be "local" (the current
     * application) rather than "all", which would return metadata from local and added metadata dependencies.
     * 
     * @param context
     *            context value
     * @param currentApplication
     *            current application to use for lookup if a local lookup
     * @param targetedArtifactIds
     *            list of artifact ids for which to find metadata
     * @return Map of services keyed by name
     */
    @Deprecated
    public Map<String, Service> getServicesByMetadataContext(String context, String currentApplication,
            List<String> targetedArtifactIds) {
        Map<String, Service> serviceMap;
        if (ModelContext.useLocalModelInstancesOnly(context)) {
            serviceMap = getAllServices(currentApplication);
        } else if (ModelContext.useTargetedModelInstances(context)) {
            serviceMap = new HashMap<>();
            for (String artifactId : targetedArtifactIds) {
                Map<String, Service> targetedServiceMap = getAllServices(artifactId);
                serviceMap.putAll(targetedServiceMap);
                if (targetedServiceMap.size() == 0) {
                    LOG.warn("No services were found for targeted artifactId '" + artifactId + "'!");
                }
            }
        } else {
            serviceMap = getAllServices();
        }

        return serviceMap;
    }

    public Map getAllComposites() {
        return CompositeMetadataManager.getInstance().getCompleteMetadataMap();
    }

    public Map getAllComposites(String applicationName) {
        return CompositeMetadataManager.getComposites(applicationName);
    }

    public Composite getComposite(String applicationName, String compositeType) {
        Map composites = getAllComposites(applicationName);
        return (Composite) composites.get(compositeType);
    }

    public Composite getComposite(String compositeType) {
        Map composites = getAllComposites();
        return (Composite) composites.get(compositeType);
    }
}

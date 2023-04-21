package org.technologybrewery.fermenter.mda.metamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;
import org.technologybrewery.fermenter.mda.metamodel.element.DictionaryType;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.mda.metamodel.element.Enumeration;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroup;
import org.technologybrewery.fermenter.mda.metamodel.element.Rule;
import org.technologybrewery.fermenter.mda.metamodel.element.Service;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

/**
 * Default implementation to serve up model instances of a specific metamodel type.
 */
public class DefaultModelInstanceRepository extends AbstractModelInstanceRepository {

    private static final Log log = LogFactory.getLog(DefaultModelInstanceRepository.class);

    private EnumerationModelInstanceManager enumerationManager = EnumerationModelInstanceManager.getInstance();
    private EntityModelInstanceManager entityManager = EntityModelInstanceManager.getInstance();
    private ServiceModelInstanceManager serviceManager = ServiceModelInstanceManager.getInstance();
    private DictionaryModelInstanceManager dictionaryManager = DictionaryModelInstanceManager.getInstance();
    private MessageGroupModelInstanceManager messageGroupManager = MessageGroupModelInstanceManager.getInstance();
    private RuleModelInstanceManager ruleManager = RuleModelInstanceManager.getInstance();

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
        dictionaryManager.reset();
        serviceManager.reset();
        entityManager.reset();
        messageGroupManager.reset();
        ruleManager.reset();
        
        Collection<ModelInstanceUrl> modelInstanceUrls = config.getMetamodelInstanceLocations().values();
        for (ModelInstanceUrl modelInstanceUrl : modelInstanceUrls) {
            long start = System.currentTimeMillis();
            enumerationManager.loadMetadata(modelInstanceUrl, config);
            dictionaryManager.loadMetadata(modelInstanceUrl, config);
            serviceManager.loadMetadata(modelInstanceUrl, config);
            entityManager.loadMetadata(modelInstanceUrl, config);
            messageGroupManager.loadMetadata(modelInstanceUrl, config);
            ruleManager.loadMetadata(modelInstanceUrl, config);

            if (log.isInfoEnabled()) {
                long stop = System.currentTimeMillis();
                log.info("Metamodel instances for artifactId '" + modelInstanceUrl.getArtifactId()
                        + "' have been loaded - " + (stop - start) + "ms");
            }
        }
    }

    public Set<String> getArtifactIds() {
        Set<String> artifactIds = new HashSet<>();
        Collection<ModelInstanceUrl> urls = config.getMetamodelInstanceLocations().values();
        for (ModelInstanceUrl url : urls) {
            artifactIds.add(url.getArtifactId());
        }
        return artifactIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        for (Enumeration enumeration : enumerationManager.getMetadataElementWithoutPackage().values()) {
            enumeration.validate();
        }

        for (DictionaryType dictionaryType : dictionaryManager.getMetadataElementWithoutPackage().values()) {
            dictionaryType.validate();
        }

        for (Service service : serviceManager.getMetadataElementWithoutPackage().values()) {
            service.validate();
        }

        for (Entity entity : entityManager.getMetadataElementWithoutPackage().values()) {
            entity.validate();
        }
        
        for (MessageGroup messageGroup : messageGroupManager.getMetadataElementWithoutPackage().values()) {
            messageGroup.validate();
        }

        for (Rule rule : ruleManager.getMetadataElementWithoutPackage().values()) {
            rule.validate();
        }

        MessageTracker messageTracker = MessageTracker.getInstance();
        messageTracker.emitMessages(log);

        if (messageTracker.hasErrors()) {
            throw new GenerationException(
                    "Encountered one or more error!  Please check your Maven output for details.");
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
     * Gets a dictionary type by name from the current package.
     * 
     * @param name
     *            name of the dictionary type to look up.
     * @return instance of the {@link DictionaryType} or null if none is found with the request name.
     */
    public DictionaryType getDictionaryType(String name) {
        return dictionaryManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);
    }

    /**
     * Gets all dictionary types from the current package.
     * 
     * @return all dictionary types within the current package, keyed by name.
     */
    public Map<String, DictionaryType> getDictionaryTypes() {
        return dictionaryManager.getMetadataElementWithoutPackage();
    }

    /**
     * Gets all dictionary types from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id.
     * @return all dictionary types within the request artifact id, keyed by name.
     */
    public Map<String, DictionaryType> getDictionaryTypesByArtifactId(String artifactId) {
        return dictionaryManager.getMetadataByArtifactIdMap(artifactId);
    }

    /**
     * Retrieves dictionary types based on a generation context.
     * 
     * @param context
     *            type of generation target context being used.
     * @return map of dictionary types.
     */
    public Map<String, DictionaryType> getDictionaryTypesByContext(String context) {
        return dictionaryManager.getMetadataElementByContext(context);
    }

    /**
     * Gets an service by name from the current package.
     * 
     * @param name
     *            name of the service to look up.
     * @return instance of the {@link Service} or null if none is found with the request name.
     */
    public Service getService(String name) {
        return serviceManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an service by name from the current package.
     * 
     * @param name
     *            name of the service to look up.
     * @param packageName
     *            the package in which to look for the request element.
     * @return instance of the {@link Service} or null if none is found with the request name.
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

    /**
     * Gets all entities ordered by their intrinsic dependencies. References are upstream, relations downstream,
     * otherwise they are equal.
     * 
     * @param context
     *            type of generation target context being used
     * @return all entities within the request package
     */
    public Set<Entity> getEntitiesByDependencyOrder(String context) {
        return entityManager.getNamesByDependencyOrder(context);
    }
    
    /**
     * Gets an message group by name from the current package.
     * 
     * @param name
     *            name of the message group to look up
     * @return instance of the {@link MessageGroup} or null if none is found with the request name
     */
    public MessageGroup getMessageGroup(String name) {
        return messageGroupManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an message group by name from the passed package.
     * 
     * @param name
     *            name of the message group to look up
     * @param packageName
     *            the package in which to look for the request element
     * @return instance of the {@link MessageGroup} or null if none is found with the request name
     */
    public MessageGroup getMessageGroup(String packageName, String name) {
        return messageGroupManager.getMetadataElementByPackageAndName(packageName, name);

    }

    /**
     * Gets all message groups from the specified package.
     * 
     * @param packageName
     *            the requested package
     * @return all message groups within the request package, keyed by name
     */
    public Map<String, MessageGroup> getMessageGroups(String packageName) {
        return messageGroupManager.getMetadataElementByPackage(packageName);
    }

    /**
     * Gets all message groups from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id
     * @return all message groups within the request artifact id, keyed by name
     */
    public Map<String, MessageGroup> getMessageGroupsByArtifactId(String artifactId) {
        return messageGroupManager.getMetadataByArtifactIdMap(artifactId);
    }

    /**
     * Retrieves message groups based on a generation context.
     * 
     * @param context
     *            type of generation target context being used
     * @return map of message groups
     */
    public Map<String, MessageGroup> getMessageGroupsByContext(String context) {
        return messageGroupManager.getMetadataElementByContext(context);
    }    

    /**
     * Gets a rule by name from the current package.
     * 
     * @param name
     *            name of the rule to look up.
     * @return instance of the {@link Rule} or null if none is found with the request name.
     */
    public Rule getRule(String name) {
        return ruleManager.getMetadataElementByPackageAndName(config.getBasePackage(), name);

    }

    /**
     * Gets an rule by name from the current package.
     * 
     * @param name
     *            name of the rule to look up.
     * @param packageName
     *            the package in which to look for the request element.
     * @return instance of the {@link Rule} or null if none is found with the request name.
     */
    public Rule getRule(String packageName, String name) {
        return ruleManager.getMetadataElementByPackageAndName(packageName, name);

    }

    /**
     * Gets all rules from the specified package.
     * 
     * @param packageName
     *            the requested package
     * @return all rules within the request package, keyed by name
     */
    public Map<String, Rule> getRules(String packageName) {
        return ruleManager.getMetadataElementByPackage(packageName);
    }

    /**
     * Gets all rules from the specified artifact id.
     * 
     * @param artifactId
     *            the requested artifact id
     * @return all rules within the request artifact id, keyed by name
     */
    public Map<String, Rule> getRulesByArtifactId(String artifactId) {
        return ruleManager.getMetadataByArtifactIdMap(artifactId);
    }

    /**
     * Retrieves rules based on a generation context.
     * 
     * @param context
     *            type of generation target context being used
     * @return map of rules
     */
    public Map<String, Rule> getRulesByContext(String context) {
        return ruleManager.getMetadataElementByContext(context);
    }

}

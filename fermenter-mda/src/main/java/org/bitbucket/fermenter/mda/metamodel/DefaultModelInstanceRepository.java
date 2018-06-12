package org.bitbucket.fermenter.mda.metamodel;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;

/**
 * Default implementation to serve up model instances of a specific metamodel type.
 */
public class DefaultModelInstanceRepository extends AbstractModelInstanceRepository {

    private static final Log log = LogFactory.getLog(DefaultModelInstanceRepository.class);

    private EnumerationModelInstanceManager enumerationManager = EnumerationModelInstanceManager.getInstance();

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

        Collection<MetadataUrl> metadataUrls = config.getMetamodelInstanceLocations().values();
        for (MetadataUrl metadataUrl : metadataUrls) {
            long start = System.currentTimeMillis();

            enumerationManager.loadMetadata(metadataUrl);

            if (log.isInfoEnabled()) {
                long stop = System.currentTimeMillis();
                log.info("Metadata for artifactId '" + metadataUrl.getArtifactId() + "' has been loaded - "
                        + (stop - start) + "ms");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        for (Enumeration enumeration : enumerationManager.getMetadataElementByPackage(config.getBasePackage()).values()) {
            enumeration.validate();
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

}

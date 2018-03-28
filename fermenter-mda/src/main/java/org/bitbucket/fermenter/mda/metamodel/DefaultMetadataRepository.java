package org.bitbucket.fermenter.mda.metamodel;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;

/**
 * Default metadata repository to serve up out of the box metamodel instances (metadata).
 */
public class DefaultMetadataRepository extends AbstractMetadataRepository {

    private static final Log log = LogFactory.getLog(DefaultMetadataRepository.class);

    private MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);

    private EnumerationMetadataManager enumerationManager = EnumerationMetadataManager.getInstance();

    /**
     * Creates a new instance w/ the base package of the current project. This package name will become the default
     * package where no other is specified.
     * 
     * @param basePackage
     *            package name
     */
    public DefaultMetadataRepository(String basePackage) {
        super(basePackage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(Properties properties) {
        MetadataUrlResolver loader = createMetadataResolverInstance();

        enumerationManager.reset();

        List<MetadataUrl> metadataUrls = loader.getMetadataURLs(properties);
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
    public void validate(Properties properties) {
        // Nothing to do so far with enumerations - will expand with other types of metadata

    }

    private MetadataUrlResolver createMetadataResolverInstance() {
        String metadataLoaderClass = config.getUrlResolver();
        MetadataUrlResolver loader;
        try {
            Class<?> clazz = Class.forName(metadataLoaderClass);
            loader = (MetadataUrlResolver) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new GenerationException("Could not create a MetadataUrlResolver instance!", e);
        }
        return loader;
    }

    /**
     * Gets an enumeration by name from the current package.
     * 
     * @param name
     *            name of the enumeration to look up
     * @return instance of the {@link Enumeration} or null if none is found with the request name
     */
    public Enumeration getEnumeration(String name) {
        return enumerationManager.getMetadataElementByPackageAndName(basePackage, name);

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

}

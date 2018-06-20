package org.bitbucket.fermenter.mda.metadata;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;

/**
 * Provides common methods needed for a metadata repository.
 * 
 */
public abstract class AbstractMetadataRepository implements ModelInstanceRepository {

    private static final Log LOG = LogFactory.getLog(AbstractMetadataRepository.class);

    protected ModelRepositoryConfiguration config;

    /**
     * Instantiates this instance with any applicable properties.
     * 
     * @param config
     *            model configuration
     */
    protected AbstractMetadataRepository(ModelRepositoryConfiguration config) {
        this.config = config;
    }

    /**
     * Returns the application name.
     * 
     * @return application name
     */
    public String getApplicationName() {
        return config.getCurrentApplicationName();
    }

    protected boolean useLocalMetadataOnly(String metadataContext) {
        boolean useLocalMetadataOnly = true;
        if (StringUtils.isBlank(metadataContext)
                || ModelRepositoryConfiguration.ALL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)
                || ModelRepositoryConfiguration.TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)) {
            useLocalMetadataOnly = false;

        } else if (ModelRepositoryConfiguration.LOCAL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)) {
            useLocalMetadataOnly = true;

        } else {
            useLocalMetadataOnly = false;
            LOG.warn("An invalid metadata context of '" + metadataContext
                    + "' has been specified.  Using 'all' instead!");
        }

        return useLocalMetadataOnly;
    }

    protected boolean useTargetedMetadata(String metadataContext) {
        return (ModelRepositoryConfiguration.TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext));
    }

}

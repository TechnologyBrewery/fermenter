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

    /** Indicates that all metadata should be used. */
    public static final String ALL_METADATA_CONTEXT = "all";

    /** Indicates that only local metadata should be used (e.g., current application.name). */
    public static final String LOCAL_METADATA_CONTEXT = "local";

    /** Indicates that a target set of metadata for a specific list of artifactIds should be used. */
    public static final String TARGETED_METADATA_CONTEXT = "targeted";
    
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
        if (StringUtils.isBlank(metadataContext) || ALL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)
                || TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext) ) {
            useLocalMetadataOnly = false;

        } else if (LOCAL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)) {
            useLocalMetadataOnly = true;

        } else {
            useLocalMetadataOnly = false;
            LOG.warn("An invalid metadata context of '" + metadataContext
                    + "' has been specified.  Using 'all' instead!");
        }

        return useLocalMetadataOnly;
    }

    protected boolean useTargetedMetadata(String metadataContext) {
        return (TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext));
    }

}

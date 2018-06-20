package org.bitbucket.fermenter.mda.metamodel;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

/**
 * Provides common methods needed for exposing model instances
 */
public abstract class AbstractModelInstanceRepository implements ModelInstanceRepository {

    protected ModelRepositoryConfiguration config;

    /**
     * Instantiates this instance with any applicable properties.
     * 
     * @param properties
     *            any applicable properties
     */
    public AbstractModelInstanceRepository(ModelRepositoryConfiguration config) {
        this.config = config;

    }

    /**
     * Returns the base package under which this repository was instantiated.
     * 
     * @return base package name
     */
    public String getBasePackage() {
        return config.getBasePackage();

    }

    static boolean useLocalMetadataOnly(String metadataContext) {
        boolean useLocalMetadataOnly = true;
        if (StringUtils.isBlank(metadataContext)
                || ModelRepositoryConfiguration.ALL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)
                || ModelRepositoryConfiguration.TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)) {
            useLocalMetadataOnly = false;

        } else if (ModelRepositoryConfiguration.LOCAL_METADATA_CONTEXT.equalsIgnoreCase(metadataContext)) {
            useLocalMetadataOnly = true;

        } else {
            useLocalMetadataOnly = false;
            MessageTracker messageTracker = MessageTracker.getInstance();
            messageTracker.addWarningMessage("An invalid metadata context of '" + metadataContext
                    + "' has been specified.  Using 'all' instead!");
        }

        return useLocalMetadataOnly;
    }

    // TODO: move and make protected & an instance method to AbstractMetamodelMoanager once legacy metadata is gone
    static boolean useTargetedMetadata(String metadataContext) {
        return (ModelRepositoryConfiguration.TARGETED_METADATA_CONTEXT.equalsIgnoreCase(metadataContext));
    }

}
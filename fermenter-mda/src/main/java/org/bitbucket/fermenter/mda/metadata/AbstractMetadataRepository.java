package org.bitbucket.fermenter.mda.metadata;

import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;

/**
 * Provides common methods needed for a metadata repository.
 * 
 */
public abstract class AbstractMetadataRepository implements ModelInstanceRepository {

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

}

package org.bitbucket.fermenter.mda.metamodel;

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

}
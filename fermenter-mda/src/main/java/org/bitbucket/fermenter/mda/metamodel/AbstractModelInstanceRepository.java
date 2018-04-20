package org.bitbucket.fermenter.mda.metamodel;

/**
 * Provides common methods needed for exposing model instances
 */
public abstract class AbstractModelInstanceRepository implements ModelInstanceRepository {

    protected String basePackage;

    /**
     * Instantiates this instance with any applicable properties.
     * 
     * @param properties
     *            any applicable properties
     */
    public AbstractModelInstanceRepository(String basePackage) {
        this.basePackage = basePackage;

    }

    /**
     * Returns the base package under which this repository was instantiated.
     * 
     * @return base package name
     */
    public String getBasePackage() {
        return basePackage;
    }

}
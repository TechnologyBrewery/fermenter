package org.bitbucket.fermenter.mda.metamodel;

import java.util.Properties;

public interface MetadataRepository {

    /**
     * Loads all metadata and will be invoked immediately after instantiating this instance in the templated workflow.
     * 
     * @param properties
     *            any applicable properties
     */
    public abstract void load(Properties properties);

    /**
     * Validates all metadata and will be invoked immediately after loading in the templated workflow.
     * 
     * @param properties
     *            any applicable properties
     */
    public abstract void validate(Properties properties);
    
}

package org.bitbucket.fermenter.mda.metamodel;

import java.util.Properties;

/**
 * Common interface between legacy and new metadata repositories to allow them to more easily interact.
 */
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

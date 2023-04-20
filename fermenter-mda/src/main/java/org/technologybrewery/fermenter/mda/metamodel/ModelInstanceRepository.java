package org.technologybrewery.fermenter.mda.metamodel;

/**
 * Common interface between legacy and new model instance repositories to allow them to more easily interact.
 */
public interface ModelInstanceRepository {

    /**
     * Loads all metadata and will be invoked immediately after instantiating this instance in the templated workflow.
     */
    public abstract void load();

    /**
     * Validates all metadata and will be invoked immediately after loading in the templated workflow.
     */
    public abstract void validate();
    
}

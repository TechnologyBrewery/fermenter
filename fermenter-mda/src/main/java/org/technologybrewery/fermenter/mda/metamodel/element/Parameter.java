package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Defines the contract for operation parameters.
 */
public interface Parameter extends NamespacedMetamodel {

    /**
     * Returns service-level documentation.
     * 
     * @return service documentation
     */
    String getDocumentation();
    
    /**
     * Returns the type of parameter.
     * 
     * @return parameter type
     */
    String getType();
    
    /**
     * Returns whether or not this returns many instances of the type or a single instance.
     * 
     * @return is many?
     */
    Boolean isMany();

}

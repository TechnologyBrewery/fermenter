package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides the base interface of any metamodel object - it has to at least have a name and be validatable.
 */
public interface Metamodel extends Validatable {
    
    String getFileName();

    /**
     * Returns the name of the metadata element.
     * 
     * @return name
     */
    String getName();
    
}
package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides the base interface of any metamodel object - it has to at least have a name and be validatable.
 */
public interface Metamodel {

    /**
     * Ensure that the this metamodel element is in a valid state. For instance, if this element refers to another
     * element, let's ensure that element exists.
     */
    void validate();
    
    String getFileName();

    /**
     * Returns the name of the metadata element.
     * 
     * @return name
     */
    String getName();
    
}
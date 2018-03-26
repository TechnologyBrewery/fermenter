package org.bitbucket.fermenter.mda.metamodel.element;

public interface Metamodel {

    /**
     * Ensure that the this metamodel element is in a valid state. For instance, if this element refers to another
     * element, let's ensure that element exists.
     */
    void validate();

    /**
     * Returns the name of the metadata element.
     * 
     * @return name
     */
    String getName();

}
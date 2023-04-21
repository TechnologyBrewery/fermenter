package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Defines the contractor for validating a metamodel element.
 */
public interface Validatable {
	
    /**
     * Ensure that the this metamodel element is in a valid state. For instance, if this element refers to another
     * element, let's ensure that element exists.
     */
    void validate();
	
	
}

package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Defines the contract for a metamodel element.
 */
public interface MetamodelElement {

    /**
     * Ensure that the this metamodel element is in a valid state.  For instance, if this element refers to another
     * element, let's ensure that element exists.
     */
    public abstract void validate();
    
    /**
     * Returns the name of the metadata element.
     * @return name
     */
    public abstract String getName();
    
    /**
     * Returns the package of the metadata element.
     * @return package
     */
    public abstract String getPackage();
    
}

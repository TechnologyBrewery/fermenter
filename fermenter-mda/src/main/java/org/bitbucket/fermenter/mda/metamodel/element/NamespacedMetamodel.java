package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Defines the contract for a "first class" or "parent" metamodel instance that must have a package (namespace) in
 * addition to a name.
 */
public interface NamespacedMetamodel extends Metamodel {

    /**
     * Returns the package of the metadata element.
     * 
     * @return package
     */
    String getPackage();

}
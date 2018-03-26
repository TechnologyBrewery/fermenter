package org.bitbucket.fermenter.mda.metamodel.element;

public interface NamespacedMetamodel extends Metamodel {

    /**
     * Returns the package of the metadata element.
     * 
     * @return package
     */
    String getPackage();

}
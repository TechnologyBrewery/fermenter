package org.bitbucket.fermenter.stout.mda;

public interface JavaNamedElement extends JavaNamespacedElement {

    String getPackage();
    
    default String getImport() {
        return getPackage() + "." + getCapitalizedName();
    }
    
}

package org.bitbucket.fermenter.stout.mda;

/**
 * Defines common and default methods for a packaged Java element.
 */
public interface JavaPackagedElement extends JavaNamedElement {

    /**
     * Returns the package (namespace) of the element.
     * @return package
     */
    String getPackage();
    
    /**
     * Creates a default import for the packaged element.
     * @return import
     */
    default String getImport() {
        return getPackage() + "." + getCapitalizedName();
    }
    
}

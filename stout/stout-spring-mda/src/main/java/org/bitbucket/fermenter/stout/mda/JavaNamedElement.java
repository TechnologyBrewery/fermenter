package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang.StringUtils;

/**
 * Defines the common interface for a Java named element with default utility methods to handle common variations of the
 * name.
 */
public interface JavaNamedElement {

    /**
     * Returns the name of this element.
     * 
     * @return name
     */
    String getName();

    /**
     * Returns the name with a capitalized first letter.
     * 
     * @return capitalized name
     */
    default String getCapitalizedName() {
        return StringUtils.uncapitalize(getName());
    }

    /**
     * Returns the uncapitalized name of thie element.
     * 
     * @return uncapitalized name
     */
    default String getUncapitalizedName() {
        return StringUtils.uncapitalize(getName());
    }

}

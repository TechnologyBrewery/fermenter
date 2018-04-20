package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for an enumeration that contains multiple constants (i.e., enums).
 */
public interface Enumeration extends NamespacedMetamodel {

    /**
     * Returns the longest size of the enum constants provided in this model to all this type to have a restricted size
     * in input or persistence contexts.
     * 
     * @return length of longest enum
     */
    Integer getMaxLength();

    /**
     * Returns the enum instances within this enumeration.
     * 
     * @return enums
     */
    List<Enum> getEnums();

}
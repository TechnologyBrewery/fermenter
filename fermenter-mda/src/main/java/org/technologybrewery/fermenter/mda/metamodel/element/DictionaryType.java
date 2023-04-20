package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for a dictionary type.
 */
public interface DictionaryType extends Validation {

    /**
     * Returns the base type for the dictionary type.
     * 
     * @return type
     */
    String getType();

    /**
     * Returns the formats applicable for the dictionary type.
     * 
     * @return formats
     */
    List<String> getFormats();
}

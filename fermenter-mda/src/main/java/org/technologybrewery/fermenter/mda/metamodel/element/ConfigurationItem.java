package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Defines the contract for a configuration item.
 */
public interface ConfigurationItem extends Validatable {

    /**
     * Returns the configuration key.
     * 
     * @return configuration key
     */
    String getKey();

    /**
     * Returns the configuration value.
     * 
     * @return configuration value
     */
    String getValue();

}

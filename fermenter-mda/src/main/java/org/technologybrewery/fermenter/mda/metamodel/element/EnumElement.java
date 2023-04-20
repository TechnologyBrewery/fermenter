package org.technologybrewery.fermenter.mda.metamodel.element;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents a constant that is defined within a specific {@link EnumerationElement}.
 */
public class EnumElement extends MetamodelElement implements Enum {

    @JsonInclude(Include.NON_NULL)
    private Integer value;
    
    /**
     * Gets the value for this enum.
     * 
     * @return value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Sets value for this enum.
     * @param value enum value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // nothing specific to validate that isn't handled at JSON load time already
    }

}

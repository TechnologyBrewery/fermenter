package org.technologybrewery.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A JSON reference to a {@link Target} by name.
 */
public class TargetReference {
    
    @JsonProperty(required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}

package org.bitbucket.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A JSON reference to a {@link Profile} by name.
 */
public class ProfileReference {

    @JsonProperty(required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
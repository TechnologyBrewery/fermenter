package org.bitbucket.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Holds generation family information to support MDA execution. A family
 * represents a set of profiles that can be used to control generation directly
 * or be included in other families.
 */
public class Family implements ValidatedElement {

    @JsonProperty(required = true)
    protected String name;

    protected Collection<ProfileReference> profileReferences = new ArrayList<>();

    @Override
    public String getSchemaFileName() {
        return "fermenter-2-profile-schema.json";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ProfileReference> getProfileReferences() {
        return profileReferences;
    }

    public void setProfileReferences(Collection<ProfileReference> profileReferences) {
        this.profileReferences = profileReferences;
    }
}

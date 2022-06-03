package org.bitbucket.fermenter.mda.element;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds generation profile information to support MDA execution. A profile
 * represents a set of targets that can be used to control generation directly
 * or be included in other profiles.
 */
public class Profile implements ValidatedElement {

    @JsonProperty(required = true)
    protected String name;
    
    protected Collection<TargetReference> targetReferences = new ArrayList<>();
    
    protected Collection<ProfileReference> profileReferences = new ArrayList<>();

    protected Collection<FamilyReference> familyReferences = new ArrayList<>();

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }    
    
    public Collection<TargetReference> getTargetReferences() {
        return targetReferences;
    }

    public void setTargetReferences(Collection<TargetReference> targetReferences) {
        this.targetReferences = targetReferences;
    }

    public Collection<ProfileReference> getProfileReferences() {
        return profileReferences;
    }

    public void setProfileReferences(Collection<ProfileReference> includeReferences) {
        this.profileReferences = includeReferences;
    }

    public Collection<FamilyReference> getFamilyReferences() {
        return familyReferences;
    }

    public void setFamilyReferences(Collection<FamilyReference> familyReferences) {
        this.familyReferences = familyReferences;
    }

    /**
     * {@inheritDoc}
     */
    public String getSchemaFileName() {
        return "fermenter-2-profile-schema.json";
    }


}

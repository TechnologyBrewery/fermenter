package org.bitbucket.fermenter.mda.element;

public class FamilyInput {
    private String profileName;
    private String family;
    private String[] targetNames;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String[] getTargetNames() {
        return targetNames;
    }

    public void setTargetNames(String[] targetNames) {
        this.targetNames = targetNames;
    }
}

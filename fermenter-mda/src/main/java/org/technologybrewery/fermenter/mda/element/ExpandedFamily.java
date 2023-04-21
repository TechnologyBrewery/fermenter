package org.technologybrewery.fermenter.mda.element;

import com.google.common.collect.ImmutableList;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a fully expanded version of the metadata in {@link Family}. For instance, it dereferences the profiles and
 * included families to provide a full set to actual profiles referenced in a {@link Family}.
 */
public class ExpandedFamily {

    private static MessageTracker messageTracker = MessageTracker.getInstance();

    protected Family Family;

    protected Map<String, Profile> profiles = new HashMap<>();

    protected Map<String, ExpandedFamily> includes = new HashMap<>();

    protected boolean isDereferenced;

    public ExpandedFamily(Family Family) {
        this.Family = Family;
    }

    public String getName() {
        return Family.getName();
    }

    public void addProfile(Profile profile) {
        profiles.put(profile.getName(), profile);
    }

    public List<Profile> getProfiles() {
        return ImmutableList.copyOf(profiles.values());
    }

    public void addReferencedFamily(ExpandedFamily Family) {
        includes.put(Family.getName(), Family);
    }

    public List<ExpandedFamily> getReferencedFamilies() {
        return ImmutableList.copyOf(includes.values());
    }

    /**
     * Looks at the profile references and Family references in this Family and expands them so they can be retrieved
     * directly from this class. For instance, a profile reference to "fooProfile" becomes an actual reference to the
     * "fooProfile" profile instance that will get returned by a call to getProfiles().
     *
     * @param families
     *            full set of expanded families from the generator
     * @param profiles
     *            full set of profiles from the generator
     */
    public void dereference(Map<String, ExpandedFamily> families, Map<String, ExpandedProfile> profiles) {
        if (!isDereferenced) {
            isDereferenced = true;

            for (ProfileReference profileRef : Family.getProfileReferences()) {
                ExpandedProfile profile = profiles.get(profileRef.getName());
                if (profile != null) {
                    addProfile(profile.profile);
                } else {
                    messageTracker.addErrorMessage("Could not find profile '" + profileRef.getName()
                            + "' referenced by Family '" + getName() + "'!");
                }
            }
        }
    }
}

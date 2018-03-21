package org.bitbucket.fermenter.mda.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.google.common.collect.ImmutableList;

/**
 * Represents a fully expanded version of the metadata in {@link Profile}. For instance, it dereferences the targets and
 * included profiles to provide a full set to actual targets referenced in a {@link Profile}.
 */
public class ExpandedProfile {

    private static MessageTracker messageTracker = MessageTracker.getInstance();

    protected Profile profile;

    protected Map<String, Target> targets = new HashMap<>();

    protected Map<String, ExpandedProfile> includes = new HashMap<>();

    protected boolean isDereferenced;

    public ExpandedProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return profile.getName();
    }

    public void addTarget(Target target) {
        targets.put(target.getName(), target);
    }

    public List<Target> getTargets() {
        return ImmutableList.copyOf(targets.values());
    }

    public void addReferencedProfile(ExpandedProfile profile) {
        includes.put(profile.getName(), profile);
    }

    public List<ExpandedProfile> getReferencedProfiles() {
        return ImmutableList.copyOf(includes.values());
    }

    /**
     * Looks at the target references and profile references in this profile and expands them so they can be retrieved
     * directly from this class. For instance, a target reference to "fooTarget" becomes an actual reference to the
     * "fooTarget" target instance that will get returned by a call to getTargets().
     * 
     * @param profiles
     *            full set of expanded profiles from the generator
     * @param targets
     *            full set of targets from the generator
     */
    public void dereference(Map<String, ExpandedProfile> profiles, Map<String, Target> targets) {
        if (!isDereferenced) {
            isDereferenced = true;

            for (TargetReference targetRef : profile.getTargetReferences()) {
                Target target = targets.get(targetRef.getName());
                if (target != null) {
                    addTarget(target);
                } else {
                    messageTracker.addErrorMessage("Could not find target '" + targetRef.getName()
                            + "' referenced by profile '" + getName() + "'!");
                }
            }

            dereferenceProfilesReferences(profiles, targets);
        }
    }

    private void transferTargetsFromProfile(String profileName, Map<String, ExpandedProfile> profiles,
            Map<String, Target> targets) {
        ExpandedProfile extendsProfile = (profiles != null) ? profiles.get(profileName) : null;
        if (extendsProfile != null) {
            // ensure that this profile has been dereferenced:
            if (!extendsProfile.isDereferenced) {
                extendsProfile.dereference(profiles, targets);
            }

            Collection<Target> extendsTargets = extendsProfile.getTargets();
            for (Target t : extendsTargets) {
                addTarget(t);
            }

        } else {
            messageTracker
                    .addErrorMessage("Profile '" + profileName + "' does not exist in profile '" + getName() + "'!");

        }
    }

    private void dereferenceProfilesReferences(Map<String, ExpandedProfile> profiles, Map<String, Target> targets) {
        for (ProfileReference nameOnlyProfile : profile.profileReferences) {
            String profileName = nameOnlyProfile.getName();
            if (StringUtils.isNotBlank(profileName)) {
                if (profileName.equals(getName())) {
                    messageTracker.addWarningMessage("Profile '" + getName() + "' cannot include itself!");

                } else {
                    transferTargetsFromProfile(profileName, profiles, targets);

                }
            }
        }
    }

}

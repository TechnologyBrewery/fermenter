package org.bitbucket.fermenter.mda.element;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Profile {

	private String name;
	private String extendsProfileName;
	private Map<String, Target> targets;
	private Map<String, Profile> includes;
	private boolean isDereferenced;
	
	private static Log log = LogFactory.getLog(Profile.class); 
	
	public Profile() { 
		super();
		
		targets = new HashMap<String, Target>();
		includes = new HashMap<String, Profile>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	} 
	
	/**
	 * @return Returns the extendsProfileName.
	 */
	public String getExtends() {
		return extendsProfileName;
	}	

	/**
	 * @param extendsProfileName The extendsProfileName to set.
	 */
	public void setExtends(String extendsProfileName) {
		this.extendsProfileName = extendsProfileName;
	}

	public void addTarget(Target target) {
		targets.put(target.getName(), target);
	}
	
	public Collection<Target> getTargets() {		
		return Collections.unmodifiableCollection(targets.values());
	}
	
	public void addInclude(Profile profile) {
		includes.put(profile.getName(), profile);
	}
	
	public Collection<Profile> getIncludes() {		
		return Collections.unmodifiableCollection(includes.values());
	}	
	
	public void dereferenceProfiles(Map<String, Profile> profiles) {
		if (!isDereferenced) {
			isDereferenced =  true;
			
			dereferenceExtends(profiles);
			dereferenceIncludes(profiles);		
		}
	}
	
	private void dereferenceExtends(Map<String, Profile> profiles) {
		if (StringUtils.isNotBlank(extendsProfileName)) {
			if (extendsProfileName.equals(name)) {
				log.warn("Profile '" + extendsProfileName + "' cannot extend itself!");
				
			} else {
				addFromProfile(extendsProfileName, profiles);
				
			}
		}
		
	}

	/**
	 * Transfer the targets from the profile with the passed name onto this profile.
	 * @param profiles
	 */
	private void addFromProfile(String profileName, Map<String, Profile> profiles) {
		Profile extendsProfile = (profiles != null) ? profiles.get(profileName) : null;
		if (extendsProfile != null) {
			//ensure that this profile has been dereferenced:
			if (!extendsProfile.isDereferenced) {
				extendsProfile.dereferenceProfiles(profiles);
			}
			
			Collection<Target> extendsTargets = extendsProfile.getTargets();
			for (Target t : extendsTargets) {
				addTarget(t);
			}
			
		} else {
			log.warn("Profile '" + profileName + "' does not exist and is being ignored in profile '" + name + "'!");
			
		}
	}
	
	private void dereferenceIncludes(Map<String, Profile> profiles) {
		String profileName;
		
		for (Profile nameOnlyProfile : includes.values()) {
			profileName = nameOnlyProfile.getName();
			if (StringUtils.isNotBlank(profileName)) {
				if (profileName.equals(name)) {
					log.warn("Profile '" + name + "' cannot include itself!");
					
				} else {
					addFromProfile(profileName, profiles);
					
				}
				
			}
			
		}		
		
	}
	
}

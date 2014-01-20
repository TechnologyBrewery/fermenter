package org.tigris.atlas.mda.element;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Profile {

	private String name;
	private String extendsProfileName;
	private Map targets;
	private Map includes;
	private boolean isDereferenced;
	
	private static Log log = LogFactory.getLog(Profile.class); 
	
	public Profile() { 
		super();
		
		targets = new HashMap();
		includes = new HashMap();
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
	
	public Collection getTargets() {		
		return Collections.unmodifiableCollection(targets.values());
	}
	
	public void addInclude(Profile profile) {
		includes.put(profile.getName(), profile);
	}
	
	public Collection getIncludes() {		
		return Collections.unmodifiableCollection(includes.values());
	}	
	
	public void dereferenceProfiles(Map profiles) {
		if (!isDereferenced) {
			isDereferenced =  true;
			
			dereferenceExtends(profiles);
			dereferenceIncludes(profiles);		
		}
	}
	
	private void dereferenceExtends(Map profiles) {
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
	private void addFromProfile(String profileName, Map profiles) {
		Profile extendsProfile = (profiles != null) ? (Profile)profiles.get(profileName) : null;
		if (extendsProfile != null) {
			//ensure that this profile has been dereferenced:
			if (!extendsProfile.isDereferenced) {
				extendsProfile.dereferenceProfiles(profiles);
			}
			
			Target t;
			Collection extendsTargets = extendsProfile.getTargets();
			Iterator i = extendsTargets.iterator();
			while (i.hasNext()) {
				t = (Target)i.next();
				addTarget(t);
			}
			
		} else {
			log.warn("Profile '" + profileName + "' does not exist and is being ignored in profile '" + name + "'!");
			
		}
	}
	
	private void dereferenceIncludes(Map profiles) {
		Iterator i = includes.values().iterator();
		String profileName;
		Profile nameOnlyProfile;
		while (i.hasNext()) {
			nameOnlyProfile = (Profile)i.next();
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

package org.bitbucket.fermenter.stout.authn;

import org.apache.shiro.SecurityUtils;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;
import org.pac4j.core.profile.CommonProfile;

import io.buji.pac4j.subject.Pac4jPrincipal;

/**
 * Stout wrapper around subject name. Allows any implementation to be used without exposing the implementation directly
 * to the rest of Stout.
 * 
 * If you want to change this implementation - please do! You can override the IdentityManager bean and register
 * whatever you want.
 */
public class IdentityManager {

    /**
     * Returns the subject name (a.k.a. username) of the currently logged in identity.
     * 
     * @return subject name
     */
    public String getSubjectName() {
        String subjectName = null;

        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            if (principal instanceof Pac4jPrincipal) {
                Pac4jPrincipal pac4JPrincipal = (Pac4jPrincipal) principal;
                CommonProfile profile = pac4JPrincipal.getProfile();
                subjectName = profile.getId();

            } else if (principal instanceof String) {
                subjectName = (String) principal;

            } else {
                throw new UnrecoverableException("Unknown type of principal: " + principal.getClass());

            }
        }

        return subjectName;
    }

}

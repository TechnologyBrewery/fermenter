package org.technologybrewery.fermenter.stout.authn;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.buji.pac4j.subject.Pac4jPrincipal;

/**
 * Stout wrapper around subject name. Allows any implementation to be used without exposing the implementation directly
 * to the rest of Stout.
 * 
 * If you want to change this implementation - please do! You can override the IdentityManager bean and register
 * whatever you want.
 */
public class IdentityManager {

    private static final Logger logger = LoggerFactory.getLogger(IdentityManager.class);

    /**
     * Returns the subject name (a.k.a. username) of the currently logged in identity. For proper tokens, a username
     * will be populated. However, for simple token (e.g., a header containing a username), only id will be populated.
     * Try username and if we don't get it, default to id.
     * 
     * @return subject name
     */
    public String getSubjectName() {
        String subjectName = null;

        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal != null) {
            if (principal instanceof Pac4jPrincipal) {
                Pac4jPrincipal pac4JPrincipal = (Pac4jPrincipal) principal;
                CommonProfile profile = (CommonProfile) pac4JPrincipal.getProfile();
                subjectName = profile.getUsername();

                if (subjectName == null) {
                    subjectName = profile.getId();
                }

            } else if (principal instanceof String) {
                subjectName = (String) principal;

            } else {
                throw new UnrecoverableException("Unknown type of principal: " + principal.getClass());

            }

            if (!subject.isAuthenticated()) {
                logger.warn("User {} has been logged out!", subjectName);
                subjectName = null;
            }
        }

        return subjectName;
    }

    /**
     * Logs the current subject out.
     */
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();

        } else {
            logger.warn("Cannot logout when subject {} is already logged out!", getSubjectName());

        }
    }

}

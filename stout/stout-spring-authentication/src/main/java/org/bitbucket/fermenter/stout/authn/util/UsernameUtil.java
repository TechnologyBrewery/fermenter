package org.bitbucket.fermenter.stout.authn.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * {@link UsernameUtil} is a utility class that provides methods for easily
 * getting the username from the security context.
 * 
 * @author Counterpointe Solutions, Inc.
 *
 */
public class UsernameUtil {

    /**
     * Method for getting the username from a {@link SecurityContext}, whether
     * it's within a {@link User} or as a string.
     * 
     * @return username
     */
    public static String getLoggedInUsername() {

        String username = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            username = user.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }
        return username;
    }

}

package org.bitbucket.fermenter.stout.authn;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Loads the user by their username and applies appropriate grantedAuthorities.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Loads a user by their username.
     *
     * @param username - the username
     * @return A configured user details model
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("Could not find username " + username);
            
        }
        
        return new User(username, "password", Collections.emptyList());
    }
    
}

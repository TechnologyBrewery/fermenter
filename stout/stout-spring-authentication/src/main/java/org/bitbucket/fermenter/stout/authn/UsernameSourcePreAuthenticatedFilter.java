package org.bitbucket.fermenter.stout.authn;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

/**
 * Filter for retrieving the preauthenticated user.
 */
@Component
public class UsernameSourcePreAuthenticatedFilter extends AbstractPreAuthenticatedProcessingFilter {

    private List<UsernameSource> usernameSources;

    /**
     * Gets the preAuthenticatedCredentials.
     *
     * @param request - HttpServletRequest
     * @returns the preAuthenticatedCredentials
     */
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        // No credentials are available
        return "NONE";
    }

    /**
     * Gets the preAuthenticatedPrincipal (username).
     *
     * @param request - the HttpServletRequest
     * @return - The preAuthenticatedPrincipal
     */
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Optional<UsernameSource> responsibleSourceOptional =
                usernameSources.stream()
                        .filter(usernameSource -> usernameSource.isResponsible(request))
                        .findFirst();

        UsernameSource usernameSource = responsibleSourceOptional.orElse(null);
        return usernameSource != null ? usernameSource.extractUsername(request) : null;

    }

    /**
     * Sets the authenticationManager for the AbstractPreAuthenticatedProcessingFilter.
     *
     * @param authenticationManager - the authenticationManager
     */
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * Sets the usernameSources responsible for extracting the username.
     *
     * @param usernameSources - List of usernameSources
     */
    @Autowired
    public void setUsernameSources(List<UsernameSource> usernameSources) {
        this.usernameSources = usernameSources;
    }

}

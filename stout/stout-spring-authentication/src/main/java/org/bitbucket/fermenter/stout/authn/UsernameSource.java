package org.bitbucket.fermenter.stout.authn;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for extracting usernames.
 */
public interface UsernameSource {

    /**
     * Extracts a username.
     *
     * @param request
     *            the HttpServlet Request
     * @return the username
     */
    String extractUsername(HttpServletRequest request);

    /**
     * Returns true if the UsernameSource is responsible for retrieving the username for a given request.
     *
     * @param request
     *            the HttpServlet Request
     * @return true if the UsernameSource is responsible for retrieving the username, false otherwise
     */
    boolean isResponsible(HttpServletRequest request);
}

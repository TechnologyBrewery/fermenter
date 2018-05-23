package org.bitbucket.fermenter.stout.authn;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for extracting the username from the header
 */
@Component
public class HeaderUsernameSource implements UsernameSource {

    /**
     * {@inheritDoc}
     */
    @Override
    public String extractUsername(HttpServletRequest request) {
        return request.getHeader(getSecurityAuthHeader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResponsible(HttpServletRequest request) {
        return request.getHeader(getSecurityAuthHeader()) != null;
    }

    /**
     * Gets the header name from Krausening config
     * @return The header name from Krausening config
     */
    private String getSecurityAuthHeader() {
        final SecurityPropertiesConfig securityConfig = KrauseningConfigFactory.create(SecurityPropertiesConfig.class);
        return securityConfig.securityAuthHeader();
    }
}

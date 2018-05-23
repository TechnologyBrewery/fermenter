package org.bitbucket.fermenter.stout.authn;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

@KrauseningSources("security.properties")
public interface SecurityPropertiesConfig extends KrauseningConfig {

    /**
     * Turns security on and off.
     * 
     * @return True if security is enabled, false otherwise
     */
    @Key("security.enabled")
    @DefaultValue("true")
    Boolean securityEnabled();

    /**
     * Turns security on and off.
     * 
     * @return True if security is enabled, false otherwise
     */
    @Key("security.auth.header")
    @DefaultValue("username")
    String securityAuthHeader();

    /**
     * URL matcher for secured resources
     * @return URL matcher
     */
    @Key("security.url.matcher")
    @DefaultValue("/**")
    String securityUrlMatcher();
    
}

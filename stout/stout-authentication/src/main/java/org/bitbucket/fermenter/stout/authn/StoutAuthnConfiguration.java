package org.bitbucket.fermenter.stout.authn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration to make the {@link IdentityManager} available for dependency injection.
 */
@Configuration
public class StoutAuthnConfiguration {
    
    @Bean
    public IdentityManager getIdentityManager() {
        return new IdentityManager();
    }

}

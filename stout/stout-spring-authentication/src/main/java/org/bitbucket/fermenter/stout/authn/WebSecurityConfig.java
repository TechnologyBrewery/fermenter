package org.bitbucket.fermenter.stout.authn;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Spring Security Configuration.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    private static final SecurityPropertiesConfig securityConfig = KrauseningConfigFactory.create(SecurityPropertiesConfig.class);
    
    private UsernameSourcePreAuthenticatedFilter filter;

    /**
     * Configures the basic HTTP Spring Security. This secures all endpoints and adds a filter that is responsible for
     * retrieving a preauthenticted user. If such a user can't be found it will fail and prevent access.
     *
     * @param httpSecurity
     *            HttpSecurity object
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        try {
            if (isSecurityEnabled()) {
                httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().headers()
                        .frameOptions().sameOrigin() // In order for some external products to embed external pages in
                                                     // IFrames we need to enable same origin requests.
                        .and() // The default behavior is X-Frame-Options: Deny which prevents the request from being
                               // processed.
                        .addFilter(filter).authorizeRequests().antMatchers(securityConfig.securityUrlMatcher())
                        .authenticated().and().csrf().disable();
                LOGGER.info("Stout Authentication Enabled");
            } else {
                httpSecurity.authorizeRequests().antMatchers("/**").permitAll().and().headers().frameOptions()
                        .sameOrigin().and().csrf().disable();
            }
        } catch (Exception e) {
            LOGGER.error("Unable to configure authentication", e);
            throw e;
        }
    }

    /**
     * Supplies the authenticationManager with the authenticationProvider that is responsible for the authentication.
     *
     * @param authenticationManagerBuilder
     *            Builder for the authenticationManager
     */
    @Autowired
    protected void registerAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(preauthAuthProvider());
    }

    /**
     * Exposes the authenticationManager as a bean and a candidate for autowiring in places like
     * UsernameSourcePreAuthenticatedFilter.
     *
     * @return the authenticationManager
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Configures the preAuthProvider with a userDetailsService that is responsible for returning the authenticated
     * user's details.
     * 
     * @return The PreAuthenticatedProvider object with the userDetailsService added
     */
    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
        PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
        return preauthAuthProvider;
    }

    /**
     * Configures the userDetailsService wrapper with the userDetailsService.
     *
     * @return the UserDetailsByNameServiceWrapper
     */
    @Bean
    public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
        wrapper.setUserDetailsService(new UserDetailsServiceImpl());
        return wrapper;
    }

    /**
     * Sets the UsernameSourcePreAuthenticatedFilter
     *
     * @param filter
     *            the UsernameSourcePreAuthenticatedFilter
     */
    @Autowired
    public void setFilter(UsernameSourcePreAuthenticatedFilter filter) {
        this.filter = filter;
    }

    /**
     * Helper method that determines whether or not to apply security based on Krausening property.
     *
     * @return true if security should be enabled, false otherwise
     */
    private Boolean isSecurityEnabled() {       
        if (!securityConfig.securityEnabled()) {
            LOGGER.warn("****NO AUTHENTICATION ENABLED FOR THIS MODULE!****");
        }
        return securityConfig.securityEnabled();
    }

    /**
     * CSRF token repository.
     *
     * @return a configured CSRF token repository
     */
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

}

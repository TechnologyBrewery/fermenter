package org.bitbucket.fermenter.stout.authn;

import org.pac4j.core.matching.matcher.csrf.CsrfTokenGeneratorMatcher;
import org.pac4j.core.matching.matcher.csrf.DefaultCsrfTokenGenerator;

/**
 * Modified CSRF Token matcher that allows for a blank constructor.
 */
public class ModifiedCsrfTokenGeneratorMatcher extends CsrfTokenGeneratorMatcher {
    
    public ModifiedCsrfTokenGeneratorMatcher() {
        super(new DefaultCsrfTokenGenerator());
    }
    
}

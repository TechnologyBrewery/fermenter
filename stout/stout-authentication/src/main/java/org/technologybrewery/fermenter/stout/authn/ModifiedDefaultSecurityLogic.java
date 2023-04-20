package org.technologybrewery.fermenter.stout.authn;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.matching.checker.MatchingChecker;

/**
 * <p>Default security logic:</p>
 *
 * <p>If the HTTP request matches the <code>matchers</code> configuration (or no <code>matchers</code> are defined),
 * the security is applied. Otherwise, the user is automatically granted access.</p>
 *
 * <p>First, if the user is not authenticated (no profile) and if some clients have been defined in the <code>clients</code> parameter,
 * a login is tried for the direct clients.</p>
 *
 * <p>Then, if the user has profile, authorizations are checked according to the <code>authorizers</code> configuration.
 * If the authorizations are valid, the user is granted access. Otherwise, a 403 error page is displayed.</p>
 *
 * <p>Finally, if the user is still not authenticated (no profile), he is redirected to the appropriate identity provider
 * if the first defined client is an indirect one in the <code>clients</code> configuration. Otherwise, a 401 error page is displayed.</p>
 */
public class ModifiedDefaultSecurityLogic<R, C extends WebContext> extends DefaultSecurityLogic {
    
    public static final ModifiedDefaultSecurityLogic INSTANCE = new ModifiedDefaultSecurityLogic();

    private MatchingChecker matchingChecker = new ModifiedDefaultMatchingChecker();
    
    public ModifiedDefaultSecurityLogic() {
        setMatchingChecker(matchingChecker);
    }
}

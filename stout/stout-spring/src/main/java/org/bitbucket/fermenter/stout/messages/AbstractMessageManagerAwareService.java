package org.bitbucket.fermenter.stout.messages;

import javax.ws.rs.NotAuthorizedException;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.bitbucket.fermenter.stout.authz.Action;
import org.bitbucket.fermenter.stout.authz.PolicyDecision;
import org.bitbucket.fermenter.stout.authz.PolicyDecisionPoint;
import org.bitbucket.fermenter.stout.config.StoutBehaviorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// TODO: Rename for auth
public abstract class AbstractMessageManagerAwareService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageManagerAwareService.class);

    protected static StoutBehaviorConfig stoutBehaviorConfig = KrauseningConfigFactory
            .create(StoutBehaviorConfig.class);

    protected static boolean shouldCreateMessageOnNonexistentFindByPrimaryKey = stoutBehaviorConfig
            .shouldCreateErrorOnNonexistentServiceFindByPrimaryKey();

    protected PolicyDecisionPoint pdp = PolicyDecisionPoint.getInstance();

    protected boolean assertAuthorization(String resource, Action action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = null;
        if (authentication != null) {
            currentPrincipalName = authentication.getName();
        } else {
            logger.error("No username available!");
            throw new NotAuthorizedException("Authorization denied - cannot access user information!");
        }
        PolicyDecision policyDecision = pdp.isAuthorized(currentPrincipalName, resource, action.toString());

        boolean isAuthorized = PolicyDecision.PERMIT.equals(policyDecision);

        if (!isAuthorized) {
            throw new NotAuthorizedException("Authorization denied!");
        }

        return isAuthorized;
    }
}

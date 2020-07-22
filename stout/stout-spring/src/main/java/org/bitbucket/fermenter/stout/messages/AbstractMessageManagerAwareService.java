package org.bitbucket.fermenter.stout.messages;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.bitbucket.fermenter.stout.authn.IdentityManager;
import org.bitbucket.fermenter.stout.authz.Action;
import org.bitbucket.fermenter.stout.authz.PolicyDecision;
import org.bitbucket.fermenter.stout.authz.PolicyDecisionPoint;
import org.bitbucket.fermenter.stout.config.StoutBehaviorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Rename for auth
public abstract class AbstractMessageManagerAwareService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageManagerAwareService.class);
    
    @Inject
    private IdentityManager identityManager; 

    protected static StoutBehaviorConfig stoutBehaviorConfig = KrauseningConfigFactory
            .create(StoutBehaviorConfig.class);

    protected static boolean shouldCreateMessageOnNonexistentFindByPrimaryKey = stoutBehaviorConfig
            .shouldCreateErrorOnNonexistentServiceFindByPrimaryKey();

    protected PolicyDecisionPoint pdp = PolicyDecisionPoint.getInstance();

    protected boolean assertAuthorization(String resource, Action action) {        
        String currentPrincipalName = identityManager.getSubjectName();
        if (currentPrincipalName == null) {
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

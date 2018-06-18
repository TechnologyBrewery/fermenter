package org.bitbucket.fermenter.stout.messages;

import javax.ws.rs.NotAuthorizedException;

import org.bitbucket.fermenter.stout.authz.Action;
import org.bitbucket.fermenter.stout.authz.PolicyDecision;
import org.bitbucket.fermenter.stout.authz.PolicyDecisionPoint;
import org.bitbucket.fermenter.stout.service.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractMsgMgrAwareService {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractMsgMgrAwareService.class);
    
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
    
	protected final void initMsgMgr(Messages messages) {
		if (messages != null) {
			MessageManager.initialize(messages);
		}
	}

	protected final ServiceResponse addAllMsgMgrToResponse(ServiceResponse response) {
		for (Message msg : MessageManager.getMessages().getAllMessages()) {
			response.getMessages().addMessage(msg);
		}
		MessageManager.cleanup();
		return response;
	}
	
}

package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SecurityTestService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.fermenter.stout.authn.IdentityManager;
import org.springframework.stereotype.Service;


/**
 * Service implementation class for the SecurityTest service.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SecurityTestService
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 * Generated from service.impl.java.vm
 */
@Service
public class SecurityTestServiceImpl extends SecurityTestBaseServiceImpl implements SecurityTestService {
    
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private SimpleDomainMaintenanceService simpleDomainMaintenanceService;

	@Override
	protected String echoUsernameImpl() {	
		return identityManager.getSubjectName();
	}


	@Override
	protected String logoutThenEchoUsernameImpl() {
	    identityManager.logout();	
		return identityManager.getSubjectName();
	}

    @Override
    protected void logoutThenInvokeAuthorizationProtectedOperationImpl() {
        identityManager.logout();
        
        simpleDomainMaintenanceService.saveOrUpdate(new SimpleDomainBO());
        
    }

}

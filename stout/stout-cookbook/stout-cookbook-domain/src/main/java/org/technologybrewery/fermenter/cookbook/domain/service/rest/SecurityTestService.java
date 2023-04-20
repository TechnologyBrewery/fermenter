package org.technologybrewery.fermenter.cookbook.domain.service.rest;

import javax.ws.rs.Path;

/**
 * Interface for the SecurityTest service that may be modified by 
 * developers to encapsulate any service operations that are not supported
 * for definition in this domain's meta-model.
 *
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.SecurityTestBaseService
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Path(SecurityTestService.PATH)
public interface SecurityTestService extends SecurityTestBaseService {

    /**
     * Path for this service. Path is used multiple places, so having it in a constant ensures they are all consistent.
     */
    public static final String PATH = "SecurityTestService";
	
	// Developers should add any service operations here that cannot be defined via the PIM
	
}

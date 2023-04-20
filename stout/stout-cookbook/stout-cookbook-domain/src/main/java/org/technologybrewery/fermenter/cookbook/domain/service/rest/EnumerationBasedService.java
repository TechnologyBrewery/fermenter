package org.technologybrewery.fermenter.cookbook.domain.service.rest;

import javax.ws.rs.Path;

/**
 * Interface for the EnumerationBased service that may be modified by 
 * developers to encapsulate any service operations that are not supported
 * for definition in this domain's meta-model.
 *
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.EnumerationBasedBaseService
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Path(EnumerationBasedService.PATH)
public interface EnumerationBasedService extends EnumerationBasedBaseService {

    /**
     * Path for this service. Path is used multiple places, so having it in a constant ensures they are all consistent.
     */
    public static final String PATH = "EnumerationBasedService";
	
	// Developers should add any service operations here that cannot be defined via the PIM
	
}

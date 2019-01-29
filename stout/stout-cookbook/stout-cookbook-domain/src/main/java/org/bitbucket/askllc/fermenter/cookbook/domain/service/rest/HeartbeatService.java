package org.bitbucket.askllc.fermenter.cookbook.domain.service.rest;

import javax.ws.rs.Path;

/**
 * Interface for the Heartbeat service that may be modified by 
 * developers to encapsulate any service operations that are not supported
 * for definition in this domain's meta-model.
 *
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.HeartbeatBaseService
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Path(HeartbeatService.PATH)
public interface HeartbeatService extends HeartbeatBaseService {

    /**
     * Path for this service. Path is used multiple places, so having it in a constant ensures they are all consistent.
     */
    public static final String PATH = "HeartbeatService";
	
	// Developers should add any service operations here that cannot be defined via the PIM
	
}
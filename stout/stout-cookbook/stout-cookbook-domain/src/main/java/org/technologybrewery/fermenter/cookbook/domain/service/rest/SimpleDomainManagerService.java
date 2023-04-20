package org.technologybrewery.fermenter.cookbook.domain.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Interface for the SimpleDomainManager service that may be modified by developers to encapsulate any service
 * operations that are not supported for definition in this domain's meta-model.
 *
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.SimpleDomainManagerBaseService
 * 
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Path(SimpleDomainManagerService.PATH)
public interface SimpleDomainManagerService extends SimpleDomainManagerBaseService {

    /**
     * Path for this service. Path is used multiple places, so having it in a constant ensures they are all consistent.
     */
    public static final String PATH = "SimpleDomainManagerService";

    // Developers should add any service operations here that cannot be defined via the PIM

    @GET
    @Path("getLargeString")
    @Produces(MediaType.TEXT_PLAIN)
    Response getLargeString(@QueryParam("simpleDomainId") String simpleDomainId);
}

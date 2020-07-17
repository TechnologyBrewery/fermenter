package org.bitbucket.askllc.fermenter.cookbook.domain.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Interface for the ContentRepositoryExample service that may be modified by developers to encapsulate any service
 * operations that are not supported for definition in this domain's meta-model.
 *
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ContentRepositoryExampleBaseService
 * 
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Path(ContentRepositoryExampleService.PATH)
public interface ContentRepositoryExampleService extends ContentRepositoryExampleBaseService {

    /**
     * Path for this service. Path is used multiple places, so having it in a constant ensures they are all consistent.
     */
    public static final String PATH = "ContentRepositoryExampleService";

    /**
     * Path for the staffing export operation.
     */
    public static final String STREAM_CONTENT_EXAMPLE_PATH = "streamContentExample";

    // Developers should add any service operations here that cannot be defined via the PIM

    /**
     * Example of streaming content via a Stout service using Stout Content Repository.
     * 
     * @return streaming content
     */
    @GET
    @Path(STREAM_CONTENT_EXAMPLE_PATH)
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    Response getInspectionChecklist();

}
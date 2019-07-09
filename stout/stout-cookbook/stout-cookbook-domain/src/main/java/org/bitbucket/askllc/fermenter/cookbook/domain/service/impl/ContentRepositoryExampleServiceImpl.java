package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ContentRepositoryExampleService;
import org.bitbucket.fermenter.stout.authz.Action;
import org.bitbucket.fermenter.stout.content.ContentRepository;
import org.bitbucket.fermenter.stout.content.ContentRepositoryStream;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the ContentRepositoryExample service.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ContentRepositoryExampleService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class ContentRepositoryExampleServiceImpl extends ContentRepositoryExampleBaseServiceImpl
        implements ContentRepositoryExampleService {

    @Inject
    ContentRepository contentRepository;

    @Override
    public Response getInspectionChecklist() {
        assertAuthorization(PATH + "/" + STREAM_CONTENT_EXAMPLE_PATH, Action.EXECUTE);

        // for this test, create some data and store it in the ContentRepository:
        String folderName = "exampleFolder";
        String fileName = UUID.randomUUID().toString() + ".txt";

        // this stream class will take your folder and file names and stream from the content repository
        // directly to response:
        StreamingOutput stream = new ContentRepositoryStream(folderName, fileName, contentRepository);
        ResponseBuilder responseBuilder = Response.ok(stream);
        responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        responseBuilder.header("Content-Transfer-Encoding", "binary");
        responseBuilder.header(HttpHeaders.CACHE_CONTROL, "must-revalidate");
        responseBuilder.header("Pragma", "public");

        return responseBuilder.build();
    }
}
package org.bitbucket.fermenter.stout.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;

import org.bitbucket.fermenter.stout.service.ServiceResponse;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * Since Feign throws a FeignException for an error, this class is the translator of a feign exception to a more
 * specific exception.
 *
 */
public class FeignErrorTranslator implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() > 400 && response.status() <= 499) {
            throw new WebApplicationException(response.toString());
        }
        if (response.status() == 400) {
            ServiceResponse errorResponse = getResponseBody(response);
            if (errorResponse != null) {
                javax.ws.rs.core.Response statusResponse = javax.ws.rs.core.Response
                        .status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(errorResponse).build();
                throw new WebApplicationException(statusResponse);
            } else {
                throw new WebApplicationException(response.toString());
            }
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }

    private ServiceResponse getResponseBody(Response response) {
        ServiceResponse result = null;
        try {
            String errorJson = new BufferedReader(new InputStreamReader(response.body().asInputStream())).lines()
                    .parallel().collect(Collectors.joining("\n"));

            try {
                result = 
                        ObjectMapperManager.getObjectMapper().readValue(errorJson, VoidServiceResponse.class);
            } catch (IOException e) {
                // do nothing, let the result be null for the next block
            }
            if (result == null) {
                result = 
                        ObjectMapperManager.getObjectMapper().readValue(errorJson, ValueServiceResponse.class);

            }
        } catch (IOException e) {
            //do nothing, return null
        }
        return result;
    }

}

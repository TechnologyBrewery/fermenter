package org.bitbucket.fermenter.stout.util;

import javax.ws.rs.WebApplicationException;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * Since Feign throws a FeignException for an error, this class is the
 * translator of a feign exception to a more specific exception.
 *
 */
public class FeignErrorTranslator implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            throw new WebApplicationException(response.toString());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
    
    // TO DO: add all the well known HTTP error codes to translate into more
    // specific exceptions (other than WebApplicationException)

}

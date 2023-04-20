package org.technologybrewery.fermenter.cookbook.domain;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class AddHeadersRequestFilter implements ClientRequestFilter {

    private final String username;
    
    public AddHeadersRequestFilter(String username) {
        this.username = username;
    }
    
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("username", username);

    }

}

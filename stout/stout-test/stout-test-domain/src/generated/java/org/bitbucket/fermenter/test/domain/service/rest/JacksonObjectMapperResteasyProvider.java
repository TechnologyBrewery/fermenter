package org.bitbucket.fermenter.test.domain.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import org.bitbucket.fermenter.test.domain.transfer.json.ObjectMapperManager;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, "application/*+json", "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "application/*+json", "text/json" })
public class JacksonObjectMapperResteasyProvider extends ResteasyJacksonProvider {

	private ObjectMapper objectMapper = ObjectMapperManager.getObjectMapper();

	/**
	 * {@inheritDoc}
	 */
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
		return objectMapper;

	}

}
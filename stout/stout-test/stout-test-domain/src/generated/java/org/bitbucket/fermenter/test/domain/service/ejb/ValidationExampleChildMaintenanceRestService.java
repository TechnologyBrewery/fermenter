package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.service.Service;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChild;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Interface for the ValidationExampleChildMaintenance service. This interface defines the contract needed to access
 * create, retrieve, update, and delete a {@link ValidationExampleChild} instance FROM THE CLIENT PERSPECTIVE. This
 * interface may or may not differ from the one used on the server side, but is useful for creating automatic proxies of
 * jax-rs endpoints that won't be polluted by other methods we may not be exposing via jax-rs.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Path("/")
public interface ValidationExampleChildMaintenanceRestService extends Service {

	/**
	 * Save or update the passed instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExampleChild
	 * @param entity
	 *            The ValidationExampleChild instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@PUT
	@Path("/ValidationExampleChild/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationExampleChild> saveOrUpdate(@PathParam("id") String id, ValidationExampleChild entity);

	/**
	 * Create the passed instance.
	 * 
	 * @param entity
	 *            The ValidationExampleChild instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@POST
	@Path("/ValidationExampleChild")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationExampleChild> saveOrUpdate(ValidationExampleChild entity);

	/**
	 * Delete the ValidationExampleChild.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExampleChild
	 * @return ValidationExampleChildServiceResponse The deleted ValidationExampleChild container
	 */
	@DELETE
	@Path("/ValidationExampleChild/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationExampleChild> delete(@PathParam("id") String id);

	/**
	 * Find the ValidationExampleChild by primary key fields.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExampleChild
	 * @return ValidationExampleChildServiceResponse The retrieved ValidationExampleChild container
	 */
	@GET
	@Path("/ValidationExampleChild/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationExampleChild> findByPrimaryKey(@PathParam("id") String id);

}
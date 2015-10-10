package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.service.Service;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationExamplePK;

import javax.ejb.Local;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Interface for the ValidationExampleMaintenance service. This interface defines a the contract needed to create,
 * retrieve, update, and delete a {@link ValidationExample} instance remotely (e.g., http, RMI).
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local
@Path("/")
public interface ValidationExampleMaintenanceService extends Service {

	/**
	 * Save or update the passed instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExample
	 * @param entity
	 *            The ValidationExample instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@PUT
	@Path("/ValidationExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationExample> saveOrUpdate(@PathParam("id") String id, ValidationExample entity);

	/**
	 * Save or update the passed instance.
	 * 
	 * @param entity
	 *            The ValidationExample instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@POST
	@Path("/ValidationExample")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationExample> saveOrUpdate(ValidationExample entity);

	/**
	 * Delete the ValidationExample.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExample
	 * 
	 * @return ValidationExampleServiceResponse The deleted ValidationExample container
	 */
	@DELETE
	@Path("/ValidationExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationExample> delete(@PathParam("id") String id);

	/**
	 * Delete the ValidationExample.
	 * 
	 * @param entity
	 *            ValidationExample to delete
	 * @return ValidationExampleServiceResponse The deleted ValidationExample container
	 */
	public ValueServiceResponse<ValidationExample> delete(ValidationExamplePK pk);

	/**
	 * Find the ValidationExample by primary key fields.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExample
	 * @return ValidationExampleServiceResponse The retrieved ValidationExample container
	 */
	@GET
	@Path("/ValidationExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationExample> findByPrimaryKey(@PathParam("id") String id);

	/**
	 * Find the ValidationExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationExample
	 * @return ValidationExampleServiceResponse The retrieved ValidationExample container
	 */
	public ValueServiceResponse<ValidationExample> findByPrimaryKey(ValidationExamplePK pk);

}
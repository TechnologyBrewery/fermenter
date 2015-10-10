package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.service.Service;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExamplePK;

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
 * Interface for the ValidationReferenceExampleMaintenance service. This interface defines a the contract needed to
 * create, retrieve, update, and delete a {@link ValidationReferenceExample} instance remotely (e.g., http, RMI).
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local
@Path("/")
public interface ValidationReferenceExampleMaintenanceService extends Service {

	/**
	 * Save or update the passed instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferenceExample
	 * @param entity
	 *            The ValidationReferenceExample instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@PUT
	@Path("/ValidationReferenceExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationReferenceExample> saveOrUpdate(@PathParam("id") String id,
			ValidationReferenceExample entity);

	/**
	 * Save or update the passed instance.
	 * 
	 * @param entity
	 *            The ValidationReferenceExample instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@POST
	@Path("/ValidationReferenceExample")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationReferenceExample> saveOrUpdate(ValidationReferenceExample entity);

	/**
	 * Delete the ValidationReferenceExample.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferenceExample
	 * 
	 * @return ValidationReferenceExampleServiceResponse The deleted ValidationReferenceExample container
	 */
	@DELETE
	@Path("/ValidationReferenceExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationReferenceExample> delete(@PathParam("id") String id);

	/**
	 * Delete the ValidationReferenceExample.
	 * 
	 * @param entity
	 *            ValidationReferenceExample to delete
	 * @return ValidationReferenceExampleServiceResponse The deleted ValidationReferenceExample container
	 */
	public ValueServiceResponse<ValidationReferenceExample> delete(ValidationReferenceExamplePK pk);

	/**
	 * Find the ValidationReferenceExample by primary key fields.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferenceExample
	 * @return ValidationReferenceExampleServiceResponse The retrieved ValidationReferenceExample container
	 */
	@GET
	@Path("/ValidationReferenceExample/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationReferenceExample> findByPrimaryKey(@PathParam("id") String id);

	/**
	 * Find the ValidationReferenceExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferenceExample
	 * @return ValidationReferenceExampleServiceResponse The retrieved ValidationReferenceExample container
	 */
	public ValueServiceResponse<ValidationReferenceExample> findByPrimaryKey(ValidationReferenceExamplePK pk);

}
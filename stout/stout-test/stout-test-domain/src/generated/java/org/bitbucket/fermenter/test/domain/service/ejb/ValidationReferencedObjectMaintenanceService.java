package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.service.Service;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObjectPK;

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
 * Interface for the ValidationReferencedObjectMaintenance service. This interface defines a the contract needed to
 * create, retrieve, update, and delete a {@link ValidationReferencedObject} instance remotely (e.g., http, RMI).
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local
@Path("/")
public interface ValidationReferencedObjectMaintenanceService extends Service {

	/**
	 * Save or update the passed instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferencedObject
	 * @param entity
	 *            The ValidationReferencedObject instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@PUT
	@Path("/ValidationReferencedObject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationReferencedObject> saveOrUpdate(@PathParam("id") String id,
			ValidationReferencedObject entity);

	/**
	 * Save or update the passed instance.
	 * 
	 * @param entity
	 *            The ValidationReferencedObject instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@POST
	@Path("/ValidationReferencedObject")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<ValidationReferencedObject> saveOrUpdate(ValidationReferencedObject entity);

	/**
	 * Delete the ValidationReferencedObject.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferencedObject
	 * 
	 * @return ValidationReferencedObjectServiceResponse The deleted ValidationReferencedObject container
	 */
	@DELETE
	@Path("/ValidationReferencedObject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationReferencedObject> delete(@PathParam("id") String id);

	/**
	 * Delete the ValidationReferencedObject.
	 * 
	 * @param entity
	 *            ValidationReferencedObject to delete
	 * @return ValidationReferencedObjectServiceResponse The deleted ValidationReferencedObject container
	 */
	public ValueServiceResponse<ValidationReferencedObject> delete(ValidationReferencedObjectPK pk);

	/**
	 * Find the ValidationReferencedObject by primary key fields.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferencedObject
	 * @return ValidationReferencedObjectServiceResponse The retrieved ValidationReferencedObject container
	 */
	@GET
	@Path("/ValidationReferencedObject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<ValidationReferencedObject> findByPrimaryKey(@PathParam("id") String id);

	/**
	 * Find the ValidationReferencedObject by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferencedObject
	 * @return ValidationReferencedObjectServiceResponse The retrieved ValidationReferencedObject container
	 */
	public ValueServiceResponse<ValidationReferencedObject> findByPrimaryKey(ValidationReferencedObjectPK pk);

}
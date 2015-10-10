package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.service.Service;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomainPK;

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
 * Interface for the SimpleDomainMaintenance service. This interface defines a the contract needed to create, retrieve,
 * update, and delete a {@link SimpleDomain} instance remotely (e.g., http, RMI).
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local
@Path("/")
public interface SimpleDomainMaintenanceService extends Service {

	/**
	 * Save or update the passed instance.
	 * 
	 * @param id
	 *            The primary key value id for the SimpleDomain
	 * @param entity
	 *            The SimpleDomain instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@PUT
	@Path("/SimpleDomain/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> saveOrUpdate(@PathParam("id") String id, SimpleDomain entity);

	/**
	 * Save or update the passed instance.
	 * 
	 * @param entity
	 *            The SimpleDomain instance to save
	 * @return a service response containing the save entity and/or any encountered messages
	 */
	@POST
	@Path("/SimpleDomain")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> saveOrUpdate(SimpleDomain entity);

	/**
	 * Delete the SimpleDomain.
	 * 
	 * @param id
	 *            The primary key value id for the SimpleDomain
	 * 
	 * @return SimpleDomainServiceResponse The deleted SimpleDomain container
	 */
	@DELETE
	@Path("/SimpleDomain/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<SimpleDomain> delete(@PathParam("id") String id);

	/**
	 * Delete the SimpleDomain.
	 * 
	 * @param entity
	 *            SimpleDomain to delete
	 * @return SimpleDomainServiceResponse The deleted SimpleDomain container
	 */
	public ValueServiceResponse<SimpleDomain> delete(SimpleDomainPK pk);

	/**
	 * Find the SimpleDomain by primary key fields.
	 * 
	 * @param id
	 *            The primary key value id for the SimpleDomain
	 * @return SimpleDomainServiceResponse The retrieved SimpleDomain container
	 */
	@GET
	@Path("/SimpleDomain/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ValueServiceResponse<SimpleDomain> findByPrimaryKey(@PathParam("id") String id);

	/**
	 * Find the SimpleDomain by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the SimpleDomain
	 * @return SimpleDomainServiceResponse The retrieved SimpleDomain container
	 */
	public ValueServiceResponse<SimpleDomain> findByPrimaryKey(SimpleDomainPK pk);

}
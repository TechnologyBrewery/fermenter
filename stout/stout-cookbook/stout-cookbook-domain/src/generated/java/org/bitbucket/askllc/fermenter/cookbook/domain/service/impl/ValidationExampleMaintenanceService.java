package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;

import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareService;
import org.bitbucket.fermenter.stout.messages.DefaultMessages;

import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;

/**
 * Provides create, retrieve, update, and delete (CRUD) functionality for ValidationExample business objects.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Path("ValidationExample")
public class ValidationExampleMaintenanceService extends AbstractMsgMgrAwareService {

	@Inject
	private DefaultMessages messages;

	/**
	 * Save or update the provided instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExample
	 * @param entity
	 *            The ValidationExample instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationExampleBO> saveOrUpdate(@PathParam("id") String id, ValidationExampleBO entity) {
		entity.setKey(id);
		return saveOrUpdate(entity);
	}

	/**
	 * Create the provided instance.
	 * 
	 * @param entity
	 *            The ValidationExample instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationExampleBO> saveOrUpdate(ValidationExampleBO entity) {
		initMsgMgr(messages);
		ValidationExampleBO persistedEntity = entity.save();
		ValueServiceResponse<ValidationExampleBO> response = new ValueServiceResponse<ValidationExampleBO>(
				persistedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Deletes the ValidationExample entity with the provided primary key value.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExample to delete
	 * @return response wrapper containing any collected messages
	 */
	@DELETE
	@Path("{id}")
	@Transactional(propagation = Propagation.REQUIRED)
	public VoidServiceResponse delete(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationExampleBO entityToDelete = ValidationExampleBO.findByPrimaryKey(id);
		if (entityToDelete != null) {
			entityToDelete.delete();
		}
		VoidServiceResponse response = new VoidServiceResponse();
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Find the ValidationExample by primary key
	 * 
	 * @param $
	 *            {id.name} The primary key value ${id.name} for the ValidationExample to retrieve
	 * @return a service response containing the retrieved entity and/or any collected messages
	 */
	@GET
	@Path("{id}")
	@Transactional(propagation = Propagation.SUPPORTS)
	public ValueServiceResponse<ValidationExampleBO> findByPrimaryKey(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationExampleBO retrievedEntity = ValidationExampleBO.findByPrimaryKey(id);
		ValueServiceResponse<ValidationExampleBO> response = new ValueServiceResponse<ValidationExampleBO>(
				retrievedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

}
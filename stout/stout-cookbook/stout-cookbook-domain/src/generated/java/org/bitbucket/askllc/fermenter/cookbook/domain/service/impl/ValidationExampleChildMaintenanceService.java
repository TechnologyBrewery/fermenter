package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;

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
 * Provides create, retrieve, update, and delete (CRUD) functionality for ValidationExampleChild business objects.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Path("ValidationExampleChild")
public class ValidationExampleChildMaintenanceService extends AbstractMsgMgrAwareService {

	@Inject
	private DefaultMessages messages;

	/**
	 * Save or update the provided instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExampleChild
	 * @param entity
	 *            The ValidationExampleChild instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationExampleChildBO> saveOrUpdate(@PathParam("id") String id,
			ValidationExampleChildBO entity) {
		entity.setKey(id);
		return saveOrUpdate(entity);
	}

	/**
	 * Create the provided instance.
	 * 
	 * @param entity
	 *            The ValidationExampleChild instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationExampleChildBO> saveOrUpdate(ValidationExampleChildBO entity) {
		initMsgMgr(messages);
		ValidationExampleChildBO persistedEntity = entity.save();
		ValueServiceResponse<ValidationExampleChildBO> response = new ValueServiceResponse<ValidationExampleChildBO>(
				persistedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Deletes the ValidationExampleChild entity with the provided primary key value.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationExampleChild to delete
	 * @return response wrapper containing any collected messages
	 */
	@DELETE
	@Path("{id}")
	@Transactional(propagation = Propagation.REQUIRED)
	public VoidServiceResponse delete(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationExampleChildBO entityToDelete = ValidationExampleChildBO.findByPrimaryKey(id);
		if (entityToDelete != null) {
			entityToDelete.delete();
		}
		VoidServiceResponse response = new VoidServiceResponse();
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Find the ValidationExampleChild by primary key
	 * 
	 * @param $
	 *            {id.name} The primary key value ${id.name} for the ValidationExampleChild to retrieve
	 * @return a service response containing the retrieved entity and/or any collected messages
	 */
	@GET
	@Path("{id}")
	@Transactional(propagation = Propagation.SUPPORTS)
	public ValueServiceResponse<ValidationExampleChildBO> findByPrimaryKey(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationExampleChildBO retrievedEntity = ValidationExampleChildBO.findByPrimaryKey(id);
		ValueServiceResponse<ValidationExampleChildBO> response = new ValueServiceResponse<ValidationExampleChildBO>(
				retrievedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

}
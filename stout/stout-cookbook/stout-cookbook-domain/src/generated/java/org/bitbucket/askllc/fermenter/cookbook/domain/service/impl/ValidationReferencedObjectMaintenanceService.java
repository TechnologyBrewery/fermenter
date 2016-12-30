package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;

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
 * Provides create, retrieve, update, and delete (CRUD) functionality for ValidationReferencedObject business objects.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Path("ValidationReferencedObject")
public class ValidationReferencedObjectMaintenanceService extends AbstractMsgMgrAwareService {

	@Inject
	private DefaultMessages messages;

	/**
	 * Save or update the provided instance.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferencedObject
	 * @param entity
	 *            The ValidationReferencedObject instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObjectBO> saveOrUpdate(@PathParam("id") String id,
			ValidationReferencedObjectBO entity) {
		entity.setKey(id);
		return saveOrUpdate(entity);
	}

	/**
	 * Create the provided instance.
	 * 
	 * @param entity
	 *            The ValidationReferencedObject instance to save
	 * @return a service response containing the saved entity and/or any collected messages
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObjectBO> saveOrUpdate(ValidationReferencedObjectBO entity) {
		initMsgMgr(messages);
		ValidationReferencedObjectBO persistedEntity = entity.save();
		ValueServiceResponse<ValidationReferencedObjectBO> response = new ValueServiceResponse<ValidationReferencedObjectBO>(
				persistedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Deletes the ValidationReferencedObject entity with the provided primary key value.
	 * 
	 * @param id
	 *            The primary key value id for the ValidationReferencedObject to delete
	 * @return response wrapper containing any collected messages
	 */
	@DELETE
	@Path("{id}")
	@Transactional(propagation = Propagation.REQUIRED)
	public VoidServiceResponse delete(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationReferencedObjectBO entityToDelete = ValidationReferencedObjectBO.findByPrimaryKey(id);
		if (entityToDelete != null) {
			entityToDelete.delete();
		}
		VoidServiceResponse response = new VoidServiceResponse();
		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Find the ValidationReferencedObject by primary key
	 * 
	 * @param $
	 *            {id.name} The primary key value ${id.name} for the ValidationReferencedObject to retrieve
	 * @return a service response containing the retrieved entity and/or any collected messages
	 */
	@GET
	@Path("{id}")
	@Transactional(propagation = Propagation.SUPPORTS)
	public ValueServiceResponse<ValidationReferencedObjectBO> findByPrimaryKey(@PathParam("id") String id) {
		initMsgMgr(messages);
		ValidationReferencedObjectBO retrievedEntity = ValidationReferencedObjectBO.findByPrimaryKey(id);
		ValueServiceResponse<ValidationReferencedObjectBO> response = new ValueServiceResponse<ValidationReferencedObjectBO>(
				retrievedEntity);
		addAllMsgMgrToResponse(response);
		return response;
	}

}
package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObjectPK;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferencedObjectAssembler;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferencedObjectBO;

import javax.annotation.Resource;

import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.inject.Inject;

import org.bitbucket.fermenter.messages.InjectableMessages;
import org.bitbucket.fermenter.messages.ThreadLocalMessages;
import org.bitbucket.fermenter.service.ValueServiceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferencedObjectMaintenanceService;

/**
 * Implementation of an EJB for create, retrieve, update, and delete (CRUD) for ValidationReferencedObject.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local(ValidationReferencedObjectMaintenanceService.class)
@Stateless
@ThreadLocalMessages
public class ValidationReferencedObjectMaintenanceServiceBean implements ValidationReferencedObjectMaintenanceService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValidationReferencedObjectMaintenanceServiceBean.class);

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObject> saveOrUpdate(String id, ValidationReferencedObject entity) {
		if ((id != null) && (entity != null)) {
			// make sure whatever is being saved is has the passed pk value:
			ValidationReferencedObjectPK pk = entity.getValidationReferencedObjectPK();
			pk.setId(id);
			entity.setKey(pk);
		}

		return saveOrUpdate(entity);

	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObject> saveOrUpdate(ValidationReferencedObject entity) {
		LOGGER.debug("Executing ValidationReferencedObject.saveOrUpdate");
		ValueServiceResponse<ValidationReferencedObject> response = null;

		if (entity != null) {
			ValidationReferencedObjectPK pk = entity.getValidationReferencedObjectPK();
			ValidationReferencedObjectBO businessObjectToSave = null;
			ValidationReferencedObjectBO retrievedBusinessObject = ValidationReferencedObjectBO.findByPrimaryKey(pk);

			if (retrievedBusinessObject != null) {
				businessObjectToSave = retrievedBusinessObject;
			} else {
				businessObjectToSave = BusinessObjectFactory.createValidationReferencedObjectBO();
			}

			ValidationReferencedObjectAssembler.mergeValidationReferencedObjectBO(entity, businessObjectToSave);

			businessObjectToSave.save();

			ValidationReferencedObject updateEntity = ValidationReferencedObjectAssembler
					.createValidationReferencedObject(businessObjectToSave);

			response = new ValueServiceResponse<ValidationReferencedObject>(updateEntity, this.messages);

		}

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObject> delete(String id) {
		ValidationReferencedObjectPK pk = TransferObjectFactory.createValidationReferencedObjectPK();
		pk.setId(id);
		return delete(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferencedObject> delete(ValidationReferencedObjectPK pk) {
		LOGGER.debug("Executing ValidationReferencedObject.delete");
		ValidationReferencedObjectBO businessObjectToDelete = BusinessObjectFactory
				.createValidationReferencedObjectBO();
		businessObjectToDelete.setKey(pk);
		businessObjectToDelete.delete();

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return new ValueServiceResponse<ValidationReferencedObject>(null, this.messages);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationReferencedObject> findByPrimaryKey(String id) {
		ValidationReferencedObjectPK pk = TransferObjectFactory.createValidationReferencedObjectPK();
		pk.setId(id);
		return findByPrimaryKey(pk);

	}

	/**
	 * Find the ValidationReferencedObject by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferencedObject
	 * @return ValidationReferencedObjectServiceResponse The retrieved ValidationReferencedObject container
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationReferencedObject> findByPrimaryKey(ValidationReferencedObjectPK pk) {
		LOGGER.debug("Executing ValidationReferencedObject.findByPrimaryKey");
		ValidationReferencedObject retrieveEntity = null;

		ValidationReferencedObjectBO retrievedBusinessObject = ValidationReferencedObjectBO.findByPrimaryKey(pk);
		if (retrievedBusinessObject != null) {
			retrieveEntity = ValidationReferencedObjectAssembler
					.createValidationReferencedObject(retrievedBusinessObject);

		}

		return new ValueServiceResponse<ValidationReferencedObject>(retrieveEntity, this.messages);
	}

}
package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationExamplePK;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationExampleAssembler;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationExampleBO;

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

import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleMaintenanceService;

/**
 * Implementation of an EJB for create, retrieve, update, and delete (CRUD) for ValidationExample.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local(ValidationExampleMaintenanceService.class)
@Stateless
@ThreadLocalMessages
public class ValidationExampleMaintenanceServiceBean implements ValidationExampleMaintenanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleMaintenanceServiceBean.class);

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExample> saveOrUpdate(String id, ValidationExample entity) {
		if ((id != null) && (entity != null)) {
			// make sure whatever is being saved is has the passed pk value:
			ValidationExamplePK pk = entity.getValidationExamplePK();
			pk.setId(id);
			entity.setKey(pk);
		}

		return saveOrUpdate(entity);

	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExample> saveOrUpdate(ValidationExample entity) {
		LOGGER.debug("Executing ValidationExample.saveOrUpdate");
		ValueServiceResponse<ValidationExample> response = null;

		if (entity != null) {
			ValidationExamplePK pk = entity.getValidationExamplePK();
			ValidationExampleBO businessObjectToSave = null;
			ValidationExampleBO retrievedBusinessObject = ValidationExampleBO.findByPrimaryKey(pk);

			if (retrievedBusinessObject != null) {
				businessObjectToSave = retrievedBusinessObject;
			} else {
				businessObjectToSave = BusinessObjectFactory.createValidationExampleBO();
			}

			ValidationExampleAssembler.mergeValidationExampleBO(entity, businessObjectToSave);

			businessObjectToSave.save();

			ValidationExample updateEntity = ValidationExampleAssembler.createValidationExample(businessObjectToSave);

			response = new ValueServiceResponse<ValidationExample>(updateEntity, this.messages);

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
	public ValueServiceResponse<ValidationExample> delete(String id) {
		ValidationExamplePK pk = TransferObjectFactory.createValidationExamplePK();
		pk.setId(id);
		return delete(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExample> delete(ValidationExamplePK pk) {
		LOGGER.debug("Executing ValidationExample.delete");
		ValidationExampleBO businessObjectToDelete = BusinessObjectFactory.createValidationExampleBO();
		businessObjectToDelete.setKey(pk);
		businessObjectToDelete.delete();

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return new ValueServiceResponse<ValidationExample>(null, this.messages);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationExample> findByPrimaryKey(String id) {
		ValidationExamplePK pk = TransferObjectFactory.createValidationExamplePK();
		pk.setId(id);
		return findByPrimaryKey(pk);

	}

	/**
	 * Find the ValidationExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationExample
	 * @return ValidationExampleServiceResponse The retrieved ValidationExample container
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationExample> findByPrimaryKey(ValidationExamplePK pk) {
		LOGGER.debug("Executing ValidationExample.findByPrimaryKey");
		ValidationExample retrieveEntity = null;

		ValidationExampleBO retrievedBusinessObject = ValidationExampleBO.findByPrimaryKey(pk);
		if (retrievedBusinessObject != null) {
			retrieveEntity = ValidationExampleAssembler.createValidationExample(retrievedBusinessObject);

		}

		return new ValueServiceResponse<ValidationExample>(retrieveEntity, this.messages);
	}

}
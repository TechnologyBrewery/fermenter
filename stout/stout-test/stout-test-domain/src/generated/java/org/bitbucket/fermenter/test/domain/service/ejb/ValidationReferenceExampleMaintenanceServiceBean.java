package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExamplePK;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferenceExampleAssembler;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferenceExampleBO;

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

import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferenceExampleMaintenanceService;

/**
 * Implementation of an EJB for create, retrieve, update, and delete (CRUD) for ValidationReferenceExample.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local(ValidationReferenceExampleMaintenanceService.class)
@Stateless
@ThreadLocalMessages
public class ValidationReferenceExampleMaintenanceServiceBean implements ValidationReferenceExampleMaintenanceService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValidationReferenceExampleMaintenanceServiceBean.class);

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferenceExample> saveOrUpdate(String id, ValidationReferenceExample entity) {
		if ((id != null) && (entity != null)) {
			// make sure whatever is being saved is has the passed pk value:
			ValidationReferenceExamplePK pk = entity.getValidationReferenceExamplePK();
			pk.setId(id);
			entity.setKey(pk);
		}

		return saveOrUpdate(entity);

	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferenceExample> saveOrUpdate(ValidationReferenceExample entity) {
		LOGGER.debug("Executing ValidationReferenceExample.saveOrUpdate");
		ValueServiceResponse<ValidationReferenceExample> response = null;

		if (entity != null) {
			ValidationReferenceExamplePK pk = entity.getValidationReferenceExamplePK();
			ValidationReferenceExampleBO businessObjectToSave = null;
			ValidationReferenceExampleBO retrievedBusinessObject = ValidationReferenceExampleBO.findByPrimaryKey(pk);

			if (retrievedBusinessObject != null) {
				businessObjectToSave = retrievedBusinessObject;
			} else {
				businessObjectToSave = BusinessObjectFactory.createValidationReferenceExampleBO();
			}

			ValidationReferenceExampleAssembler.mergeValidationReferenceExampleBO(entity, businessObjectToSave);

			businessObjectToSave.save();

			ValidationReferenceExample updateEntity = ValidationReferenceExampleAssembler
					.createValidationReferenceExample(businessObjectToSave);

			response = new ValueServiceResponse<ValidationReferenceExample>(updateEntity, this.messages);

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
	public ValueServiceResponse<ValidationReferenceExample> delete(String id) {
		ValidationReferenceExamplePK pk = TransferObjectFactory.createValidationReferenceExamplePK();
		pk.setId(id);
		return delete(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationReferenceExample> delete(ValidationReferenceExamplePK pk) {
		LOGGER.debug("Executing ValidationReferenceExample.delete");
		ValidationReferenceExampleBO businessObjectToDelete = BusinessObjectFactory
				.createValidationReferenceExampleBO();
		businessObjectToDelete.setKey(pk);
		businessObjectToDelete.delete();

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return new ValueServiceResponse<ValidationReferenceExample>(null, this.messages);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationReferenceExample> findByPrimaryKey(String id) {
		ValidationReferenceExamplePK pk = TransferObjectFactory.createValidationReferenceExamplePK();
		pk.setId(id);
		return findByPrimaryKey(pk);

	}

	/**
	 * Find the ValidationReferenceExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferenceExample
	 * @return ValidationReferenceExampleServiceResponse The retrieved ValidationReferenceExample container
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationReferenceExample> findByPrimaryKey(ValidationReferenceExamplePK pk) {
		LOGGER.debug("Executing ValidationReferenceExample.findByPrimaryKey");
		ValidationReferenceExample retrieveEntity = null;

		ValidationReferenceExampleBO retrievedBusinessObject = ValidationReferenceExampleBO.findByPrimaryKey(pk);
		if (retrievedBusinessObject != null) {
			retrieveEntity = ValidationReferenceExampleAssembler
					.createValidationReferenceExample(retrievedBusinessObject);

		}

		return new ValueServiceResponse<ValidationReferenceExample>(retrieveEntity, this.messages);
	}

}
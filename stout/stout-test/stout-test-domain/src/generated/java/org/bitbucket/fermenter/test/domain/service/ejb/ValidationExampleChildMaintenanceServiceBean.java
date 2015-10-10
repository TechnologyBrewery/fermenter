package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChild;
import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChildPK;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationExampleChildAssembler;
import org.bitbucket.fermenter.test.domain.bizobj.ValidationExampleChildBO;

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

import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleChildMaintenanceService;

/**
 * Implementation of an EJB for create, retrieve, update, and delete (CRUD) for ValidationExampleChild.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local(ValidationExampleChildMaintenanceService.class)
@Stateless
@ThreadLocalMessages
public class ValidationExampleChildMaintenanceServiceBean implements ValidationExampleChildMaintenanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleChildMaintenanceServiceBean.class);

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExampleChild> saveOrUpdate(String id, ValidationExampleChild entity) {
		if ((id != null) && (entity != null)) {
			// make sure whatever is being saved is has the passed pk value:
			ValidationExampleChildPK pk = entity.getValidationExampleChildPK();
			pk.setId(id);
			entity.setKey(pk);
		}

		return saveOrUpdate(entity);

	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExampleChild> saveOrUpdate(ValidationExampleChild entity) {
		LOGGER.debug("Executing ValidationExampleChild.saveOrUpdate");
		ValueServiceResponse<ValidationExampleChild> response = null;

		if (entity != null) {
			ValidationExampleChildPK pk = entity.getValidationExampleChildPK();
			ValidationExampleChildBO businessObjectToSave = null;
			ValidationExampleChildBO retrievedBusinessObject = ValidationExampleChildBO.findByPrimaryKey(pk);

			if (retrievedBusinessObject != null) {
				businessObjectToSave = retrievedBusinessObject;
			} else {
				businessObjectToSave = BusinessObjectFactory.createValidationExampleChildBO();
			}

			ValidationExampleChildAssembler.mergeValidationExampleChildBO(entity, businessObjectToSave);

			businessObjectToSave.save();

			ValidationExampleChild updateEntity = ValidationExampleChildAssembler
					.createValidationExampleChild(businessObjectToSave);

			response = new ValueServiceResponse<ValidationExampleChild>(updateEntity, this.messages);

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
	public ValueServiceResponse<ValidationExampleChild> delete(String id) {
		ValidationExampleChildPK pk = TransferObjectFactory.createValidationExampleChildPK();
		pk.setId(id);
		return delete(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<ValidationExampleChild> delete(ValidationExampleChildPK pk) {
		LOGGER.debug("Executing ValidationExampleChild.delete");
		ValidationExampleChildBO businessObjectToDelete = BusinessObjectFactory.createValidationExampleChildBO();
		businessObjectToDelete.setKey(pk);
		businessObjectToDelete.delete();

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return new ValueServiceResponse<ValidationExampleChild>(null, this.messages);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationExampleChild> findByPrimaryKey(String id) {
		ValidationExampleChildPK pk = TransferObjectFactory.createValidationExampleChildPK();
		pk.setId(id);
		return findByPrimaryKey(pk);

	}

	/**
	 * Find the ValidationExampleChild by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationExampleChild
	 * @return ValidationExampleChildServiceResponse The retrieved ValidationExampleChild container
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<ValidationExampleChild> findByPrimaryKey(ValidationExampleChildPK pk) {
		LOGGER.debug("Executing ValidationExampleChild.findByPrimaryKey");
		ValidationExampleChild retrieveEntity = null;

		ValidationExampleChildBO retrievedBusinessObject = ValidationExampleChildBO.findByPrimaryKey(pk);
		if (retrievedBusinessObject != null) {
			retrieveEntity = ValidationExampleChildAssembler.createValidationExampleChild(retrievedBusinessObject);

		}

		return new ValueServiceResponse<ValidationExampleChild>(retrieveEntity, this.messages);
	}

}
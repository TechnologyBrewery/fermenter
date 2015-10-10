package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomainPK;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.test.domain.bizobj.SimpleDomainAssembler;
import org.bitbucket.fermenter.test.domain.bizobj.SimpleDomainBO;

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

import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainMaintenanceService;

/**
 * Implementation of an EJB for create, retrieve, update, and delete (CRUD) for SimpleDomain.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local(SimpleDomainMaintenanceService.class)
@Stateless
@ThreadLocalMessages
public class SimpleDomainMaintenanceServiceBean implements SimpleDomainMaintenanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainMaintenanceServiceBean.class);

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<SimpleDomain> saveOrUpdate(String id, SimpleDomain entity) {
		if ((id != null) && (entity != null)) {
			// make sure whatever is being saved is has the passed pk value:
			SimpleDomainPK pk = entity.getSimpleDomainPK();
			pk.setId(id);
			entity.setKey(pk);
		}

		return saveOrUpdate(entity);

	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<SimpleDomain> saveOrUpdate(SimpleDomain entity) {
		LOGGER.debug("Executing SimpleDomain.saveOrUpdate");
		ValueServiceResponse<SimpleDomain> response = null;

		if (entity != null) {
			SimpleDomainPK pk = entity.getSimpleDomainPK();
			SimpleDomainBO businessObjectToSave = null;
			SimpleDomainBO retrievedBusinessObject = SimpleDomainBO.findByPrimaryKey(pk);

			if (retrievedBusinessObject != null) {
				businessObjectToSave = retrievedBusinessObject;
			} else {
				businessObjectToSave = BusinessObjectFactory.createSimpleDomainBO();
			}

			SimpleDomainAssembler.mergeSimpleDomainBO(entity, businessObjectToSave);

			businessObjectToSave.save();

			SimpleDomain updateEntity = SimpleDomainAssembler.createSimpleDomain(businessObjectToSave);

			response = new ValueServiceResponse<SimpleDomain>(updateEntity, this.messages);

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
	public ValueServiceResponse<SimpleDomain> delete(String id) {
		SimpleDomainPK pk = TransferObjectFactory.createSimpleDomainPK();
		pk.setId(id);
		return delete(pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ValueServiceResponse<SimpleDomain> delete(SimpleDomainPK pk) {
		LOGGER.debug("Executing SimpleDomain.delete");
		SimpleDomainBO businessObjectToDelete = BusinessObjectFactory.createSimpleDomainBO();
		businessObjectToDelete.setKey(pk);
		businessObjectToDelete.delete();

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}

		return new ValueServiceResponse<SimpleDomain>(null, this.messages);
	}

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<SimpleDomain> findByPrimaryKey(String id) {
		SimpleDomainPK pk = TransferObjectFactory.createSimpleDomainPK();
		pk.setId(id);
		return findByPrimaryKey(pk);

	}

	/**
	 * Find the SimpleDomain by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the SimpleDomain
	 * @return SimpleDomainServiceResponse The retrieved SimpleDomain container
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ValueServiceResponse<SimpleDomain> findByPrimaryKey(SimpleDomainPK pk) {
		LOGGER.debug("Executing SimpleDomain.findByPrimaryKey");
		SimpleDomain retrieveEntity = null;

		SimpleDomainBO retrievedBusinessObject = SimpleDomainBO.findByPrimaryKey(pk);
		if (retrievedBusinessObject != null) {
			retrieveEntity = SimpleDomainAssembler.createSimpleDomain(retrievedBusinessObject);

		}

		return new ValueServiceResponse<SimpleDomain>(retrieveEntity, this.messages);
	}

}
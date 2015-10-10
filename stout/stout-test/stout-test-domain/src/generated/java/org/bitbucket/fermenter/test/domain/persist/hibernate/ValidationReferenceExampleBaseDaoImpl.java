package org.bitbucket.fermenter.test.domain.persist.hibernate;

import java.io.Serializable;

import org.bitbucket.fermenter.persist.hibernate.AbstractHibernateDaoImpl;

import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferenceExampleBO;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExamplePK;
import org.bitbucket.fermenter.test.domain.persist.ValidationReferenceExampleBaseDao;

import org.hibernate.Hibernate;
import org.hibernate.type.*;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Data access object for the ValidationReferenceExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationReferenceExampleBaseDaoImpl extends
		AbstractHibernateDaoImpl<ValidationReferenceExampleBO, ValidationReferenceExamplePK> implements
		ValidationReferenceExampleBaseDao {

	/**
	 * Creates a new {@link ValidationReferenceExamplePK} wrapper based on the given String ID value that was generated
	 * by Hibernate.
	 * 
	 * @param persistentIDValue
	 *            ID that was created by Hibernate as a result of making a transient
	 *            {@link ValidationReferenceExampleBO} persistent.
	 * @return wrapper {@link ValidationReferenceExamplePK} object that encapsulates the newly created
	 *         {@link ValidationReferenceExampleBO}'s ID.
	 */
	@Override
	protected ValidationReferenceExamplePK getNewPKWrapperForPersistentIDValue(Serializable persistentIDValue) {
		ValidationReferenceExamplePK pk = TransferObjectFactory.createValidationReferenceExamplePK();
		pk.setId((String) persistentIDValue);
		return pk;
	}

	/**
	 * Returns the underlying {@link ValidationReferenceExampleBO} implementation {@link Class} whose data access logic
	 * is managed by this DAO.
	 * 
	 * @return
	 */
	@Override
	protected Class<ValidationReferenceExampleBO> getBusinessObjectClazz() {
		return ValidationReferenceExampleBO.class;
	}

	/**
	 * Returns the logical name for the entity whose data access logic is managed by this DAO.
	 * 
	 * @return
	 */
	@Override
	protected String getEntityName() {
		return ValidationReferenceExample.ENTITY;
	}

}
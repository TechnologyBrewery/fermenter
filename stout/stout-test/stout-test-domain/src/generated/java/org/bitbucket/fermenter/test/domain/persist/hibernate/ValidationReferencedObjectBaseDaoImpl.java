package org.bitbucket.fermenter.test.domain.persist.hibernate;

import java.io.Serializable;

import org.bitbucket.fermenter.persist.hibernate.AbstractHibernateDaoImpl;

import org.bitbucket.fermenter.test.domain.bizobj.ValidationReferencedObjectBO;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObjectPK;
import org.bitbucket.fermenter.test.domain.persist.ValidationReferencedObjectBaseDao;

import org.hibernate.Hibernate;
import org.hibernate.type.*;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Data access object for the ValidationReferencedObject application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationReferencedObjectBaseDaoImpl extends
		AbstractHibernateDaoImpl<ValidationReferencedObjectBO, ValidationReferencedObjectPK> implements
		ValidationReferencedObjectBaseDao {

	/**
	 * Creates a new {@link ValidationReferencedObjectPK} wrapper based on the given String ID value that was generated
	 * by Hibernate.
	 * 
	 * @param persistentIDValue
	 *            ID that was created by Hibernate as a result of making a transient
	 *            {@link ValidationReferencedObjectBO} persistent.
	 * @return wrapper {@link ValidationReferencedObjectPK} object that encapsulates the newly created
	 *         {@link ValidationReferencedObjectBO}'s ID.
	 */
	@Override
	protected ValidationReferencedObjectPK getNewPKWrapperForPersistentIDValue(Serializable persistentIDValue) {
		ValidationReferencedObjectPK pk = TransferObjectFactory.createValidationReferencedObjectPK();
		pk.setId((String) persistentIDValue);
		return pk;
	}

	/**
	 * Returns the underlying {@link ValidationReferencedObjectBO} implementation {@link Class} whose data access logic
	 * is managed by this DAO.
	 * 
	 * @return
	 */
	@Override
	protected Class<ValidationReferencedObjectBO> getBusinessObjectClazz() {
		return ValidationReferencedObjectBO.class;
	}

	/**
	 * Returns the logical name for the entity whose data access logic is managed by this DAO.
	 * 
	 * @return
	 */
	@Override
	protected String getEntityName() {
		return ValidationReferencedObject.ENTITY;
	}

}
package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.persist.*;
import org.bitbucket.fermenter.test.domain.transfer.*;

import org.bitbucket.fermenter.transfer.PrimaryKey;
import org.bitbucket.fermenter.transfer.TransferObject;
import org.bitbucket.fermenter.bizobj.*;
import org.bitbucket.fermenter.validate.Validations;

import org.bitbucket.fermenter.messages.MessageManager;
import org.bitbucket.fermenter.messages.Messages;
import org.bitbucket.fermenter.messages.MessageUtils;

import java.util.UUID;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

/**
 * Business object for the ValidationReferencedObject application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationReferencedObjectBaseBO extends BaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferencedObjectBO.class);

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	private static final ValidationReferencedObjectDao DAO = DaoFactory.createValidationReferencedObjectDao();

	private String id;
	private Integer oplock;
	private String someDataField;

	/**
	 * {@inheritDoc}
	 */
	protected Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Get the values for this ValidationReferencedObject entity.
	 */
	public ValidationReferencedObject getValidationReferencedObjectValues() {
		return ValidationReferencedObjectAssembler
				.createValidationReferencedObject((ValidationReferencedObjectBO) this);
	}

	/**
	 * Set the values for this ValidationReferencedObject entity.
	 */
	public void setValidationReferencedObjectValues(ValidationReferencedObject entity) {
		ValidationReferencedObjectAssembler.mergeValidationReferencedObjectBO(entity,
				(ValidationReferencedObjectBO) this);
	}

	/**
	 * Get the validation reference object id.
	 * 
	 * @return The validation reference object id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the validation reference object id.
	 * 
	 * @param The
	 *            validation reference object id
	 */
	protected void setId(String id) {
		id = StringUtils.trimToNull(id);
		this.id = id;
	}

	/**
	 * Get the optimistic locking value.
	 * 
	 * @return The optimistic locking value
	 */
	public Integer getOplock() {
		return oplock;
	}

	/**
	 * Set the optimistic locking value.
	 * 
	 * @param The
	 *            optimistic locking value
	 */
	void setOplock(Integer oplock) {
		this.oplock = oplock;
	}

	@Override
	protected void complexValidationOnComposites() {
	}

	/**
	 * Get the some data field.
	 * 
	 * @return The some data field
	 */
	public String getSomeDataField() {
		return someDataField;
	}

	/**
	 * Set the some data field.
	 * 
	 * @param The
	 *            some data field
	 */
	public void setSomeDataField(String someDataField) {
		someDataField = StringUtils.trimToNull(someDataField);
		this.someDataField = someDataField;
	}

	/**
	 * Validates some data field.
	 */
	protected void validateSomeDataField() {
		String value = getSomeDataField();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the primary key for the ValidationReferencedObject.
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationReferencedObjectPK();
	}

	/**
	 * Get the primary key for the ValidationReferencedObject.
	 * 
	 */
	public ValidationReferencedObjectPK getValidationReferencedObjectPK() {
		return new ValidationReferencedObjectPK(id);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationReferencedObjectPK((ValidationReferencedObjectPK) pk);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setValidationReferencedObjectPK(ValidationReferencedObjectPK pk) {

		this.id = pk.getId();
	}

	/**
	 * Executes all field-level validations.
	 */
	@Override
	protected void fieldValidation() {
		validateSomeDataField();

	}

	@Override
	protected void compositeValidation() {
	}

	/**
	 * Executes all reference-level validations.
	 */
	@Override
	protected void referenceValidation() {

	}

	/**
	 * Executes all complex validation on children.
	 */
	@Override
	protected void complexValidationOnChildren() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValidationReferencedObjectDao getDao() {
		return ValidationReferencedObjectBO.getDefaultDao();
	}

	protected static ValidationReferencedObjectDao getDefaultDao() {
		return DAO;
	}

	/**
	 * Find the ValidationReferencedObject by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferencedObject
	 * @return ValidationReferencedObject The retrieved ValidationReferencedObject
	 */
	public static ValidationReferencedObjectBO findByPrimaryKey(ValidationReferencedObjectPK pk) {
		return ValidationReferencedObjectBO.getDefaultDao().findByPrimaryKey(pk);
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationReferencedObjectBO bo = (ValidationReferencedObjectBO) o;
			// Can't be null!
			PrimaryKey thisPk = getKey();
			PrimaryKey thatPk = (bo == null) ? null : bo.getKey();
			if (thatPk != null && thisPk.getValue() == null && thatPk.getValue() == null) {
				return this.internalTransientID == bo.internalTransientID;
			}
			if (thisPk == thatPk || (thisPk.equals(thatPk))) {
				areEqual = true;
			}
		} catch (ClassCastException ex) {
			areEqual = false;
		}

		return areEqual;
	}

	public int hashCode() {
		return (getKey().getValue() == null) ? internalTransientID.hashCode() : getKey().getValue().hashCode();
	}

}
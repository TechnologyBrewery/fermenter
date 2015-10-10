package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.persist.*;
import org.bitbucket.fermenter.test.domain.transfer.*;

import org.bitbucket.fermenter.transfer.PrimaryKey;
import org.bitbucket.fermenter.transfer.TransferObject;
import org.bitbucket.fermenter.bizobj.*;
import org.bitbucket.fermenter.persist.*;
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
 * Business object for the ValidationReferenceExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationReferenceExampleBaseBO extends BaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferenceExampleBO.class);

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	private static final ValidationReferenceExampleDao DAO = DaoFactory.createValidationReferenceExampleDao();

	private String id;
	private Integer oplock;
	private String someDataField;
	private ValidationReferencedObjectBO requiredReference;

	/**
	 * {@inheritDoc}
	 */
	protected Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Get the values for this ValidationReferenceExample entity.
	 */
	public ValidationReferenceExample getValidationReferenceExampleValues() {
		return ValidationReferenceExampleAssembler
				.createValidationReferenceExample((ValidationReferenceExampleBO) this);
	}

	/**
	 * Set the values for this ValidationReferenceExample entity.
	 */
	public void setValidationReferenceExampleValues(ValidationReferenceExample entity) {
		ValidationReferenceExampleAssembler.mergeValidationReferenceExampleBO(entity,
				(ValidationReferenceExampleBO) this);
	}

	/**
	 * Get the validation reference example id.
	 * 
	 * @return The validation reference example id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the validation reference example id.
	 * 
	 * @param The
	 *            validation reference example id
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
	 * Get the RequiredReference reference.
	 * 
	 * @return ValidationReferencedObjectBO - The RequiredReference
	 */
	public ValidationReferencedObjectBO getRequiredReference() {
		return requiredReference;

	}

	/**
	 * Set the RequiredReference reference.
	 * 
	 * @param The
	 *            RequiredReference
	 */
	public void setRequiredReference(ValidationReferencedObjectBO requiredReference) {
		this.requiredReference = requiredReference;
	}

	/**
	 * Validates RequiredReference.
	 */
	protected void validateRequiredReference() {
		ValidationReferencedObjectBO ref = getRequiredReference();
		// check requiredness:
		if (ref == null) {
			MessageManager.addMessage(MessageUtils.createErrorMessage("null.not.allowed",
					new String[] { "ValidationReferencedObject.requiredReference" },
					new Object[] { "requiredReference" }));

			// no need to continue:
			return;
		}

		// ensure that this is a valid reference instance. First,
		// check to see if the pk has a non-null value. If not, then
		// try to load the reference:
		ValidationReferencedObjectBO retrievedRef = null;
		boolean shouldValidate = true;
		ValidationReferencedObjectPK pk = (ValidationReferencedObjectPK) ref.getKey();
		shouldValidate = (pk.getValue() != null);
		if (shouldValidate) {
			retrievedRef = ValidationReferencedObjectBO.findByPrimaryKey(pk);
		}
		if (retrievedRef == null) {
			MessageManager.addMessage(MessageUtils.createErrorMessage("invalid.reference",
					new String[] { "ValidationReferencedObject.requiredReference" },
					new Object[] { "requiredReference" }));

		}

	}

	/**
	 * Get the primary key for the ValidationReferenceExample.
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationReferenceExamplePK();
	}

	/**
	 * Get the primary key for the ValidationReferenceExample.
	 * 
	 */
	public ValidationReferenceExamplePK getValidationReferenceExamplePK() {
		return new ValidationReferenceExamplePK(id);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationReferenceExamplePK((ValidationReferenceExamplePK) pk);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setValidationReferenceExamplePK(ValidationReferenceExamplePK pk) {

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
		validateRequiredReference();

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
	protected ValidationReferenceExampleDao getDao() {
		return ValidationReferenceExampleBO.getDefaultDao();
	}

	protected static ValidationReferenceExampleDao getDefaultDao() {
		return DAO;
	}

	/**
	 * Find the ValidationReferenceExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationReferenceExample
	 * @return ValidationReferenceExample The retrieved ValidationReferenceExample
	 */
	public static ValidationReferenceExampleBO findByPrimaryKey(ValidationReferenceExamplePK pk) {
		return ValidationReferenceExampleBO.getDefaultDao().findByPrimaryKey(pk);
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationReferenceExampleBO bo = (ValidationReferenceExampleBO) o;
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
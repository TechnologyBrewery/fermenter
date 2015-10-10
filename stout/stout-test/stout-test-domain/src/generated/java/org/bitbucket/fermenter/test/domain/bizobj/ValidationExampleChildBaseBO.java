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
 * Business object for the ValidationExampleChild application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleChildBaseBO extends BaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleChildBO.class);

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	private static final ValidationExampleChildDao DAO = DaoFactory.createValidationExampleChildDao();

	private String id;
	private Integer oplock;
	private String requiredField;
	private ValidationExampleBO parentValidationExample;

	/**
	 * {@inheritDoc}
	 */
	protected Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Get the values for this ValidationExampleChild entity.
	 */
	public ValidationExampleChild getValidationExampleChildValues() {
		return ValidationExampleChildAssembler.createValidationExampleChild((ValidationExampleChildBO) this);
	}

	/**
	 * Set the values for this ValidationExampleChild entity.
	 */
	public void setValidationExampleChildValues(ValidationExampleChild entity) {
		// make sure to keep the parent relationship instances straight:
		ValidationExampleBO originalParentValidationExample = parentValidationExample;
		ValidationExampleChildAssembler.mergeValidationExampleChildBO(entity, (ValidationExampleChildBO) this);
		parentValidationExample = originalParentValidationExample;
	}

	/**
	 * Get the one to many child id.
	 * 
	 * @return The one to many child id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the one to many child id.
	 * 
	 * @param The
	 *            one to many child id
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
	 * Get the child required field.
	 * 
	 * @return The child required field
	 */
	public String getRequiredField() {
		return requiredField;
	}

	/**
	 * Set the child required field.
	 * 
	 * @param The
	 *            child required field
	 */
	public void setRequiredField(String requiredField) {
		requiredField = StringUtils.trimToNull(requiredField);
		this.requiredField = requiredField;
	}

	/**
	 * Validates child required field.
	 */
	protected void validateRequiredField() {
		String value = getRequiredField();
		// check requiredness:
		if (value == null) {
			Validations.validateRequired(value, "ValidationExampleChild.requiredField", "child required field",
					MessageManager.getMessages());

			// no need to continue:
			return;
		}
	}

	/**
	 * Set the parent ValidationExample onto this instance.
	 * 
	 * @param parent
	 *            The parent instance to set
	 */
	public void setValidationExample(ValidationExampleBO parent) {
		parentValidationExample = parent;
	}

	/**
	 * Returns the parent of the type for this instance.
	 * 
	 * @return The parent instance or null if no parent of this type exists
	 */
	public ValidationExampleBO getValidationExample() {
		return parentValidationExample;
	}

	/**
	 * Get the primary key for the ValidationExampleChild.
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationExampleChildPK();
	}

	/**
	 * Get the primary key for the ValidationExampleChild.
	 * 
	 */
	public ValidationExampleChildPK getValidationExampleChildPK() {
		return new ValidationExampleChildPK(id);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationExampleChildPK((ValidationExampleChildPK) pk);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setValidationExampleChildPK(ValidationExampleChildPK pk) {

		this.id = pk.getId();
	}

	/**
	 * Executes all field-level validations.
	 */
	@Override
	protected void fieldValidation() {
		validateRequiredField();

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
	protected ValidationExampleChildDao getDao() {
		return ValidationExampleChildBO.getDefaultDao();
	}

	protected static ValidationExampleChildDao getDefaultDao() {
		return DAO;
	}

	/**
	 * Find the ValidationExampleChild by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationExampleChild
	 * @return ValidationExampleChild The retrieved ValidationExampleChild
	 */
	public static ValidationExampleChildBO findByPrimaryKey(ValidationExampleChildPK pk) {
		return ValidationExampleChildBO.getDefaultDao().findByPrimaryKey(pk);
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationExampleChildBO bo = (ValidationExampleChildBO) o;
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
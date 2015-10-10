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

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

/**
 * Business object for the ValidationExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleBaseBO extends BaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleBO.class);

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	private static final ValidationExampleDao DAO = DaoFactory.createValidationExampleDao();

	private static final Integer INTEGEREXAMPLE_MIN_VALUE = new Integer("-12345");
	private static final Integer INTEGEREXAMPLE_MAX_VALUE = new Integer("12345");
	private static final int BIGDECIMALEXAMPLEWITHSCALE_SCALE = 3;
	private static final BigDecimal BIGDECIMALEXAMPLE_MIN_VALUE = new BigDecimal("-123456789.123456789");
	private static final BigDecimal BIGDECIMALEXAMPLE_MAX_VALUE = new BigDecimal("123456789.123456789");
	public static final int STRINGEXAMPLE_MIN_LENGTH = 2;
	public static final int STRINGEXAMPLE_MAX_LENGTH = 20;
	private static final Long LONGEXAMPLE_MIN_VALUE = new Long("-123456789");
	private static final Long LONGEXAMPLE_MAX_VALUE = new Long("123456789");

	private String id;
	private Integer oplock;
	private Integer integerExample;
	private BigDecimal bigDecimalExampleWithScale;
	private String requiredField;
	private BigDecimal bigDecimalExample;
	private String characterExample;
	private String stringExample;
	private Date dateExample;
	private Long longExample;
	private Set<ValidationExampleChildBO> validationExampleChilds;

	/**
	 * {@inheritDoc}
	 */
	protected Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Get the values for this ValidationExample entity.
	 */
	public ValidationExample getValidationExampleValues() {
		return ValidationExampleAssembler.createValidationExample((ValidationExampleBO) this);
	}

	/**
	 * Set the values for this ValidationExample entity.
	 */
	public void setValidationExampleValues(ValidationExample entity) {
		ValidationExampleAssembler.mergeValidationExampleBO(entity, (ValidationExampleBO) this);
	}

	/**
	 * Get the validation example id.
	 * 
	 * @return The validation example id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the validation example id.
	 * 
	 * @param The
	 *            validation example id
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
	 * Get the integerExample.
	 * 
	 * @return The integerExample
	 */
	public Integer getIntegerExample() {
		return integerExample;
	}

	/**
	 * Set the integerExample.
	 * 
	 * @param The
	 *            integerExample
	 */
	public void setIntegerExample(Integer integerExample) {
		this.integerExample = integerExample;
	}

	/**
	 * Validates integerExample.
	 */
	protected void validateIntegerExample() {
		Integer value = getIntegerExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
		// check min value
		Validations.validateMinValue(value, INTEGEREXAMPLE_MIN_VALUE, "ValidationExample.integerExample",
				"integerExample", MessageManager.getMessages());
		// check max value
		Validations.validateMaxValue(value, INTEGEREXAMPLE_MAX_VALUE, "ValidationExample.integerExample",
				"integerExample", MessageManager.getMessages());
	}

	/**
	 * Get the bigDecimalExampleWithScale.
	 * 
	 * @return The bigDecimalExampleWithScale
	 */
	public BigDecimal getBigDecimalExampleWithScale() {
		return bigDecimalExampleWithScale;
	}

	/**
	 * Set the bigDecimalExampleWithScale.
	 * 
	 * @param The
	 *            bigDecimalExampleWithScale
	 */
	public void setBigDecimalExampleWithScale(BigDecimal bigDecimalExampleWithScale) {
		this.bigDecimalExampleWithScale = bigDecimalExampleWithScale;
	}

	/**
	 * Validates bigDecimalExampleWithScale.
	 */
	protected void validateBigDecimalExampleWithScale() {
		BigDecimal value = getBigDecimalExampleWithScale();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
		// check scale
		Validations.validateScale(value, BIGDECIMALEXAMPLEWITHSCALE_SCALE,
				"ValidationExample.bigDecimalExampleWithScale", "bigDecimalExampleWithScale",
				MessageManager.getMessages());
	}

	/**
	 * Get the required field.
	 * 
	 * @return The required field
	 */
	public String getRequiredField() {
		return requiredField;
	}

	/**
	 * Set the required field.
	 * 
	 * @param The
	 *            required field
	 */
	public void setRequiredField(String requiredField) {
		requiredField = StringUtils.trimToNull(requiredField);
		this.requiredField = requiredField;
	}

	/**
	 * Validates required field.
	 */
	protected void validateRequiredField() {
		String value = getRequiredField();
		// check requiredness:
		if (value == null) {
			Validations.validateRequired(value, "ValidationExample.requiredField", "required field",
					MessageManager.getMessages());

			// no need to continue:
			return;
		}
	}

	/**
	 * Get the bigDecimalExample.
	 * 
	 * @return The bigDecimalExample
	 */
	public BigDecimal getBigDecimalExample() {
		return bigDecimalExample;
	}

	/**
	 * Set the bigDecimalExample.
	 * 
	 * @param The
	 *            bigDecimalExample
	 */
	public void setBigDecimalExample(BigDecimal bigDecimalExample) {
		this.bigDecimalExample = bigDecimalExample;
	}

	/**
	 * Validates bigDecimalExample.
	 */
	protected void validateBigDecimalExample() {
		BigDecimal value = getBigDecimalExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
		// check min value
		Validations.validateMinValue(value, BIGDECIMALEXAMPLE_MIN_VALUE, "ValidationExample.bigDecimalExample",
				"bigDecimalExample", MessageManager.getMessages());
		// check max value
		Validations.validateMaxValue(value, BIGDECIMALEXAMPLE_MAX_VALUE, "ValidationExample.bigDecimalExample",
				"bigDecimalExample", MessageManager.getMessages());
	}

	/**
	 * Get the characterExample.
	 * 
	 * @return The characterExample
	 */
	public String getCharacterExample() {
		return characterExample;
	}

	/**
	 * Set the characterExample.
	 * 
	 * @param The
	 *            characterExample
	 */
	public void setCharacterExample(String characterExample) {
		characterExample = StringUtils.trimToNull(characterExample);
		this.characterExample = characterExample;
	}

	/**
	 * Validates characterExample.
	 */
	protected void validateCharacterExample() {
		String value = getCharacterExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the stringExample.
	 * 
	 * @return The stringExample
	 */
	public String getStringExample() {
		return stringExample;
	}

	/**
	 * Set the stringExample.
	 * 
	 * @param The
	 *            stringExample
	 */
	public void setStringExample(String stringExample) {
		stringExample = StringUtils.trimToNull(stringExample);
		this.stringExample = stringExample;
	}

	/**
	 * Validates stringExample.
	 */
	protected void validateStringExample() {
		String value = getStringExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
		// check min length
		Validations.validateMinLength(value, STRINGEXAMPLE_MIN_LENGTH, "ValidationExample.stringExample",
				"stringExample", MessageManager.getMessages());
		// check max length
		Validations.validateMaxLength(value, STRINGEXAMPLE_MAX_LENGTH, "ValidationExample.stringExample",
				"stringExample", MessageManager.getMessages());
	}

	/**
	 * Get the dateExample.
	 * 
	 * @return The dateExample
	 */
	public Date getDateExample() {
		return dateExample;
	}

	/**
	 * Set the dateExample.
	 * 
	 * @param The
	 *            dateExample
	 */
	public void setDateExample(Date dateExample) {
		this.dateExample = dateExample;
	}

	/**
	 * Validates dateExample.
	 */
	protected void validateDateExample() {
		Date value = getDateExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the longExample.
	 * 
	 * @return The longExample
	 */
	public Long getLongExample() {
		return longExample;
	}

	/**
	 * Set the longExample.
	 * 
	 * @param The
	 *            longExample
	 */
	public void setLongExample(Long longExample) {
		this.longExample = longExample;
	}

	/**
	 * Validates longExample.
	 */
	protected void validateLongExample() {
		Long value = getLongExample();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
		// check min value
		Validations.validateMinValue(value, LONGEXAMPLE_MIN_VALUE, "ValidationExample.longExample", "longExample",
				MessageManager.getMessages());
		// check max value
		Validations.validateMaxValue(value, LONGEXAMPLE_MAX_VALUE, "ValidationExample.longExample", "longExample",
				MessageManager.getMessages());
	}

	/**
	 * Set the validationExampleChild relation.
	 * 
	 * @param Set
	 *            - The validationExampleChilds
	 */
	public void setValidationExampleChilds(Set<ValidationExampleChildBO> validationExampleChilds) {
		this.validationExampleChilds = validationExampleChilds;
	}

	/**
	 * Get the validationExampleChild relation.
	 * 
	 * @return Set - The validationExampleChilds
	 */
	public Set<ValidationExampleChildBO> getValidationExampleChilds() {
		if (validationExampleChilds == null) {
			validationExampleChilds = new HashSet();
		}

		return validationExampleChilds;
	}

	/**
	 * Add a validationExampleChild.
	 * 
	 * @param The
	 *            validationExampleChild to add
	 */
	public void addValidationExampleChild(ValidationExampleChildBO validationExampleChild) {
		Set<ValidationExampleChildBO> childSet = getValidationExampleChilds();
		if (childSet == null) {
			childSet = new HashSet();
			setValidationExampleChilds(childSet);
		}

		validationExampleChild.setValidationExample((ValidationExampleBO) this);
		childSet.add(validationExampleChild);
	}

	/**
	 * Remove a validationExampleChild.
	 * 
	 * @param The
	 *            validationExampleChild to remove
	 */
	public ValidationExampleChildBO removeValidationExampleChild(ValidationExampleChildBO validationExampleChild) {
		if (getValidationExampleChilds().remove(validationExampleChild)) {
			validationExampleChild.setValidationExample(null);

		} else {
			getLogger().error(
					"Could not remove validationExampleChild instance with key " + validationExampleChild.getKey());

		}

		return validationExampleChild;
	}

	/**
	 * Get the primary key for the ValidationExample.
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationExamplePK();
	}

	/**
	 * Get the primary key for the ValidationExample.
	 * 
	 */
	public ValidationExamplePK getValidationExamplePK() {
		return new ValidationExamplePK(id);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationExamplePK((ValidationExamplePK) pk);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setValidationExamplePK(ValidationExamplePK pk) {

		this.id = pk.getId();
	}

	/**
	 * Executes all field-level validations.
	 */
	@Override
	protected void fieldValidation() {
		validateIntegerExample();
		validateBigDecimalExampleWithScale();
		validateRequiredField();
		validateBigDecimalExample();
		validateCharacterExample();
		validateStringExample();
		validateDateExample();
		validateLongExample();

		// call field validation on children:
		Set<ValidationExampleChildBO> validationExampleChildSet = getValidationExampleChilds();
		if (validationExampleChildSet != null && !validationExampleChildSet.isEmpty()) {
			for (ValidationExampleChildBO child : validationExampleChildSet) {
				child.fieldValidation();
			}
		}

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
		// call complex validation on children:
		Set<ValidationExampleChildBO> validationExampleChildSet = getValidationExampleChilds();
		if (validationExampleChildSet != null && !validationExampleChildSet.isEmpty()) {
			for (ValidationExampleChildBO child : validationExampleChildSet) {
				child.validate();
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ValidationExampleDao getDao() {
		return ValidationExampleBO.getDefaultDao();
	}

	protected static ValidationExampleDao getDefaultDao() {
		return DAO;
	}

	/**
	 * Find the ValidationExample by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the ValidationExample
	 * @return ValidationExample The retrieved ValidationExample
	 */
	public static ValidationExampleBO findByPrimaryKey(ValidationExamplePK pk) {
		return ValidationExampleBO.getDefaultDao().findByPrimaryKey(pk);
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationExampleBO bo = (ValidationExampleBO) o;
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
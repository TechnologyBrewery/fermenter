package org.bitbucket.fermenter.test.domain.transfer;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.transfer.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

/**
 * Transfer object for the ValidationExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public class ValidationExample extends BaseTO {

	private static final long serialVersionUID = 452001985224025088L;

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	public static final String ENTITY = "org.bitbucket.fermenter.test.domain.ValidationExample";

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
	private Set<ValidationExampleChild> validationExampleChilds;

	/**
	 * Answer the logical entity name
	 * 
	 * @return String - The logical entity name
	 */
	public String getEntityName() {
		return ENTITY;
	}

	/**
	 * Get the validation example id
	 * 
	 * @return The validation example id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the validation example id
	 * 
	 * @param string
	 *            The validation example id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the optimistic locking value
	 * 
	 * @return The optimistic locking value
	 */
	public Integer getOplock() {
		return oplock;
	}

	/**
	 * Set the optimistic locking value
	 * 
	 * @param The
	 *            optimistic locking value
	 */
	private void setOplock(Integer oplock) {
		this.oplock = oplock;
	}

	/**
	 * Get the integerExample
	 * 
	 * @return The integerExample
	 */
	public Integer getIntegerExample() {
		return integerExample;
	}

	/**
	 * Set the integerExample
	 * 
	 * @param integer
	 *            The integerExample
	 */
	public void setIntegerExample(Integer integerExample) {
		this.integerExample = integerExample;
	}

	/**
	 * Get the bigDecimalExampleWithScale
	 * 
	 * @return The bigDecimalExampleWithScale
	 */
	public BigDecimal getBigDecimalExampleWithScale() {
		return bigDecimalExampleWithScale;
	}

	/**
	 * Set the bigDecimalExampleWithScale
	 * 
	 * @param big_decimal
	 *            The bigDecimalExampleWithScale
	 */
	public void setBigDecimalExampleWithScale(BigDecimal bigDecimalExampleWithScale) {
		this.bigDecimalExampleWithScale = bigDecimalExampleWithScale;
	}

	/**
	 * Get the required field
	 * 
	 * @return The required field
	 */
	public String getRequiredField() {
		return requiredField;
	}

	/**
	 * Set the required field
	 * 
	 * @param string
	 *            The required field
	 */
	public void setRequiredField(String requiredField) {
		requiredField = StringUtils.trimToNull(requiredField);
		this.requiredField = requiredField;
	}

	/**
	 * Get the bigDecimalExample
	 * 
	 * @return The bigDecimalExample
	 */
	public BigDecimal getBigDecimalExample() {
		return bigDecimalExample;
	}

	/**
	 * Set the bigDecimalExample
	 * 
	 * @param big_decimal
	 *            The bigDecimalExample
	 */
	public void setBigDecimalExample(BigDecimal bigDecimalExample) {
		this.bigDecimalExample = bigDecimalExample;
	}

	/**
	 * Get the characterExample
	 * 
	 * @return The characterExample
	 */
	public String getCharacterExample() {
		return characterExample;
	}

	/**
	 * Set the characterExample
	 * 
	 * @param character
	 *            The characterExample
	 */
	public void setCharacterExample(String characterExample) {
		characterExample = StringUtils.trimToNull(characterExample);
		this.characterExample = characterExample;
	}

	/**
	 * Get the stringExample
	 * 
	 * @return The stringExample
	 */
	public String getStringExample() {
		return stringExample;
	}

	/**
	 * Set the stringExample
	 * 
	 * @param string
	 *            The stringExample
	 */
	public void setStringExample(String stringExample) {
		stringExample = StringUtils.trimToNull(stringExample);
		this.stringExample = stringExample;
	}

	/**
	 * Get the dateExample
	 * 
	 * @return The dateExample
	 */
	public Date getDateExample() {
		return dateExample;
	}

	/**
	 * Set the dateExample
	 * 
	 * @param date
	 *            The dateExample
	 */
	public void setDateExample(Date dateExample) {
		this.dateExample = dateExample;
	}

	/**
	 * Get the longExample
	 * 
	 * @return The longExample
	 */
	public Long getLongExample() {
		return longExample;
	}

	/**
	 * Set the longExample
	 * 
	 * @param long The longExample
	 */
	public void setLongExample(Long longExample) {
		this.longExample = longExample;
	}

	/**
	 * Set the validationExampleChild relation.
	 * 
	 * @param Set
	 *            - The validationExampleChilds
	 */
	public void setValidationExampleChilds(Set<ValidationExampleChild> validationExampleChilds) {
		this.validationExampleChilds = validationExampleChilds;
	}

	/**
	 * Get the validationExampleChild relation.
	 * 
	 * @return Set - The validationExampleChilds
	 */
	public Set<ValidationExampleChild> getValidationExampleChilds() {
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
	public void addValidationExampleChild(ValidationExampleChild validationExampleChild) {
		if (getValidationExampleChilds().contains(validationExampleChild)) {
			getValidationExampleChilds().remove(validationExampleChild);
		}

		validationExampleChild.setValidationExample(this);
		getValidationExampleChilds().add(validationExampleChild);
	}

	/**
	 * Remove a validationExampleChild.
	 * 
	 * @param The
	 *            validationExampleChild to remove
	 */
	public ValidationExampleChild removeValidationExampleChild(ValidationExampleChild validationExampleChild) {
		if (getValidationExampleChilds().remove(validationExampleChild)) {
			validationExampleChild.setValidationExample(null);
		}

		return validationExampleChild;
	}

	/**
	 * Get the primary key for the ValidationExample
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationExamplePK();
	}

	/**
	 * Get the primary key for the ValidationExample
	 * 
	 */
	public ValidationExamplePK getValidationExamplePK() {
		return new ValidationExamplePK(id);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationExamplePK((ValidationExamplePK) pk);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setValidationExamplePK(ValidationExamplePK pk) {
		this.id = pk.getId();
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationExample to = (ValidationExample) o;
			// Can't be null!
			PrimaryKey thisPk = getKey();
			PrimaryKey thatPk = (to == null) ? null : to.getKey();
			if (thatPk != null && thisPk.getValue() == null && thatPk.getValue() == null) {
				return this.internalTransientID == to.internalTransientID;
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

package org.bitbucket.fermenter.test.domain.transfer;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.transfer.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * Transfer object for the ValidationExampleChild application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public class ValidationExampleChild extends BaseTO {

	private static final long serialVersionUID = 2133069281196050432L;

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	public static final String ENTITY = "org.bitbucket.fermenter.test.domain.ValidationExampleChild";

	private String id;
	private Integer oplock;
	private String requiredField;
	private ValidationExample parentValidationExample;

	/**
	 * Answer the logical entity name
	 * 
	 * @return String - The logical entity name
	 */
	public String getEntityName() {
		return ENTITY;
	}

	/**
	 * Get the one to many child id
	 * 
	 * @return The one to many child id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the one to many child id
	 * 
	 * @param string
	 *            The one to many child id
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
	 * Get the child required field
	 * 
	 * @return The child required field
	 */
	public String getRequiredField() {
		return requiredField;
	}

	/**
	 * Set the child required field
	 * 
	 * @param string
	 *            The child required field
	 */
	public void setRequiredField(String requiredField) {
		requiredField = StringUtils.trimToNull(requiredField);
		this.requiredField = requiredField;
	}

	/**
	 * Set the parent ValidationExample onto this instance
	 * 
	 * @param parent
	 *            The parent instance to set
	 */
	public void setValidationExample(ValidationExample parent) {
		parentValidationExample = parent;
	}

	/**
	 * Returns the parent of the type for this instance
	 * 
	 * @return The parent instance or null if no parent of this type exists
	 */
	public ValidationExample getValidationExample() {
		return parentValidationExample;
	}

	/**
	 * Get the primary key for the ValidationExampleChild
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationExampleChildPK();
	}

	/**
	 * Get the primary key for the ValidationExampleChild
	 * 
	 */
	public ValidationExampleChildPK getValidationExampleChildPK() {
		return new ValidationExampleChildPK(id);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationExampleChildPK((ValidationExampleChildPK) pk);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setValidationExampleChildPK(ValidationExampleChildPK pk) {
		this.id = pk.getId();
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationExampleChild to = (ValidationExampleChild) o;
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

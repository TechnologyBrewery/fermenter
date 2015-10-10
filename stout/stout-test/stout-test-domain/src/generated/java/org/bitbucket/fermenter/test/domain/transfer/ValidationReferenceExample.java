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
 * Transfer object for the ValidationReferenceExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public class ValidationReferenceExample extends BaseTO {

	private static final long serialVersionUID = 3631124705720929280L;

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	public static final String ENTITY = "org.bitbucket.fermenter.test.domain.ValidationReferenceExample";

	private String id;
	private Integer oplock;
	private String someDataField;
	private ValidationReferencedObject requiredReference;

	/**
	 * Answer the logical entity name
	 * 
	 * @return String - The logical entity name
	 */
	public String getEntityName() {
		return ENTITY;
	}

	/**
	 * Get the validation reference example id
	 * 
	 * @return The validation reference example id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the validation reference example id
	 * 
	 * @param string
	 *            The validation reference example id
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
	 * Get the some data field
	 * 
	 * @return The some data field
	 */
	public String getSomeDataField() {
		return someDataField;
	}

	/**
	 * Set the some data field
	 * 
	 * @param string
	 *            The some data field
	 */
	public void setSomeDataField(String someDataField) {
		someDataField = StringUtils.trimToNull(someDataField);
		this.someDataField = someDataField;
	}

	/**
	 * Get the RequiredReference reference
	 * 
	 * @return ValidationReferencedObject - The RequiredReference
	 */
	public ValidationReferencedObject getRequiredReference() {
		return requiredReference;
	}

	/**
	 * Set the RequiredReference reference
	 * 
	 * @param The
	 *            RequiredReference
	 */
	public void setRequiredReference(ValidationReferencedObject requiredReference) {
		this.requiredReference = requiredReference;
	}

	/**
	 * Get the primary key for the ValidationReferenceExample
	 * 
	 */
	public PrimaryKey getKey() {
		return getValidationReferenceExamplePK();
	}

	/**
	 * Get the primary key for the ValidationReferenceExample
	 * 
	 */
	public ValidationReferenceExamplePK getValidationReferenceExamplePK() {
		return new ValidationReferenceExamplePK(id);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setValidationReferenceExamplePK((ValidationReferenceExamplePK) pk);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setValidationReferenceExamplePK(ValidationReferenceExamplePK pk) {
		this.id = pk.getId();
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			ValidationReferenceExample to = (ValidationReferenceExample) o;
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

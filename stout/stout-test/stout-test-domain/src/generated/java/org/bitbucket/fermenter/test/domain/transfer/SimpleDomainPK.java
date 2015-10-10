package org.bitbucket.fermenter.test.domain.transfer;

import org.bitbucket.fermenter.transfer.PrimaryKey;
import java.io.Serializable;

/**
 * Primary Key for the SimpleDomain application entity
 * 
 * Generated Code - DO NOT MODIFY
 */

public class SimpleDomainPK implements PrimaryKey {

	private String id;

	/**
	 * @deprecated Use specialized constructors instead. Will be removed as of release 0.9.3
	 */
	public SimpleDomainPK() {
	}

	/**
	 * Answer the type of entity for this PK
	 * 
	 * @return String The logical entity name
	 */
	public String getEntityName() {
		return SimpleDomain.ENTITY;
	}

	public SimpleDomainPK(String id) {
		this.id = id;
	}

	public SimpleDomainPK(SimpleDomain entity) {
		this.id = entity.getId();
	}

	/**
	 * Get the id
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id
	 * 
	 * @param string
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the string representation of this pk
	 * 
	 * @return String The PK string
	 */
	public String toString() {
		String s = new String();
		s = s + id.toString();
		return s;
	}

	/**
	 * Get the value. Used by the framework for persistence ops
	 * 
	 */
	public Serializable getValue() {
		return id;
	}

	/**
	 * Override <tt>hashCode()</tt> so that this class can be seamlessly used in hash-based collections.
	 * 
	 * @param o
	 *            The <tt>Object</tt> to which this instance should be compared
	 * @return <tt>true</tt> if equals, <tt>false</tt> otherwise
	 */
	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			SimpleDomainPK entity = (SimpleDomainPK) o;
			String entityId = (entity != null) ? entity.getId() : null;
			if (id == null && entityId == null) {
				return super.equals(o);
			}
			if (id == entityId || (id != null && id.equals(entityId))) {
				areEqual = true;
			}

		} catch (ClassCastException cce) {
			areEqual = false;
		}

		return areEqual;
	}

	/**
	 * Generate a unique hash. NOTE: If you put this object into a hash-based collection, you must remove it if you
	 * decide to update the fields from which the hashCode is derived. This is because the hashCode is derived from the
	 * superclass of this instance if no key fields have been defined.
	 */
	public int hashCode() {
		int hashCode = 0;
		hashCode = (id != null) ? id.hashCode() : super.hashCode();
		return hashCode;
	}

}
package org.bitbucket.fermenter.test.domain.transfer;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.test.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.transfer.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * Transfer object for the SimpleDomain application entity. A simple domain with one (generated) id field, fields of
 * different types, and two sample queries.
 * 
 * Generated Code - DO NOT MODIFY
 */
public class SimpleDomain extends BaseTO {

	private static final long serialVersionUID = 261893978539669504L;

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	public static final String ENTITY = "org.bitbucket.fermenter.test.domain.SimpleDomain";

	private String id;
	private Integer oplock;
	private String name;
	private Long theLong1;
	private BigDecimal bigDecimalValue;
	private String type;
	private SimpleDomainEnumeration anEnumeratedValue;
	private Date theDate1;

	/**
	 * Answer the logical entity name
	 * 
	 * @return String - The logical entity name
	 */
	public String getEntityName() {
		return ENTITY;
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
	 * Gets: The friendly domain moniker
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets: The friendly domain moniker
	 * 
	 * @param string
	 *            The name
	 */
	public void setName(String name) {
		name = StringUtils.trimToNull(name);
		this.name = name;
	}

	/**
	 * Gets: A relevant number for this domain
	 * 
	 * @return The theLong1
	 */
	public Long getTheLong1() {
		return theLong1;
	}

	/**
	 * Sets: A relevant number for this domain
	 * 
	 * @param long The theLong1
	 */
	public void setTheLong1(Long theLong1) {
		this.theLong1 = theLong1;
	}

	/**
	 * Get the bigDecimalValue
	 * 
	 * @return The bigDecimalValue
	 */
	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	/**
	 * Set the bigDecimalValue
	 * 
	 * @param big_decimal
	 *            The bigDecimalValue
	 */
	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}

	/**
	 * Gets: A simple domain classification
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets: A simple domain classification
	 * 
	 * @param string
	 *            The type
	 */
	public void setType(String type) {
		type = StringUtils.trimToNull(type);
		this.type = type;
	}

	/**
	 * Get the anEnumeratedValue
	 * 
	 * @return The anEnumeratedValue
	 */
	public SimpleDomainEnumeration getAnEnumeratedValue() {
		return anEnumeratedValue;
	}

	/**
	 * Set the anEnumeratedValue
	 * 
	 * @param SimpleDomainEnumeration
	 *            The anEnumeratedValue
	 */
	public void setAnEnumeratedValue(SimpleDomainEnumeration anEnumeratedValue) {
		this.anEnumeratedValue = anEnumeratedValue;
	}

	/**
	 * Gets: An important date for this domain
	 * 
	 * @return The theDate1
	 */
	public Date getTheDate1() {
		return theDate1;
	}

	/**
	 * Sets: An important date for this domain
	 * 
	 * @param date
	 *            The theDate1
	 */
	public void setTheDate1(Date theDate1) {
		this.theDate1 = theDate1;
	}

	/**
	 * Get the primary key for the SimpleDomain
	 * 
	 */
	public PrimaryKey getKey() {
		return getSimpleDomainPK();
	}

	/**
	 * Get the primary key for the SimpleDomain
	 * 
	 */
	public SimpleDomainPK getSimpleDomainPK() {
		return new SimpleDomainPK(id);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setSimpleDomainPK((SimpleDomainPK) pk);
	}

	/**
	 * Set the key fields from the primary key
	 * 
	 */
	public void setSimpleDomainPK(SimpleDomainPK pk) {
		this.id = pk.getId();
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			SimpleDomain to = (SimpleDomain) o;
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

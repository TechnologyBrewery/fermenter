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

import java.util.List;
import java.lang.String;
import java.lang.String;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.test.domain.enumeration.SimpleDomainEnumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

/**
 * Business object for the SimpleDomain application entity. A simple domain with one (generated) id field, fields of
 * different types, and two sample queries.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class SimpleDomainBaseBO extends BaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainBO.class);

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null
	 * {@link PrimaryKey} value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	private static final SimpleDomainDao DAO = DaoFactory.createSimpleDomainDao();

	private String id;
	private Integer oplock;
	private String name;
	private Long theLong1;
	private BigDecimal bigDecimalValue;
	private String type;
	private SimpleDomainEnumeration anEnumeratedValue;
	private Date theDate1;

	/**
	 * {@inheritDoc}
	 */
	protected Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Get the values for this SimpleDomain entity.
	 */
	public SimpleDomain getSimpleDomainValues() {
		return SimpleDomainAssembler.createSimpleDomain((SimpleDomainBO) this);
	}

	/**
	 * Set the values for this SimpleDomain entity.
	 */
	public void setSimpleDomainValues(SimpleDomain entity) {
		SimpleDomainAssembler.mergeSimpleDomainBO(entity, (SimpleDomainBO) this);
	}

	/**
	 * Get the id.
	 * 
	 * @return The id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param The
	 *            id
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
	 * Gets: The friendly domain moniker.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets: The friendly domain moniker.
	 * 
	 * @param The
	 *            name
	 */
	public void setName(String name) {
		name = StringUtils.trimToNull(name);
		this.name = name;
	}

	/**
	 * Validates name.
	 */
	protected void validateName() {
		String value = getName();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Gets: A relevant number for this domain.
	 * 
	 * @return The theLong1
	 */
	public Long getTheLong1() {
		return theLong1;
	}

	/**
	 * Sets: A relevant number for this domain.
	 * 
	 * @param The
	 *            theLong1
	 */
	public void setTheLong1(Long theLong1) {
		this.theLong1 = theLong1;
	}

	/**
	 * Validates theLong1.
	 */
	protected void validateTheLong1() {
		Long value = getTheLong1();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the bigDecimalValue.
	 * 
	 * @return The bigDecimalValue
	 */
	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	/**
	 * Set the bigDecimalValue.
	 * 
	 * @param The
	 *            bigDecimalValue
	 */
	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}

	/**
	 * Validates bigDecimalValue.
	 */
	protected void validateBigDecimalValue() {
		BigDecimal value = getBigDecimalValue();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Gets: A simple domain classification.
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets: A simple domain classification.
	 * 
	 * @param The
	 *            type
	 */
	public void setType(String type) {
		type = StringUtils.trimToNull(type);
		this.type = type;
	}

	/**
	 * Validates type.
	 */
	protected void validateType() {
		String value = getType();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the anEnumeratedValue.
	 * 
	 * @return The anEnumeratedValue
	 */
	public SimpleDomainEnumeration getAnEnumeratedValue() {
		return anEnumeratedValue;
	}

	/**
	 * Set the anEnumeratedValue.
	 * 
	 * @param The
	 *            anEnumeratedValue
	 */
	public void setAnEnumeratedValue(SimpleDomainEnumeration anEnumeratedValue) {
		this.anEnumeratedValue = anEnumeratedValue;
	}

	/**
	 * Validates anEnumeratedValue.
	 */
	protected void validateAnEnumeratedValue() {
		SimpleDomainEnumeration value = getAnEnumeratedValue();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Gets: An important date for this domain.
	 * 
	 * @return The theDate1
	 */
	public Date getTheDate1() {
		return theDate1;
	}

	/**
	 * Sets: An important date for this domain.
	 * 
	 * @param The
	 *            theDate1
	 */
	public void setTheDate1(Date theDate1) {
		this.theDate1 = theDate1;
	}

	/**
	 * Validates theDate1.
	 */
	protected void validateTheDate1() {
		Date value = getTheDate1();
		// check for a value to validate:
		if (value == null) {
			// nothing to validate
			return;
		}
	}

	/**
	 * Get the primary key for the SimpleDomain.
	 * 
	 */
	public PrimaryKey getKey() {
		return getSimpleDomainPK();
	}

	/**
	 * Get the primary key for the SimpleDomain.
	 * 
	 */
	public SimpleDomainPK getSimpleDomainPK() {
		return new SimpleDomainPK(id);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setKey(PrimaryKey pk) {
		setSimpleDomainPK((SimpleDomainPK) pk);
	}

	/**
	 * Set the key fields from the primary key.
	 * 
	 */
	public void setSimpleDomainPK(SimpleDomainPK pk) {

		this.id = pk.getId();
	}

	/**
	 * Executes all field-level validations.
	 */
	@Override
	protected void fieldValidation() {
		validateName();
		validateTheLong1();
		validateBigDecimalValue();
		validateType();
		validateAnEnumeratedValue();
		validateTheDate1();

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
	protected SimpleDomainDao getDao() {
		return SimpleDomainBO.getDefaultDao();
	}

	protected static SimpleDomainDao getDefaultDao() {
		return DAO;
	}

	/**
	 * Find the SimpleDomain by primary key fields.
	 * 
	 * @param pk
	 *            The primary key for the SimpleDomain
	 * @return SimpleDomain The retrieved SimpleDomain
	 */
	public static SimpleDomainBO findByPrimaryKey(SimpleDomainPK pk) {
		return SimpleDomainBO.getDefaultDao().findByPrimaryKey(pk);
	}

	/**
	 * Retrieves all domains, completely unfiltered.
	 * 
	 * @return List of SimpleDomains
	 */
	public static List<SimpleDomainBO> selectAllSimpleDomains() {
		List<SimpleDomainBO> bos = SimpleDomainBO.getDefaultDao().selectAllSimpleDomains();
		return bos;
	}

	/**
	 * Retrieves SimpleDomain objects with the given name.
	 * 
	 * @param name
	 *            The desired name of the SimpleDomain objects to retrieve.
	 * @return List of SimpleDomains
	 */
	public static List<SimpleDomainBO> selectAllSimpleDomainsByName(String name) {
		List<SimpleDomainBO> bos = SimpleDomainBO.getDefaultDao().selectAllSimpleDomainsByName(name);
		return bos;
	}

	/**
	 * Retrieves domains of a given type.
	 * 
	 * @param type
	 *            The class of domains to retrieve.
	 * @return List of SimpleDomains
	 */
	public static List<SimpleDomainBO> selectAllSimpleDomainsByType(String type) {
		List<SimpleDomainBO> bos = SimpleDomainBO.getDefaultDao().selectAllSimpleDomainsByType(type);
		return bos;
	}

	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			SimpleDomainBO bo = (SimpleDomainBO) o;
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
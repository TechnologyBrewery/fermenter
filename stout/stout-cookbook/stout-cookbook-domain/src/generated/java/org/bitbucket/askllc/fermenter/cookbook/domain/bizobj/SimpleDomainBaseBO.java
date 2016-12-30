package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.bitbucket.fermenter.stout.bizobj.BaseSpringBO;
import org.bitbucket.fermenter.stout.bizobj.BusinessObject;
import org.springframework.data.jpa.repository.JpaRepository;

import org.bitbucket.askllc.fermenter.cookbook.domain.persist.SimpleDomainRepository;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;

/**
 * Business object for the SimpleDomain application entity. A simple domain with one (generated) id field, fields of
 * different types, and two sample queries.
 * 
 * Generated Code - DO NOT MODIFY
 */
@MappedSuperclass
public abstract class SimpleDomainBaseBO extends BaseSpringBO<String, SimpleDomainBO, SimpleDomainRepository> implements
		BusinessObject<String, SimpleDomainBO> {

	@Inject
	@Transient
	private SimpleDomainRepository repository;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LONG1")
	private Long theLong1;

	@Column(name = "BIG_DECIMAL_COLUMN")
	private BigDecimal bigDecimalValue;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "SIMPLE_DOMAIN_ENUM")
	private SimpleDomainEnumeration anEnumeratedValue;

	@Column(name = "DATE1")
	private Date theDate1;

	protected SimpleDomainBaseBO() {
		setId(java.util.UUID.randomUUID().toString());
	}

	@Override
	public String getKey() {
		return id;
	}

	@Override
	public void setKey(String id) {
		setId(id);
	}

	public static SimpleDomainBO findByPrimaryKey(String key) {
		return getDefaultRepository().findOne(key);
	}

	@Override
	protected SimpleDomainRepository getRepository() {
		return repository;
	}

	protected static SimpleDomainRepository getDefaultRepository() {
		SimpleDomainBO unusedBizObj = new SimpleDomainBO();
		return unusedBizObj.getRepository();
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
		this.id = id;
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
		this.name = name;
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
		this.type = type;
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
	 * Executes field-level validations on all child relations.
	 */
	@Override
	protected void validateRelations() {
	}

	/**
	 * Executes all complex validation on child relations.
	 */
	@Override
	protected void complexValidationOnRelations() {
	}

}
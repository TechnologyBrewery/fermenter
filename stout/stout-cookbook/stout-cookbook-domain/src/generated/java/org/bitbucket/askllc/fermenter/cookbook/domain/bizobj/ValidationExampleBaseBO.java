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

import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationExampleRepository;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Business object for the ValidationExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
@MappedSuperclass
public abstract class ValidationExampleBaseBO extends
		BaseSpringBO<String, ValidationExampleBO, ValidationExampleRepository> implements
		BusinessObject<String, ValidationExampleBO> {

	@Inject
	@Transient
	private ValidationExampleRepository repository;

	@Id
	@Column(name = "VALIDATION_EXAMPLE_ID")
	private String id;

	@Column(name = "INTEGER_EXAMPLE")
	@Max(12345)
	@Min(-12345)
	private Integer integerExample;

	@Column(name = "BIG_DECIMAL_SCALE")
	private BigDecimal bigDecimalExampleWithScale;

	@Column(name = "REQUIRED_FIELD", nullable = false)
	@NotNull
	private String requiredField;

	@Column(name = "BIG_DECIMAL_EXAMPLE")
	@DecimalMax("123456789.123456789")
	@DecimalMin("-123456789.123456789")
	private BigDecimal bigDecimalExample;

	@Column(name = "CHAR_EXAMPLE")
	private String characterExample;

	@Column(name = "STRING_EXAMPLE")
	@Size(min = 2, max = 20)
	private String stringExample;

	@Column(name = "DATE_EXAMPLE")
	private Date dateExample;

	@Column(name = "LONG_EXAMPLE")
	@Max(123456789)
	@Min(-123456789)
	private Long longExample;

	protected ValidationExampleBaseBO() {
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

	public static ValidationExampleBO findByPrimaryKey(String key) {
		return getDefaultRepository().findOne(key);
	}

	@Override
	protected ValidationExampleRepository getRepository() {
		return repository;
	}

	protected static ValidationExampleRepository getDefaultRepository() {
		ValidationExampleBO unusedBizObj = new ValidationExampleBO();
		return unusedBizObj.getRepository();
	}

	@OneToMany(mappedBy = "parentValidationExample", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ValidationExampleChildBO> validationExampleChilds;

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
		this.id = id;
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
		this.requiredField = requiredField;
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
		this.characterExample = characterExample;
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
		this.stringExample = stringExample;
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
			validationExampleChilds = new HashSet<ValidationExampleChildBO>();
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
			childSet = new HashSet<ValidationExampleChildBO>();
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
	 * Executes field-level validations on all child relations.
	 */
	@Override
	protected void validateRelations() {
		// call field validation on children:
		Set<ValidationExampleChildBO> validationExampleChildSet = getValidationExampleChilds();
		if (validationExampleChildSet != null && !validationExampleChildSet.isEmpty()) {
			for (ValidationExampleChildBO child : validationExampleChildSet) {
				child.validateFields();
			}
		}

	}

	/**
	 * Executes all complex validation on child relations.
	 */
	@Override
	protected void complexValidationOnRelations() {
		// call complex validation on children:
		Set<ValidationExampleChildBO> validationExampleChildSet = getValidationExampleChilds();
		if (validationExampleChildSet != null && !validationExampleChildSet.isEmpty()) {
			for (ValidationExampleChildBO child : validationExampleChildSet) {
				child.complexValidation();
			}
		}

	}

}
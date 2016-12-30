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

import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationReferenceExampleRepository;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Business object for the ValidationReferenceExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
@MappedSuperclass
public abstract class ValidationReferenceExampleBaseBO extends
		BaseSpringBO<String, ValidationReferenceExampleBO, ValidationReferenceExampleRepository> implements
		BusinessObject<String, ValidationReferenceExampleBO> {

	@Inject
	@Transient
	private ValidationReferenceExampleRepository repository;

	@Id
	@Column(name = "VALIDATION_REF_EXAMPLE_ID")
	private String id;

	@Column(name = "SOME_DATA_FIELD")
	private String someDataField;

	protected ValidationReferenceExampleBaseBO() {
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

	public static ValidationReferenceExampleBO findByPrimaryKey(String key) {
		return getDefaultRepository().findOne(key);
	}

	@Override
	protected ValidationReferenceExampleRepository getRepository() {
		return repository;
	}

	protected static ValidationReferenceExampleRepository getDefaultRepository() {
		ValidationReferenceExampleBO unusedBizObj = new ValidationReferenceExampleBO();
		return unusedBizObj.getRepository();
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "VALIDATION_REF_OBJECT_ID")
	@NotNull
	private ValidationReferencedObjectBO requiredReference;

	/**
	 * Get the validation reference example id.
	 * 
	 * @return The validation reference example id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the validation reference example id.
	 * 
	 * @param The
	 *            validation reference example id
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the some data field.
	 * 
	 * @return The some data field
	 */
	public String getSomeDataField() {
		return someDataField;
	}

	/**
	 * Set the some data field.
	 * 
	 * @param The
	 *            some data field
	 */
	public void setSomeDataField(String someDataField) {
		this.someDataField = someDataField;
	}

	/**
	 * Get the RequiredReference reference.
	 * 
	 * @return ValidationReferencedObjectBO - The RequiredReference
	 */
	public ValidationReferencedObjectBO getRequiredReference() {
		return requiredReference;
	}

	/**
	 * Set the RequiredReference reference.
	 * 
	 * @param The
	 *            RequiredReference
	 */
	public void setRequiredReference(ValidationReferencedObjectBO requiredReference) {
		this.requiredReference = requiredReference;
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
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

import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationReferencedObjectRepository;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Business object for the ValidationReferencedObject application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
@MappedSuperclass
public abstract class ValidationReferencedObjectBaseBO extends
		BaseSpringBO<String, ValidationReferencedObjectBO, ValidationReferencedObjectRepository> implements
		BusinessObject<String, ValidationReferencedObjectBO> {

	@Inject
	@Transient
	private ValidationReferencedObjectRepository repository;

	@Id
	@Column(name = "VALIDATION_REF_OBJECT_ID")
	private String id;

	@Column(name = "SOME_DATA_FIELD")
	private String someDataField;

	protected ValidationReferencedObjectBaseBO() {
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

	public static ValidationReferencedObjectBO findByPrimaryKey(String key) {
		return getDefaultRepository().findOne(key);
	}

	@Override
	protected ValidationReferencedObjectRepository getRepository() {
		return repository;
	}

	protected static ValidationReferencedObjectRepository getDefaultRepository() {
		ValidationReferencedObjectBO unusedBizObj = new ValidationReferencedObjectBO();
		return unusedBizObj.getRepository();
	}

	/**
	 * Get the validation reference object id.
	 * 
	 * @return The validation reference object id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the validation reference object id.
	 * 
	 * @param The
	 *            validation reference object id
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
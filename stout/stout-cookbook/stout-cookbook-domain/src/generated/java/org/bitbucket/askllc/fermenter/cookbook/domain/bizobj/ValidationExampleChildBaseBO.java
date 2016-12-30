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

import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationExampleChildRepository;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Business object for the ValidationExampleChild application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
@MappedSuperclass
public abstract class ValidationExampleChildBaseBO extends
		BaseSpringBO<String, ValidationExampleChildBO, ValidationExampleChildRepository> implements
		BusinessObject<String, ValidationExampleChildBO> {

	@Inject
	@Transient
	private ValidationExampleChildRepository repository;

	@Id
	@Column(name = "VALIDATION_EXAMPLE_CHILD_ID")
	private String id;

	@Column(name = "STRING_EXAMPLE", nullable = false)
	@NotNull
	private String requiredField;

	protected ValidationExampleChildBaseBO() {
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

	public static ValidationExampleChildBO findByPrimaryKey(String key) {
		return getDefaultRepository().findOne(key);
	}

	@Override
	protected ValidationExampleChildRepository getRepository() {
		return repository;
	}

	protected static ValidationExampleChildRepository getDefaultRepository() {
		ValidationExampleChildBO unusedBizObj = new ValidationExampleChildBO();
		return unusedBizObj.getRepository();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALIDATION_EXAMPLE_ID", nullable = false)
	private ValidationExampleBO parentValidationExample;

	/**
	 * Get the one to many child id.
	 * 
	 * @return The one to many child id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Set the one to many child id.
	 * 
	 * @param The
	 *            one to many child id
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the child required field.
	 * 
	 * @return The child required field
	 */
	public String getRequiredField() {
		return requiredField;
	}

	/**
	 * Set the child required field.
	 * 
	 * @param The
	 *            child required field
	 */
	public void setRequiredField(String requiredField) {
		this.requiredField = requiredField;
	}

	/**
	 * Set the parent ValidationExample onto this instance.
	 * 
	 * @param parent
	 *            The parent instance to set
	 */
	public void setValidationExample(ValidationExampleBO parent) {
		parentValidationExample = parent;
	}

	/**
	 * Returns the parent of the type for this instance.
	 * 
	 * @return The parent instance or null if no parent of this type exists
	 */
	public ValidationExampleBO getValidationExample() {
		return parentValidationExample;
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
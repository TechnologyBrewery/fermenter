package org.bitbucket.fermenter.mda.metamodel.element;

import java.text.Format;
import java.util.Collection;

import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a enumeration of declared constants.
 */
@JsonPropertyOrder({ "package", "name" })
public class ValidationElement extends NamespacedMetamodelElement implements Validation {

	@JsonInclude(Include.NON_NULL)
	private String documentation;

	@JsonInclude(Include.NON_NULL)
	private Integer maxLength;

	@JsonInclude(Include.NON_NULL)
	private Integer minLength;

	/**
	 * Applies to both integers and floating point values, so use a String to store.
	 */
	@JsonInclude(Include.NON_NULL)
	private String maxValue;

	/**
	 * Applies to both integers and floating point values, so use a String to store.
	 */
	@JsonInclude(Include.NON_NULL)
	private String minValue;

	@JsonInclude(Include.NON_NULL)
	private Integer scale;

	@JsonInclude(Include.NON_NULL)
	private Collection<String> formats;

	@JsonIgnore
	private DefaultModelInstanceRepository repository = ModelInstanceRepositoryManager
			.getMetamodelRepository(DefaultModelInstanceRepository.class);

	/**
	 * Override to make optional (for base types) and not write if null.
	 * 
	 * {@inheritDoc}
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = PACKAGE, required = false)
	@Override
	public String getPackage() {
		return super.getPackage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getMaxLength() {
		if (maxLength == null && isEnumerationType()) {
			Enumeration enumeration = repository.getEnumeration(getPackage(), getName());
			Integer enumerationMaxLength = enumeration.getMaxLength();
			if (enumerationMaxLength != null) {
				maxLength = enumerationMaxLength;

			}
		}

		return maxLength;

	}

	@Override
	@JsonInclude(Include.NON_NULL)
	public Integer getMinLength() {
		return minLength;
	}

	@Override
	@JsonInclude(Include.NON_NULL)
	public String getMaxValue() {
		return maxValue;
	}

	@Override
	@JsonInclude(Include.NON_NULL)
	public String getMinValue() {
		return minValue;
	}

	@Override
	@JsonInclude(Include.NON_NULL)
	public Integer getScale() {
		return scale;
	}

	@Override
	@JsonInclude(Include.NON_NULL)
	public Collection<String> getFormats() {
		return formats;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() {

	}

	/**
	 * Sets the documentation for this type.
	 * 
	 * @param documentation description of the type
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Sets the maximum length of a string.
	 * 
	 * @param maxLength string max length
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Sets the minimum length of a string.
	 * 
	 * @param minLength string min length
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * Sets the maximum value of a numeric type.
	 * 
	 * @param maxValue numeric max value
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * Sets the minimum value of a numeric type.
	 * 
	 * @param minValue numeric min value
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	/**
	 * Sets scale (number of places to right of decimal point).
	 * 
	 * @param scale allowed places
	 */
	public void setScale(Integer scale) {
		this.scale = scale;
	}

	/**
	 * Format name for regular expression matching.
	 * 
	 * @param formats format name (reference to {@link Format}).
	 */
	public void setFormats(Collection<String> formats) {
		this.formats = formats;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSchemaFileName() {
		return "fermenter-2-entity-schema.json";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("package", getPackage()).add("name", name).toString();
	}

	/**
	 * Determines if any validation constraint information has been configured.
	 * 
	 * @return true if configured
	 */
	public boolean hasValue() {
		return maxLength != null || minLength != null || maxValue != null || minValue != null || scale != null
				|| formats != null || documentation != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonIgnore
	public Boolean isEnumerationType() {
		return repository.getEnumeration(getPackage(), getName()) != null;
	}	

}

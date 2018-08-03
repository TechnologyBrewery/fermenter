package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a field on an entity.
 */
@JsonPropertyOrder({ "package", "name" })
public class FieldElement extends MetamodelElement implements Field {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = NamespacedMetamodelElement.PACKAGE)
    protected String packageName;
    
    @JsonInclude(Include.NON_NULL)
    protected String type;
	
    @JsonInclude(Include.NON_NULL)
    protected String column;

    @JsonInclude(Include.NON_NULL)
    protected Validation validation;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected Boolean required;

    @JsonInclude(Include.NON_NULL)
    protected Generator generator;   
    
    @JsonInclude(Include.NON_NULL)
    protected String defaultValue;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return type;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return packageName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumn() {
        return column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validation getValidation() {
        return validation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonInclude(Include.NON_NULL)
    public Boolean isRequired() {
        return required;
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
    public Generator getGenerator() {
        return generator;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
    	//TODO: validate this refers to a valid type (once type dictionary is present) 
    }

    /**
     * Sets the packageName of the field type.
     * 
     * @param packageName
     *            namespace/package of the element
     */
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }

	/**
	 * Sets the type of the metadata element.
	 * 
	 * @param type type to set
	 */
	public void setType(String type) {
		this.type = type;
	}    
    
    /**
     * Sets the column value.
     * 
     * @param column
     *            column value
     */
    public void setColumn(String column) {
        this.column = column;
    }
    
    /**
     * Sets the field validation constraints.
     * 
     * @param validation
     *            field type
     */
    public void setType(Validation validation) {
        this.validation = validation;
    }    

    /**
     * Sets the documentation value.
     * 
     * @param documentation
     *            documentation text
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
    
    /**
     * Sets the required value.
     * 
     * @param required
     *            required value
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }    

    /**
     * Sets the generator.
     * 
     * @param generator
     *            generator
     */
    public void setGenerator(String generator) {
        this.generator = Generator.fromString(generator);

        if (StringUtils.isNoneBlank(generator) && this.generator == null) {
            messageTracker.addErrorMessage("Could not map generator '" + generator
                    + "' to one of the known generator types! (" + Generator.options() + ") ");
        }
    }    
    
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).toString();
    }
}

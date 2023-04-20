package org.technologybrewery.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a parameter in a service operation.
 */
@JsonPropertyOrder({ "name", "package", "type" })
public class ParameterElement extends NamespacedMetamodelElement implements Parameter {

    @JsonProperty(required = true)
    protected String type;

    @JsonInclude(Include.NON_NULL)
    protected Boolean many;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

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
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     * 
     * Default to false if no specific value is provided
     */
    @Override
    public Boolean isMany() {
        return many;
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
    public void validate() {
        if (StringUtils.isBlank(getName())) {
            messageTracker.addErrorMessage("A parameter has been specified without a name!");
        }

        if (StringUtils.isBlank(type)) {
            messageTracker.addErrorMessage("Parameter " + getName() + " has been specified without a type!");
        }

        // default many when not specified:
        if (many == null) {
            many = Boolean.FALSE;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaFileName() {
        return "fermenter-2-service-schema.json";
    }

    /**
     * Sets if this is a multiple instances of the specified type or just one.
     * 
     * @param many
     *            collection or single instance
     */
    public void setMany(Boolean many) {
        this.many = many;
    }

    /**
     * Sets the type of the parameter.
     * 
     * @param type
     *            parameter type
     */
    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("type", getType()).add(PACKAGE, getPackage()).add(NAME, name).toString();
    }

}

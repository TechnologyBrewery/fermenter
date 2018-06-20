package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a enumeration of declared constants.
 */
@JsonPropertyOrder({ "package", "type" })
public class ReturnElement extends NamespacedMetamodelElement implements Return {

    @JsonProperty(required = true)
    protected String type;

    @JsonInclude(Include.NON_NULL)
    protected Boolean many;

    @JsonInclude(Include.NON_NULL)
    protected String responseEncoding;

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
    public String getResponseEncoding() {
        return responseEncoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // default return type when not specified:
        if (StringUtils.isBlank(getType())) {
            type = "void";
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
     * Sets whether or not the return type is many instances of the type or just one.
     * 
     * @param many
     *            is many
     */
    public void setMany(Boolean many) {
        this.many = many;
    }

    /**
     * Sets the return type.
     * 
     * @param type
     *            return type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the encoding on for the return.
     * 
     * @param responseEncoding
     *            encoding
     */
    public void setResponseEncoding(String responseEncoding) {
        this.responseEncoding = responseEncoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("package", getPackage()).add("type", type).add("many", many)
                .toString();
    }

}

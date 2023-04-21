package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a type in the dictionary.
 */
@JsonPropertyOrder({ "name" })
public class DictionaryTypeElement extends ValidationElement implements DictionaryType {

    /**
     * Formats applicable to the dictionary type.
     */
    @JsonInclude(Include.NON_NULL)
    private List<String> formats = new ArrayList<>();

    /**
     * Indicate the base type of the dictionary type.
     */
    @JsonInclude(Include.NON_NULL)
    private String type;

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
    public List<String> getFormats() {
        return formats;
    }

    /**
     * Sets the base type for the dictionary type.
     * 
     * @param type
     *            base type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the format(s) for the dictionary type.
     * 
     * @param formats
     *            list of formats for the type
     */
    public void setFormats(List<String> formats) {
        this.formats.addAll(formats);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();

        // name of the dictionary type is required
        if (null == getName()) {
            messageTracker.addErrorMessage("Dictionary type name is not specified.");
        }

        // base type of the dictionary type is required
        if (null == getType()) {
            messageTracker.addErrorMessage("Dictionary base type is not specified.");
        }

        // min length is not require but if one is specified, only positive length is allowed
        if (null != getMinLength()) {
            if (getMinLength() < 0) {
                messageTracker.addErrorMessage("Dictionary type min length is less than zero.");
            }
        }

        // max length is not require but if one is specified, it must be non zero and positive
        if (null != getMaxLength()) {
            if (getMaxLength() <= 0) {
                messageTracker.addErrorMessage("Dictionary type max length is less than or equal to zero.");
            }
        }

        // max must be >= min
        if (null != getMinLength() && null != getMaxLength()) {
            if (getMaxLength() < getMinLength()) {
                messageTracker.addErrorMessage("Dictionary type max length is less than min length.");
            }
        }

        // type format must be non-empty
        for (String format : getFormats()) {
            if (StringUtils.isBlank(format)) {
                messageTracker.addErrorMessage("Dictionary type format is empty.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaFileName() {
        return "fermenter-2-type-dictionary-schema.json";
    }

    /**
     * Determines if any validation constraint information has been configured.
     * 
     * @return true if configured
     */
    public boolean hasValue() {
        return this.getMaxLength() != null || this.getMinLength() != null || this.getMaxValue() != null
                || this.getMinValue() != null || this.getScale() != null || this.getDocumentation() != null
                || getType() != null || getFormats() != null;
    }

}

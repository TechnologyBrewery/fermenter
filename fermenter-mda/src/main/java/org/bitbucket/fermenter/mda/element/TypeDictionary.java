package org.bitbucket.fermenter.mda.element;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds type dictionary information to support MDA execution. A dictionary type
 * specifies the name, the type and the description of the type available for
 * Fields
 */
public class TypeDictionary implements ValidatedElement {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String type;

    @JsonProperty(required = false)
    private String description;

    @JsonProperty(required = false)
    private Integer minLength;

    @JsonProperty(required = false)
    private Integer maxLength;

    @JsonProperty(required = false)
    private List<String> format = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer min) {
        this.minLength = min;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer max) {
        this.maxLength = max;
    }

    /**
     * Gets all regex formats for the type
     *
     * @return
     *      a list of regex formats applicable for the type
     */ 
    public List<String> getFormat() {
        return format;
    }

    /**
     * Multiple regex formats are allowed. A single, complete, and valid regex string per item in the list
     *
     * @param formats
     */
    public void setFormat(List<String> formats) {
        this.format.addAll(formats);
    }

    /**
     * {@inheritDoc}
     */
    public String getSchemaFileName() {
        return "fermenter-2-type-dictionary-schema.json";
    }
}
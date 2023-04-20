package org.technologybrewery.fermenter.stout.authz.json;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents the structure of an attribute that will be sourced by Stout.
 */
public class StoutAttribute {

    private String id;
    
    private String category;
    
    private String type;
    
    private boolean required;
    
    private String attributePointClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return StringUtils.trim(category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        if (StringUtils.isBlank(type)) {
            type = "string";
        }
        return StringUtils.trim(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAttributePointClass() {
        return StringUtils.trim(attributePointClass);
    }

    public void setAttributeSourceClass(String attributePointClass) {
        this.attributePointClass = attributePointClass;
    }
    
}

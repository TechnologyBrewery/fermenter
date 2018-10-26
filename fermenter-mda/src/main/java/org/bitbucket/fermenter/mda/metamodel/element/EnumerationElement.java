package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Enum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a enumeration of declared constants.
 */
@JsonPropertyOrder({ "package", "name" })
public class EnumerationElement extends NamespacedMetamodelElement implements Enumeration {

    @JsonProperty(required = true)
    protected List<Enum> enums = new ArrayList<>();
    protected Integer maxLength;
    protected boolean isNamed = true;

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public Integer getMaxLength() {
        if (maxLength == null) {
            List<Enum> enumList = getEnums();
            String name;
            int currentLength;
            int maxiumLength = 0;
            for (Enum e : enumList) {
                name = e.getName();
                currentLength = name.length();
                if (currentLength > maxiumLength) {
                    maxiumLength = currentLength;
                }
            }

            maxLength = maxiumLength;
        }
        return maxLength;
    }

    /**
     * Return the list of constants for this enumeration.
     * 
     * @return constants
     */
    public List<Enum> getEnums() {
        return enums;
    }

    /**
     * Adds a constant to this enumeration.
     * 
     * @param contant
     *            the constant to add
     */
    public void addEnums(EnumElement contant) {
        enums.add(contant);
    }
    
    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isNamed() {
        return isNamed;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isValued() {
        return !isNamed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
    	super.validate();
    	
        if (CollectionUtils.isEmpty(enums)) {
            messageTracker.addWarningMessage("Enumeration " + getName() + " does NOT contain any enum constants!");
        }
        
        for (Enum enumInstance : enums) {
            if (enumInstance.getValue() != null) {
                isNamed = false;
                break;
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaFileName() {
        return "fermenter-2-enumeration-schema.json";
    }

}

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
@JsonPropertyOrder({"package", "name"})
public class EnumerationElement extends NamespacedMetamodelElement implements Enumeration {

    @JsonProperty(required = true)
    protected List<Enum> enums = new ArrayList<>();
    protected Integer maxLength;
    
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
     * @return constants
     */
    public List<Enum> getEnums() {
        return enums;
    }

    /**
     * Adds a constant to this enumeration.
     * @param contant the constant to add
     */
    public void addEnums(EnumElement contant) {
        enums.add(contant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
       if (CollectionUtils.isEmpty(enums)) {
           messageTracker.addWarningMessage("Enumeration " + getName() + " does NOT contain any enum constants!");
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

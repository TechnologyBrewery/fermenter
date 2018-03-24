package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a enumeration of declared constants.
 */
@JsonPropertyOrder({"package", "name"})
public class Enumeration extends NamespacedMetamodelElement {

    @JsonProperty(required = true)
    protected List<Enum> enums = new ArrayList<>();
    
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
    public void addEnums(Enum contant) {
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

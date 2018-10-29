package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a service contains at least one operation.
 */
@JsonPropertyOrder({ "package", "name" })
public class ServiceElement extends NamespacedMetamodelElement implements Service {

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonProperty(required = true)
    protected List<Operation> operations = new ArrayList<>();

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
    public List<Operation> getOperations() {
        return operations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        
        if (CollectionUtils.isEmpty(operations)) {
            messageTracker.addWarningMessage("Service " + getName() + " does NOT contain any operations!");
        }
        
        for (Operation operation : operations) {
            operation.validate();
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
     * Sets the documentation value.
     * 
     * @param documentation
     *            documentation text
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
     * Sets a list of operations.
     * 
     * @param operations
     *            operations
     */
    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    } 

}

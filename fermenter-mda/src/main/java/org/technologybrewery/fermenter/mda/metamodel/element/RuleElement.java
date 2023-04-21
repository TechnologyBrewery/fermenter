package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a rule contains at least one operation.
 */
@JsonPropertyOrder({ "package", "name" })
public class RuleElement extends NamespacedMetamodelElement implements Rule {

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected String defaultStatement;
    
    @JsonInclude(Include.NON_NULL)
    protected String defaultProcessing;

    @JsonInclude(Include.NON_NULL)
    protected String ruleGroup;

    @JsonProperty(required = true)
    protected List<Operation> operations = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultStatement() {
        return defaultStatement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultProcessing() {
        return defaultProcessing;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRuleGroup() {
        return ruleGroup;
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
            messageTracker.addWarningMessage("Rule " + getName() + " does NOT contain any operations!");
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
        System.out.println("fermenter-2-rule-schema.json reqested");
        return "fermenter-2-rule-schema.json";
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

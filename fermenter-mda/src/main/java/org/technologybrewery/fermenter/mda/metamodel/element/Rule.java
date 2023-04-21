package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for a rule that contains one or more operations.
 */
public interface Rule extends NamespacedMetamodel {

    /**
     * Returns rule-level documentation.
     * 
     * @return rule documentation
     */
    String getDocumentation();

    /**
     * Returns rule-level defaultStatement.
     * 
     * @return rule defaultStatement
     */
    String getDefaultStatement();

    /**
     * Returns rule-level defaultProcessing.
     * 
     * @return rule defaultProcessing
     */
    String getDefaultProcessing();

    /**
     * Returns rule-level ruleGroup.
     * 
     * @return rule ruleGroup
     */
    String getRuleGroup();

    /**
     * Returns the operation instances within this rule.
     * 
     * @return operations
     */
    List<Operation> getOperations();

}

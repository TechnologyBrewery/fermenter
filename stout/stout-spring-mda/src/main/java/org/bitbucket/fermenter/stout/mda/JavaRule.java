package org.technologybrewery.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseRuleDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Operation;
import org.technologybrewery.fermenter.mda.metamodel.element.Rule;

/**
 * Decorates a {@link Rule} with Java-specific capabilities.
 */
public class JavaRule extends BaseRuleDecorator implements Rule, JavaPackagedElement {

    protected List<Operation> decoratedOperations;
    protected Set<String> imports;

    /**
     * {@inheritDoc}
     */
    public JavaRule(Rule ruleToDecorate) {
        super(ruleToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    public List<Operation> getOperations() {
        if (decoratedOperations == null) {
            List<Operation> ruleOperations = super.getOperations();
            if (CollectionUtils.isEmpty(ruleOperations)) {
                decoratedOperations = Collections.emptyList();

            } else {
                decoratedOperations = new ArrayList<>();
                for (Operation operation : ruleOperations) {
                    decoratedOperations.add(new JavaOperation(operation));

                }

            }
        }

        return decoratedOperations;
    }

    /**
     * Returns all the imports needed for operations on this rule.
     * 
     * @return operation imports
     */
    public Set<String> getOperationImports() {
        Set<String> importSet = new HashSet<>();

        List<Operation> operations = getOperations();
        for (Operation operation : operations) {
            JavaOperation javaOperation = (JavaOperation) operation;
            importSet.addAll(javaOperation.getImports());
        }

        return importSet;
    }

    /**
     * Returns all imports related to this rule, including those uses on operations.
     * 
     * @return rule imports
     */
    public Set<String> getImports() {
        if (imports == null) {
            imports = new TreeSet<>();
            imports.addAll(getOperationImports());
        }

        return imports;
    }

}

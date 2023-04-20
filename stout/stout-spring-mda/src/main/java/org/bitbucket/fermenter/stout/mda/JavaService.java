package org.technologybrewery.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseServiceDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Operation;
import org.technologybrewery.fermenter.mda.metamodel.element.Service;

/**
 * Decorates a {@link Service} with Java-specific capabilities.
 */
public class JavaService extends BaseServiceDecorator implements Service, JavaPackagedElement {

    protected List<Operation> decoratedOperations;
    protected Set<String> imports;

    /**
     * {@inheritDoc}
     */
    public JavaService(Service serviceToDecorate) {
        super(serviceToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    public List<Operation> getOperations() {
        if (decoratedOperations == null) {
            List<Operation> serviceOperations = super.getOperations();
            if (CollectionUtils.isEmpty(serviceOperations)) {
                decoratedOperations = Collections.emptyList();

            } else {
                decoratedOperations = new ArrayList<>();
                for (Operation operation : serviceOperations) {
                    decoratedOperations.add(new JavaOperation(operation));

                }

            }
        }

        return decoratedOperations;
    }

    /**
     * Returns all the imports needed for operations on this service.
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
     * Returns all imports related to this service, including those uses on operations.
     * 
     * @return service imports
     */
    public Set<String> getImports() {
        if (imports == null) {
            imports = new TreeSet<>();
            imports.addAll(getOperationImports());
        }

        return imports;
    }

}

package org.technologybrewery.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.Operation;
import org.technologybrewery.fermenter.mda.metamodel.element.Parameter;
import org.technologybrewery.fermenter.mda.metamodel.element.Return;

/**
 * Decorates a {@link Operation} with Java-specific capabilities that focus on treating the operation as a *remote* Java
 * class. For instance, one that references TransferObjects instead of BusinessObjects.
 */
public class RemoteJavaOperation extends JavaOperation {

    public RemoteJavaOperation(Operation operationToDecorate) {
        super(operationToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Return getReturn() {
        return new RemoteJavaReturn(super.getReturn());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Parameter> getParameters() {
        if (decoratedParameterList == null) {
            List<Parameter> operationParameterList = super.getParameters();
            if (CollectionUtils.isEmpty(operationParameterList)) {
                decoratedParameterList = Collections.emptyList();

            } else {
                decoratedParameterList = new ArrayList<>(operationParameterList.size());
                for (Parameter p : operationParameterList) {
                    decoratedParameterList.add(new RemoteJavaParameter(p));

                }

            }
        }

        return decoratedParameterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTransactionAttribute() {
        return wrapped.getTransactionAttribute();
    }

    /**
     * All imports for this operation, inclusive of parameters and the return.
     * 
     * @return imports
     */
    @Override
    public Set<String> getImports() {
        Set<String> importSet = new HashSet<>();
        Return returnElement = addGenericImports(importSet);

        if (!isResponseTypeVoid()) {
            String returnImport = JavaElementUtils.getJavaImportByPackageAndType(returnElement.getPackage(),
                    returnElement.getType(), false);
            if (JavaElementUtils.checkImportAgainstDefaults(returnImport)) {
                importSet.add(returnImport);
            }
        }
        return importSet;
    }
}

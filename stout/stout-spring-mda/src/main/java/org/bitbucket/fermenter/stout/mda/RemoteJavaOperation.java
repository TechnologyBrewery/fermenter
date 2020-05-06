package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;
import org.bitbucket.fermenter.mda.metamodel.element.Return;

/**
 * Decorates a {@link Operation} with Java-specific capabilities that focus on treating the operation as a *remote*
 * Java class. For instance, one that references TransferObjects instead of BusinessObjects.
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
                decoratedParameterList = new ArrayList<>((int) (operationParameterList.size()));
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

}

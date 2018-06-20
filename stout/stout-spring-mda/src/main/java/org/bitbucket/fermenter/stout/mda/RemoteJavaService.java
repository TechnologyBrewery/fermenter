package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.Service;

/**
 * Decorates a {@link Service} with Java-specific capabilities that focus on treating the operation as a *remote* Java
 * class. For instance, one that references TransferObjects instead of BusinessObjects.
 */
public class RemoteJavaService extends JavaService {

    private List<Operation> decoratedOperations;

    public RemoteJavaService(Service serviceToDecorate) {
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
                    decoratedOperations.add(new RemoteJavaOperation(operation));

                }

            }
        }

        return decoratedOperations;
    }

}

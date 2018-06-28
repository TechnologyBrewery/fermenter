package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides baseline decorator functionality for {@link Service}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorate does only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorate requires by default.
 */
public class BaseServiceDecorator implements Service {

    protected Service wrapped;

    /**
     * New decorator for {@link Service}.
     * 
     * @param serviceToDecorate
     *            instance to decorate
     */
    public BaseServiceDecorator(Service serviceToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), serviceToDecorate);
        wrapped = serviceToDecorate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return wrapped.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return wrapped.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentation() {
        return wrapped.getDocumentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Operation> getOperations() {
        List<Operation> wrappedOperations = new ArrayList<>();
        for (Operation operation : wrapped.getOperations()) {
            Operation wrappedOperation = new BaseOperationDecorator(operation);
            wrappedOperations.add(wrappedOperation);
        }
        
        return wrappedOperations;
    }

}

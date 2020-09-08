package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides baseline decorator functionality for {@link Operation}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseOperationDecorator implements Operation {

    protected Operation wrapped;

    /**
     * New decorator for {@link Operation}.
     * 
     * @param operationToDecorate
     *            instance to decorate
     */
    public BaseOperationDecorator(Operation operationToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), operationToDecorate);
        wrapped = operationToDecorate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return wrapped.getName();
    }
    
    @Override
    public String getFileName() {
    	return wrapped.getFileName();
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
    public void validate() {
        wrapped.validate();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTransactionAttribute() {
        return wrapped.getTransactionAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Return getReturn() {
        return new BaseReturnDecorator(wrapped.getReturn());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Parameter> getParameters() {
        List<Parameter> wrappedParameters = new ArrayList<>();
        for (Parameter parameter : wrapped.getParameters()) {
            Parameter wrappedParameter = new BaseParameterDecorator(parameter);
            wrappedParameters.add(wrappedParameter);
        }
        
        return wrappedParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isCompressedWithGZip() {
        return wrapped.isCompressedWithGZip() != null && wrapped.isCompressedWithGZip();
    }

}

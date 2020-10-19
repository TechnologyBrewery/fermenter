package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link Parameter}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseParameterDecorator implements Parameter {

    protected Parameter wrapped;

    /**
     * New decorator for {@link Parameter}.
     * 
     * @param parameterToDecorate
     *            instance to decorate
     */
    public BaseParameterDecorator(Parameter parameterToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), parameterToDecorate);
        wrapped = parameterToDecorate;
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
    public String getPackage() {
        return wrapped.getPackage();
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
    public String getType() {
        return wrapped.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isMany() {
        return wrapped.isMany() != null && wrapped.isMany();
    }

}

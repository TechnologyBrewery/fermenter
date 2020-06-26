package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link Parent}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseParentDecorator implements Parent {

    protected Parent wrapped;

    /**
     * New decorator for {@link Parent}.
     * 
     * @param parentToDecorate
     *            instance to decorate
     */
    public BaseParentDecorator(Parent parentToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), parentToDecorate);
        wrapped = parentToDecorate;
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
	public String getFileName() {
		return wrapped.getFileName();
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
	public InheritanceStrategy getInheritanceStrategy() {
		return wrapped.getInheritanceStrategy();
	}    

}

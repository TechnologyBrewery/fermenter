package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link Enum}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorate does only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorate requires by default.
 */
public class BaseEnumDecorator implements Enum {

    protected Enum wrapped;

    /**
     * New decorator for {@link Enum}.
     * 
     * @param enumToDecorate
     *            instance to decorate
     */
    public BaseEnumDecorator(Enum enumToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), enumToDecorate);
        wrapped = enumToDecorate;
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
    public Integer getValue() {
        return wrapped.getValue();
    }    

}

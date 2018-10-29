package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Provides baseline decorator functionality for {@link Enumeration}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorate does only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorate requires by default.
 */
public class BaseEnumertionDecorator implements Enumeration {

    protected Enumeration wrapped;

    /**
     * New decorator for {@link Enumeration}.
     * 
     * @param enumerationToDecorate
     *            instance to decorate
     */
    public BaseEnumertionDecorator(Enumeration enumerationToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), enumerationToDecorate);
        wrapped = enumerationToDecorate;
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
    public List<Enum> getEnums() {
        return wrapped.getEnums();
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
    public Integer getMaxLength() {
        return wrapped.getMaxLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNamed() {
        return wrapped.isNamed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValued() {
        return wrapped.isValued();
    }

}

package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.Collection;

/**
 * Provides baseline decorator functionality for {@link Validation}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseValidationDecorator implements Validation {

    protected Validation wrapped;

    /**
     * New decorator for {@link Validation}.
     * 
     * @param validationToDecorate
     *            instance to decorate
     */
    public BaseValidationDecorator(Validation validationToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), validationToDecorate);
        wrapped = validationToDecorate;
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
    public Boolean isEnumerationType() {
        return wrapped.isEnumerationType();
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
    public Integer getMinLength() {
        return wrapped.getMinLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxValue() {
        return wrapped.getMaxValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinValue() {
        return wrapped.getMinValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getScale() {
        return wrapped.getScale();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getFormats() {
        return wrapped.getFormats();
    }
}

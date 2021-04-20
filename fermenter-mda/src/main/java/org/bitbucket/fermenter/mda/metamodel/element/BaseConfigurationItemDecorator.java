package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link ConfigurationItem}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are needed,
 * not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseConfigurationItemDecorator implements ConfigurationItem {

    protected ConfigurationItem wrapped;

    /**
     * New decorator for {@link ConfigurationItem}.
     * 
     * @param configurationItemToDecorate
     *            instance to decorate
     */
    public BaseConfigurationItemDecorator(ConfigurationItem configurationItemToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), configurationItemToDecorate);
        wrapped = configurationItemToDecorate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return wrapped.getKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return wrapped.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();
    }

}

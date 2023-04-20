package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Provides baseline decorator functionality for {@link Relation}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are needed,
 * not all the pass-through methods that each decorate requires by default.
 */
public class BaseRelationDecorator implements Relation {

    protected Relation wrapped;

    /**
     * New decorator for {@link Relation}.
     * 
     * @param relationToDecorate
     *            instance to decorate
     */
    public BaseRelationDecorator(Relation relationToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), relationToDecorate);
        wrapped = relationToDecorate;
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
    public String getType() {
        return wrapped.getType();
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
    public Multiplicity getMultiplicity() {
        return wrapped.getMultiplicity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetchMode getFetchMode() {
        return wrapped.getFetchMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field getParentIdentifier(String parentEntityName) {
        return wrapped.getParentIdentifier(parentEntityName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();

    }
}

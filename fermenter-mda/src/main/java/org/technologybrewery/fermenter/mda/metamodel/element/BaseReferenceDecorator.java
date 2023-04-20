package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides baseline decorator functionality for {@link Reference}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are needed,
 * not all the pass-through methods that each decorate requires by default.
 */
public class BaseReferenceDecorator implements Reference {

    protected Reference wrapped;

    /**
     * New decorator for {@link Reference}.
     * 
     * @param referenceToDecorate
     *            instance to decorate
     */
    public BaseReferenceDecorator(Reference referenceToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), referenceToDecorate);
        wrapped = referenceToDecorate;
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
    public String getName() {
        return wrapped.getName();
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
    public String getLocalColumn() {
        return wrapped.getLocalColumn();
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
    public Boolean isRequired() {
        return wrapped.isRequired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Field> getForeignKeyFields() {
        List<Field> wrappedFields = new ArrayList<>();
        for (Field foreignKeyField : wrapped.getForeignKeyFields()) {
            Field wrappedForeignKeyField = new BaseFieldDecorator(foreignKeyField);
            wrappedFields.add(wrappedForeignKeyField);
        }

        return wrappedFields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();

    }
}

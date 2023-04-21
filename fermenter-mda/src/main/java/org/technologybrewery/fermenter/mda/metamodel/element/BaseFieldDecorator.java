package org.technologybrewery.fermenter.mda.metamodel.element;

import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

import java.util.Collection;

/**
 * Provides baseline decorator functionality for {@link Field}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are needed,
 * not all the pass-through methods that each decorate requires by default.
 */
public class BaseFieldDecorator implements Field {

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    protected static final Integer DEFAULT_SCALE = 5;

    protected Field wrapped;

    /**
     * New decorator for {@link Field}.
     * 
     * @param fieldToDecorate
     *            instance to decorate
     */
    public BaseFieldDecorator(Field fieldToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), fieldToDecorate);
        wrapped = fieldToDecorate;
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
     * Capitalizes field name.
     * 
     * @return the name of the field, capitalized
     */
    public String getUppercasedName() {
        return getName().toUpperCase();
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
    public String getColumn() {
        return wrapped.getColumn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validation getValidation() {
        Validation validation = wrapped.getValidation() != null ? wrapped.getValidation() : new ValidationElement();
        return new BaseValidationDecorator(validation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isRequired() {
        Boolean wrappedValue = wrapped.isRequired();
        return (wrappedValue != null) ? wrappedValue : Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient() {
        return wrapped.isTransient();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return wrapped.getLabel();
    }

    /**
     * Returns whether or not this field has a label.
     * 
     * @return has a label
     */
    public boolean hasLabel() {
        return (getLabel() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue() {
        return wrapped.getDefaultValue();
    }

    /**
     * Returns whether or not this field has a default value.
     * 
     * @return has a default value
     */
    public boolean hasDefaultValue() {
        return (getDefaultValue() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Generator getGenerator() {
        return wrapped.getGenerator();
    }

    /**
     * Returns whether or not this field has a generator.
     * 
     * @return has a generator
     */
    public boolean hasGenerator() {
        return (getGenerator() != null);
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public Integer getMaxLength() {
        return (getValidation() != null) ? getValidation().getMaxLength() : null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasMaxLength() {
        return getMaxLength() != null ? true : false;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public Integer getMinLength() {
        return (getValidation() != null) ? getValidation().getMinLength() : null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasMinLength() {
        return getMinLength() != null ? true : false;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public String getMaxValue() {
        return (getValidation() != null) ? getValidation().getMaxValue() : null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasMaxValue() {
        return getMaxValue() != null ? true : false;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public String getMinValue() {
        return (getValidation() != null) ? getValidation().getMinValue() : null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasMinValue() {
        return getMinValue() != null ? true : false;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public Integer getScale() {
        return (hasScale()) ? getValidation().getScale() : DEFAULT_SCALE;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasScale() {
        return getValidation() != null && getValidation().getScale() != null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public Collection<String> getFormats() {
        return (hasFormats()) ? getValidation().getFormats() : null;
    }

    // TODO: remove convenience methods once the TypeDictionary is complete
    // (FER-57):
    public boolean hasFormats() {
        return getValidation() != null && getValidation().getFormats() != null;
    }

    /**
     * Returns whether or not this field refers to an entity.
     * 
     * @return is an entity
     */
    public boolean isEntity() {
        return MetamodelType.ENTITY.equals(MetamodelType.getMetamodelType(getPackage(), getType()));
    }

    /**
     * Helper method that returns if this field models a named {@link Enumeration}.
     * 
     * @return if this field is a named {@link Enumeration}.
     */
    public boolean isNamedEnumeration() {
        Enumeration enumeration = getModelInstanceRepository().getEnumeration(getPackage(), getType());
        return enumeration != null && enumeration.isNamed();
    }

    private DefaultModelInstanceRepository getModelInstanceRepository() {
        return ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
    }

}

package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

/**
 * Provides baseline decorator functionality for {@link Entity}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorator only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorator would otherwise need to implement (that add no real value).
 */
public class BaseEntityDecorator implements Entity {

    protected Entity wrapped;

    /**
     * New decorator for {@link Entity}.
     * 
     * @param entityToDecorate
     *            instance to decorate
     */
    public BaseEntityDecorator(Entity entityToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), entityToDecorate);
        wrapped = entityToDecorate;
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
    public String getDocumentation() {
        return wrapped.getDocumentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTable() {
        return wrapped.getTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LockStrategy getLockStrategy() {
        return wrapped.getLockStrategy();
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
    public Boolean isNonPersistentParentEntity() {
        return wrapped.isNonPersistentParentEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isChildOfNonPersistentParentEntity() {
        return wrapped.isChildOfNonPersistentParentEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parent getParent() {
        Parent wrappedParent = null;
        if (wrapped.getParent() != null) {
            wrappedParent = new BaseParentDecorator(wrapped.getParent());
        }
        return wrappedParent;
    }

    /**
     * Does this entity have a parent?
     * 
     * @return true is it has a parent, false if it does not
     */
    public boolean hasParent() {
        return wrapped.getParent() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field getIdentifier() {
        Field wrappedIdentifier = null;
        if (wrapped.getIdentifier() != null) {
            wrappedIdentifier = new BaseFieldDecorator(wrapped.getIdentifier());
        }
        return wrappedIdentifier;
    }
    
    /**
     * Does this entity have an identifier?
     * 
     * @return true is it has an identifier, false if it does not
     */
    public boolean hasIdentifier() {
        return wrapped.getIdentifier() != null;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Field> getFields() {
        List<Field> wrappedFields = new ArrayList<>();
        for (Field field : wrapped.getFields()) {
            Field wrappedField = new BaseFieldDecorator(field);
            wrappedFields.add(wrappedField);
        }

        return wrappedFields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reference> getReferences() {
        List<Reference> wrappedReferences = new ArrayList<>();
        for (Reference reference : wrapped.getReferences()) {
            Reference wrappedReference = new BaseReferenceDecorator(reference);
            wrappedReferences.add(wrappedReference);
        }

        return wrappedReferences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relation> getRelations() {
        List<Relation> wrappedRelations = new ArrayList<>();
        for (Relation relation : wrapped.getRelations()) {
            Relation wrappedRelation = new BaseRelationDecorator(relation);
            wrappedRelations.add(wrappedRelation);
        }

        return wrappedRelations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relation getRelation(String type) {
        Map<String, Relation> relationsByType = new HashMap<>();
        MapUtils.populateMap(relationsByType, getRelations(), Relation::getType);
        return relationsByType.get(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> getInverseRelations() {
        List<Entity> wrappedInverseRelations = new ArrayList<>();
        for (Entity inverseRelation : wrapped.getInverseRelations()) {
            Entity wrappedInverseRelation = new BaseEntityDecorator(inverseRelation);
            wrappedInverseRelations.add(wrappedInverseRelation);
        }

        return wrappedInverseRelations;
    }

}

package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.BaseEntityDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Parent;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;
import org.bitbucket.fermenter.stout.bizobj.BasePersistentSpringBO;

/**
 * An {@link Entity} that has been decorated for easier generation of Java files.
 */
public class JavaEntity extends BaseEntityDecorator implements Entity, JavaNamedElement {

    private Map<String, Field> decoratedFieldMap;
    private Map<String, Relation> decoratedRelationMap;
    private Map<String, Entity> decoratedInverseRelationMap;
    private Map<String, Reference> decoratedReferenceMap;
    private Set<String> imports;

    /**
     * {@inheritDoc}
     */
    public JavaEntity(Entity wrapped) {
        super(wrapped);

        loadFields();
        loadRelations();
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public Field getIdentifier() {
        Field identifier = super.getIdentifier();
        return identifier != null ? new JavaField(identifier) : null;
    }

    /**
     * {@inheritDoc}
     */
    public List<Field> getFields() {
        if (decoratedFieldMap == null) {
            loadFields();
        }

        return new ArrayList<>(decoratedFieldMap.values());
    }

    private void loadFields() {
        List<Field> entityFields = wrapped.getFields();
        decoratedFieldMap = new HashMap<>();
        for (Field f : entityFields) {
            JavaField jField = new JavaField(f);
            decoratedFieldMap.put(f.getName(), jField);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Relation> getRelations() {
        if (decoratedRelationMap == null) {
            loadRelations();
        }
        return new ArrayList<>(decoratedRelationMap.values());
    }

    private void loadRelations() {
        List<Relation> entityRelations = wrapped.getRelations();
        decoratedRelationMap = new HashMap<>();
        for (Relation r : entityRelations) {
            decoratedRelationMap.put(r.getType(), new JavaRelation(r));

        }
    }

    /**
     * {@inheritDoc}
     */
    public Relation getRelation(String type) {
        return decoratedRelationMap.get(type);
    }

    /**
     * {@inheritDoc}
     */
    public List<Reference> getReferences() {
        if (decoratedReferenceMap == null) {
            loadReferences();
        }

        return decoratedReferenceMap.values().stream().collect(Collectors.toList());
    }

    private void loadReferences() {
        List<Reference> entityReferences = wrapped.getReferences();
        decoratedReferenceMap = new HashMap<>();
        for (Reference reference : entityReferences) {
            decoratedReferenceMap.put(reference.getName(), new JavaReference(reference));

        }
    }

    /**
     * {@inheritDoc}
     */
    public Reference getReference(String type) {
        return decoratedReferenceMap.get(type);
    }

    /**
     * Generates the appropriate super class for this entity if this entity is non-transient and not a non-persistent
     * parent entity.
     * 
     * @return
     */
    public String getPersistentEntityParentJavaType() {
        Parent parent = getParent();
        if (parent != null) {
            Entity parentEntity = ModelInstanceRepositoryManager
                    .getMetamodelRepository(DefaultModelInstanceRepository.class).getEntity(parent.getType());
            return parentEntity.getName() + "BO";
        } else {
            return BasePersistentSpringBO.class.getSimpleName();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Entity> getInverseRelations() {
        if (decoratedInverseRelationMap == null) {
            loadInverseRelations();
        }

        return new ArrayList<>(decoratedInverseRelationMap.values());
    }

    private void loadInverseRelations() {
        List<Entity> entityInverseRelationMap = wrapped.getInverseRelations();
        decoratedInverseRelationMap = new HashMap<>();
        for (Entity r : entityInverseRelationMap) {
            decoratedInverseRelationMap.put(r.getName(), new RelatedJavaEntity(r, this));

        }
    }

    /**
     * {@inheritDoc}
     */
    public Relation getInverseRelation(String type) {
        return (Relation) decoratedInverseRelationMap.get(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient() {
        return wrapped.isTransient() != null ? wrapped.isTransient() : Boolean.FALSE;
    }

    /**
     * Returns the full set of imports.
     * 
     * @return all imports
     */
    public Set<String> getImports() {
        if (imports == null) {
            imports = new TreeSet<>();
            imports.addAll(getFieldImports());
            imports.addAll(getReferenceImports());
        }

        return imports;
    }

    /**
     * Returns the full set of imports for fields.
     * 
     * @return field imports
     */
    public Set<String> getFieldImports() {
        return getFieldImports(true);
    }

    /**
     * Returns the full set of imports for fields.
     * 
     * @return field imports
     */
    public Set<String> getIdFieldImports() {
        return getFieldImports(false);
    }

    protected Set<String> getFieldImports(boolean includeNonIdFields) {
        Set<String> importSet = new HashSet<>();

        Map<String, Field> fieldCollection = new HashMap<>();
        Field idField = getIdentifier();
        if (idField != null) {
            // identifier can be null for transient entities
            fieldCollection.put(idField.getName(), new JavaField(idField));
        }
        if (includeNonIdFields) {
            fieldCollection.putAll(decoratedFieldMap);
        }

        JavaField javaField;
        for (Field field : fieldCollection.values()) {
            javaField = (JavaField) field;
            String importValue = javaField.getImport();

            // java.lang is imported by default, so filter them out:
            if (!importValue.startsWith("java.lang.")) {
                importSet.add(javaField.getImport());
            }
        }

        return importSet;
    }

    /**
     * Returns the full set of imports for references.
     * 
     * @return reference imports
     */
    public Set<String> getReferenceImports() {
        Set<String> importSet = new HashSet<>();
        Set<String> fkSet;

        JavaReference javaReference;
        for (Reference reference : getReferences()) {
            javaReference = (JavaReference) reference;
            fkSet = javaReference.getForeignKeyImports();
            if (fkSet != null) {
                importSet.addAll(fkSet);
            }

            if (javaReference.isExternal()) {
                importSet.add(javaReference.getImport());
            }
        }

        return importSet;
    }

    /**
     * Returns the prefixes for all references (e.g., org.blah versus org.blah.Foo).
     * 
     * @return collection of import prefixes
     */
    public Collection<String> getImportPrefixes() {
        Collection<String> prefixes = new TreeSet<>();

        for (Reference reference : getReferences()) {
            JavaReference ref = (JavaReference) reference;
            prefixes.add(ref.getImportPrefix());
        }

        return prefixes;
    }

    /**
     * Returns if any of the {@link JavaField}s that are modeled by this entity are named enumerations.
     * 
     * @return if any of this entity's fields are named enumerations.
     */
    public boolean hasNamedEnumeration() {
        return getFields().stream().anyMatch(field -> ((JavaField) field).isNamedEnumeration());
    }

}

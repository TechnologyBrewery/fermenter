package org.bitbucket.fermenter.stout.mda;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Parent;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;
import org.bitbucket.fermenter.stout.bizobj.BasePersistentSpringBO;

/**
 * An {@link Entity} that has been decorated for easier generation of Java files.
 */
public class JavaEntity implements Entity {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(JavaEntity.class);

    private Entity entity;
    private Map<String, Field> decoratedFieldMap;
    private Map<String, Relation> decoratedRelationMap;
    private Map<String, Entity> decoratedInverseRelationMap;
    private Map<String, Reference> decoratedReferenceMap;
    private Set<String> imports;

    /**
     * Create a new instance of {@link Entity} with the correct functionality set to generate Java code
     * 
     * @param entityToDecorate
     *            The {@link Entity} to decorate
     */
    public JavaEntity(Entity entityToDecorate) {
        if (entityToDecorate == null) {
            throw new IllegalArgumentException("JavaEntity must be instatiated with a non-null entity!");
        }
        entity = entityToDecorate;
        loadFields();
        loadRelations();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return entity.getName();
    }

    @Override
    public String getPackage() {
        return entity.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    public String getDocumentation() {
        return entity.getDocumentation();
    }

    /**
     * {@inheritDoc}
     */
    public String getTable() {
        return entity.getTable();
    }

    /**
     * {@inheritDoc}
     */
    public List<Field> getFields() {
        if (decoratedFieldMap == null) {
            loadFields();
        }
        return decoratedFieldMap.values().stream().collect(Collectors.toList());
    }

    private void loadFields() {

        List<Field> entityFields = entity.getFields();
        if ((entityFields == null) || (entityFields.isEmpty())) {
            decoratedFieldMap = Collections.<String, Field> emptyMap();

        } else {
            decoratedFieldMap = new HashMap<>((int) (entityFields.size() * 1.25));
            for (Field f : entityFields) {
                JavaField jField = new JavaField(f);
                decoratedFieldMap.put(f.getName(), jField);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    public List<Relation> getRelations() {
        if (decoratedRelationMap == null) {
            loadRelations();
        }
        return decoratedRelationMap.values().stream().collect(Collectors.toList());
    }

    private void loadRelations() {
        List<Relation> entityRelations = entity.getRelations();
        if ((entityRelations == null) || (entityRelations.isEmpty())) {
            decoratedRelationMap = Collections.<String, Relation> emptyMap();

        } else {
            decoratedRelationMap = new HashMap<>((int) (entityRelations.size() * 1.25));
            for (Relation r : entityRelations) {
                decoratedRelationMap.put(r.getType(), new JavaRelation(r));

            }

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
            List<Reference> entityReferences = entity.getReferences();
            if ((entityReferences == null) || (entityReferences.isEmpty())) {
                decoratedReferenceMap = Collections.<String, Reference> emptyMap();

            } else {
                decoratedReferenceMap = new HashMap<>((int) (entityReferences.size() * 1.25));
                for (Reference reference : entityReferences) {
                    decoratedReferenceMap.put(reference.getName(), new JavaReference(reference));

                }

            }
        }

        return decoratedReferenceMap.values().stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public Reference getReference(String type) {
        return decoratedReferenceMap.get(type);
    }

    /**
     * {@inheritDoc}
     */
    public Parent getParent() {
        return entity.getParent();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasParent() {
        return entity.getParent() != null;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isChildOfNonPersistentParentEntity() {
        return entity.isChildOfNonPersistentParentEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isNonPersistentParentEntity() {
        return entity.isNonPersistentParentEntity();
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
                    .getMetadataRepostory(DefaultModelInstanceRepository.class).getEntity(parent.getType());
            return parentEntity.getName() + "BO";
        } else {
            return BasePersistentSpringBO.class.getSimpleName();
        }
    }

    /**
     * {@inheritDoc}
     */
    public LockStrategy getLockStrategy() {
        return entity.getLockStrategy();
    }

    /**
     * {@inheritDoc}
     */
    public List<Entity> getInverseRelations() {
        if (decoratedInverseRelationMap == null) {
            List<Entity> entityInverseRelationMap = entity.getInverseRelations();
            if ((entityInverseRelationMap == null) || (entityInverseRelationMap.isEmpty())) {
                decoratedInverseRelationMap = Collections.<String, Entity> emptyMap();

            } else {
                decoratedInverseRelationMap = new HashMap<>((int) (entityInverseRelationMap.size() * 1.25));
                for (Entity r : entityInverseRelationMap) {
                    decoratedInverseRelationMap.put(r.getName(), new RelatedJavaEntity(r, this));

                }

            }
        }

        return decoratedInverseRelationMap.values().stream().collect(Collectors.toList());
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
        return entity.isTransient() != null ? entity.isTransient() : false;
    }

    /**
     * {@inheritDoc}
     */
    public String getFileName() {
        return entity.getFileName();
    }

    /**
     * {@inheritDoc}
     */
    public void validate() {
        entity.validate();

    }

    /**
     * {@inheritDoc}
     */
    public Field getIdentifier() {
        return entity.getIdentifier();
    }

    public Map<String, Field> getIdFields() {
        Field idField = getIdentifier();
        Map<String, Field> decoratedIdFieldMap = new HashMap<>();

        if (idField != null) {
            JavaField jField = new JavaField(getIdentifier());
            decoratedIdFieldMap.put(getIdentifier().getName(), jField);
        }
        return decoratedIdFieldMap;

    }

    // java-specific generation methods:

    /**
     * Returns the uncapitalized name of this entity for variable generation.
     * 
     * @return name
     */
    public String getUncapitalizedName() {
        return StringUtils.uncapitalize(getName());
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
        if (idField != null) { // identifier can be null for transient entities
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
            fkSet = javaReference.getFkImports();
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

}

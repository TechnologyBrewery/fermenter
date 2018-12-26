package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Parent;
import org.bitbucket.fermenter.mda.metadata.element.Query;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Relation;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.stout.bizobj.BasePersistentSpringBO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An {@link Entity} that has been decorated for easier generation of Java files.
 */
public class JavaEntity implements Entity {

    private Entity entity;
    private Map<String, Field> decoratedFieldMap;
    private Map<String, Field> decoratedIdFieldMap;
    private Map<String, Composite> decoratedCompositeMap;
    private Map<String, Relation> decoratedRelationMap;
    private Map<String, Entity> decoratedInverseRelationMap;
    private Map<String, Reference> decoratedReferenceMap;
    private Map<String, Query> decoratedQueryMap;
    private Set<String> imports;

    private String keySignature;
    private String keySignatureParams;

    /**
     * Create a new instance of {@link Entity} with the correct functionality
     * set to generate Java code
     * 
     * @param entityToDecorate
     *            The {@link Entity} to decorate
     */
    public JavaEntity(Entity entityToDecorate) {
        if (entityToDecorate == null) {
            throw new IllegalArgumentException("JavaEntity must be instatiated with a non-null entity!");
        }
        entity = entityToDecorate;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return entity.getName();
    }
    
    @Override
    public String getNamespace() {
        return entity.getNamespace();
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
    public String getApplicationName() {
        return entity.getApplicationName();
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
    public Map<String, Field> getFields() {
        if (decoratedFieldMap == null) {
            Map<String, Field> entityFieldMap = entity.getFields();
            if ((entityFieldMap == null) || (entityFieldMap.isEmpty())) {
                decoratedFieldMap = Collections.<String, Field> emptyMap();

            } else {
                decoratedFieldMap = new HashMap<>((int) (entityFieldMap.size() * 1.25));
                for (Field f : entityFieldMap.values()) {
                    JavaField jField = new JavaField(f);
                    decoratedFieldMap.put(f.getName(), jField);
                }

            }
        }

        return decoratedFieldMap;
    }

    /**
     * {@inheritDoc}
     */
    public Field getField(String name) {
        return getFields().get(name);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Field> getIdFields() {
        if (decoratedIdFieldMap == null) {
            Map<String, Field> entityIdFieldMap = entity.getIdFields();
            if ((entityIdFieldMap == null) || (entityIdFieldMap.isEmpty())) {
                decoratedIdFieldMap = Collections.<String, Field> emptyMap();

            } else {
                decoratedIdFieldMap = new HashMap<>((int) (entityIdFieldMap.size() * 1.25));
                for (Field f : entityIdFieldMap.values()) {
                    decoratedIdFieldMap.put(f.getName(), new JavaField(f));

                }

            }
        }

        return decoratedIdFieldMap;
    }

    /**
     * {@inheritDoc}
     */
    public Field getIdField(String name) {
        return getIdFields().get(name);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Composite> getComposites() {
        if (decoratedCompositeMap == null) {
            Map<String, Composite> entityCompositeMap = entity.getComposites();
            if ((entityCompositeMap == null) || (entityCompositeMap.isEmpty())) {
                decoratedCompositeMap = Collections.<String, Composite> emptyMap();

            } else {
                decoratedCompositeMap = new HashMap<>((int) (entityCompositeMap.size() * 1.25));
                for (Composite c : entityCompositeMap.values()) {
                    decoratedCompositeMap.put(c.getName(), new JavaComposite(c));

                }

            }
        }

        return decoratedCompositeMap;
    }

    /**
     * {@inheritDoc}
     */
    public Composite getComposite(String name) {
        return getComposites().get(name);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Relation> getRelations() {
        if (decoratedRelationMap == null) {
            Map<String, Relation> entityRelationMap = entity.getRelations();
            if ((entityRelationMap == null) || (entityRelationMap.isEmpty())) {
                decoratedRelationMap = Collections.<String, Relation> emptyMap();

            } else {
                decoratedRelationMap = new HashMap<>((int) (entityRelationMap.size() * 1.25));
                for (Relation r : entityRelationMap.values()) {
                    decoratedRelationMap.put(r.getType(), new JavaRelation(r));

                }

            }
        }

        return decoratedRelationMap;
    }

    /**
     * {@inheritDoc}
     */
    public Relation getRelation(String type) {
        return getRelations().get(type);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Entity> getInverseRelations() {
        if (decoratedInverseRelationMap == null) {
            Map<String, Entity> entityInverseRelationMap = entity.getInverseRelations();
            if ((entityInverseRelationMap == null) || (entityInverseRelationMap.isEmpty())) {
                decoratedInverseRelationMap = Collections.<String, Entity> emptyMap();

            } else {
                decoratedInverseRelationMap = new HashMap<>(
                        (int) (entityInverseRelationMap.size() * 1.25));
                for (Entity r : entityInverseRelationMap.values()) {
                    decoratedInverseRelationMap.put(r.getName(), new RelatedJavaEntity(r, this));

                }

            }
        }

        return decoratedInverseRelationMap;
    }

    /**
     * {@inheritDoc}
     */
    public Relation getInverseRelation(String type) {
        return (Relation) getInverseRelations().get(type);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Reference> getReferences() {
        if (decoratedReferenceMap == null) {
            Map<String, Reference> entityReferenceMap = entity.getReferences();
            if ((entityReferenceMap == null) || (entityReferenceMap.isEmpty())) {
                decoratedReferenceMap = Collections.<String, Reference> emptyMap();

            } else {
                decoratedReferenceMap = new HashMap<>((int) (entityReferenceMap.size() * 1.25));
                for (Reference reference : entityReferenceMap.values()) {
                    decoratedReferenceMap.put(reference.getName(), new JavaReference(reference));

                }

            }
        }

        return decoratedReferenceMap;
    }

    /**
     * {@inheritDoc}
     */
    public Reference getReference(String type) {
        return getReferences().get(type);
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
    public boolean isChildOfNonPersistentParentEntity() {
        return entity.isChildOfNonPersistentParentEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNonPersistentParentEntity() {
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
            Entity parentEntity = ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class)
                    .getEntity(parent.getType());
            return parentEntity.getName() + "BO";
        } else {
            return BasePersistentSpringBO.class.getSimpleName();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String, Query> getQueries() {
        if (decoratedQueryMap == null) {
            Map<String, Query> entityQueryMap = entity.getQueries();
            if ((entityQueryMap == null) || (entityQueryMap.isEmpty())) {
                decoratedQueryMap = Collections.<String, Query> emptyMap();

            } else {
                decoratedQueryMap = new HashMap<>((int) (entityQueryMap.size() * 1.25));
                for (Query q : entityQueryMap.values()) {
                    decoratedQueryMap.put(q.getName(), new JavaQuery(q));

                }

            }
        }

        return decoratedQueryMap;
    }

    /**
     * {@inheritDoc}
     */
    public Query getQuery(String name) {
        return getQueries().get(name);
    }

    /**
     * {@inheritDoc}
     */
    public String getLockStrategy() {
        return entity.getLockStrategy();
    }

    /**
     * {@inheritDoc}
     */
    public boolean useOptimisticLocking() {
        return entity.useOptimisticLocking();
    }

    // java-specific generation methods:
    
    /**
     * Returns the uncapitalized name of this entity for variable generation.
     * @return name
     */
    public String getUncapitalizedName() {
        return StringUtils.uncapitalize(getName());
    }

    /**
     * Returns the full set of imports.
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
     * @return field imports
     */
    public Set<String> getFieldImports() {
        return getFieldImports(true);
    }

    /**
     * Returns the full set of imports for fields.
     * @return field imports
     */
    public Set<String> getIdFieldImports() {
        return getFieldImports(false);
    }
    
    protected Set<String> getFieldImports(boolean includeNonIdFields) {
        Set<String> importSet = new HashSet<>();
        
        Map<String, Field> fieldCollection = new HashMap<>();
        fieldCollection.putAll(getIdFields());
        if (includeNonIdFields) {
            fieldCollection.putAll(getFields());
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
     * @return reference imports
     */
    public Set<String> getReferenceImports() {
        Set<String> importSet = new HashSet<>();
        Set<String> fkSet;

        JavaReference javaReference;
        for (Reference reference : getReferences().values()) {
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
     * Returns a Java method's signature based on this instance's key fields.
     * 
     * @return The key signature
     */
    public String getKeySignature() {
        if (keySignature == null) {
            Map<String, Field> idFieldMap = getIdFields();
            if (idFieldMap != null) {
                List<Field> keyList = new ArrayList<>(idFieldMap.values());
                keySignature = JavaElementUtils.createSignatureFields(keyList);

            }
        }

        return keySignature;
    }

    /**
     * Returns a Java signature's parameters based on this instance's key
     * fields.
     * 
     * @return The key signature params
     */
    public String getKeySignatureParams() {
        if (keySignatureParams == null) {
            Map<String, Field> idFieldMap = getIdFields();
            if (idFieldMap != null) {
                List<Field> keyList = new ArrayList<>(idFieldMap.values());
                keySignatureParams = JavaElementUtils.createSignatureFieldParams(keyList);

            }
        }

        return keySignatureParams;
    }

    /**
     * Returns the prefixes for all references (e.g., org.blah versus
     * org.blah.Foo).
     * 
     * @return collection of import prefixes
     */
    public Collection<String> getImportPrefixes() {
        Collection<String> prefixes = new TreeSet<>();

        for (Reference reference : getReferences().values()) {
            JavaReference ref = (JavaReference) reference;
            prefixes.add(ref.getImportPrefix());
        }

        return prefixes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransient(boolean transientEntity) {
        entity.setTransient(transientEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTransient() {
        return entity.isTransient();
    }
    
    public boolean hasNamedEnumeration() {
        boolean result = false;
        for(Field field : getFields().values()) {
            if(field.isEnumerationType()) {
                if(!field.getEnumeration().isValued()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}

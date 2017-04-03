package org.bitbucket.fermenter.stout.mda;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Query;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

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
                decoratedFieldMap = new HashMap<String, Field>((int) (entityFieldMap.size() * 1.25));
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
                decoratedIdFieldMap = new HashMap<String, Field>((int) (entityIdFieldMap.size() * 1.25));
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
                decoratedCompositeMap = new HashMap<String, Composite>((int) (entityCompositeMap.size() * 1.25));
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
                decoratedRelationMap = new HashMap<String, Relation>((int) (entityRelationMap.size() * 1.25));
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
                decoratedInverseRelationMap = new HashMap<String, Entity>(
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
                decoratedReferenceMap = new HashMap<String, Reference>((int) (entityReferenceMap.size() * 1.25));
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
        return (Reference) getReferences().get(type);
    }

    /**
     * {@inheritDoc}
     */
    public String getSuperclass() {
        return entity.getSuperclass();
    }

    /**
     * {@inheritDoc}
     */
    public String getParent() {
        return entity.getParent();
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
                decoratedQueryMap = new HashMap<String, Query>((int) (entityQueryMap.size() * 1.25));
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
        return (Query) getQueries().get(name);
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
            imports = new TreeSet<String>();
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
        Set<String> importSet = new HashSet<String>();
        importSet.add(Date.class.getName());
        importSet.add(Timestamp.class.getName());
        importSet.add(BigDecimal.class.getName());

        JavaField javaField;
        Map<String, Field> operationCollection = getFields();
        for (Field field : operationCollection.values()) {
            javaField = (JavaField) field;
            importSet.add(javaField.getImport());
        }

        return importSet;
    }

    
    /**
     * Returns the full set of imports for references.
     * @return reference imports
     */
    public Set<String> getReferenceImports() {
        Set<String> importSet = new HashSet<String>();
        Set<String> fkSet;

        JavaReference javaReference;
        for (Reference reference : getReferences().values()) {
            javaReference = (JavaReference) reference;
            fkSet = javaReference.getFkImports();
            if (fkSet != null) {
                importSet.addAll(fkSet);
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
                List<Field> keyList = new ArrayList<Field>(idFieldMap.values());
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
                List<Field> keyList = new ArrayList<Field>(idFieldMap.values());
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
        Collection<String> prefixes = new TreeSet<String>();

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

}

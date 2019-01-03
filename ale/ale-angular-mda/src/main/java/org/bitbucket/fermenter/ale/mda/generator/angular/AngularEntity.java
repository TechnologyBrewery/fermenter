package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Parent;
import org.bitbucket.fermenter.mda.metadata.element.Query;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

import com.google.common.base.CaseFormat;

public class AngularEntity implements Entity {

    private static final String ID_FIELD_DOES_NOT_EXIST = "ID_FIELD_DOES_NOT_EXIST";
    private Entity entity;
    private Map<String, AngularField> allFieldMap;
    private Map<String, Field> decoratedIdFieldMap;
    private Map<String, AngularAssociation> decoratedAssociationsMap;

    public AngularEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getNamespace() {
        return entity.getNamespace();
    }

    @Override
    public String getName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entity.getName());
    }

    public String getNameUpperCamel() {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, entity.getName());
    }

    public String getNameLowerCamel() {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, entity.getName());
    }
    
    public String getApplicationNameLowerCamel() {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, entity.getApplicationName());
    }

    public Map<String, AngularAssociation> getAssociations() {
        if (decoratedAssociationsMap == null) {
            Map<String, Relation> entityRelationMap = entity.getRelations();
            Map<String, Reference> entityReferenceMap = entity.getReferences();
            if ((entityRelationMap == null || entityRelationMap.isEmpty())
                    && (entityReferenceMap == null || entityReferenceMap.isEmpty())) {
                decoratedAssociationsMap = Collections.<String, AngularAssociation> emptyMap();
            } else {
                decoratedAssociationsMap = new HashMap<>();
                if (entityRelationMap != null) {
                    for (Relation r : entityRelationMap.values()) {
                        decoratedAssociationsMap.put(r.getType(), new AngularAssociation(r));
                    }
                }
                if (entityReferenceMap != null) {
                    for (Reference r : entityReferenceMap.values()) {
                        decoratedAssociationsMap.put(r.getType(), new AngularAssociation(r));
                    }
                }
            }
        }
        return decoratedAssociationsMap;
    }

    @Override
    public Map<String, Field> getIdFields() {
        if (decoratedIdFieldMap == null) {
            Map<String, Field> entityIdFieldMap = entity.getIdFields();
            if ((entityIdFieldMap == null) || (entityIdFieldMap.isEmpty())) {
                decoratedIdFieldMap = Collections.<String, Field> emptyMap();

            } else {
                decoratedIdFieldMap = new HashMap<>((int) (entityIdFieldMap.size() * 1.25));
                for (Field f : entityIdFieldMap.values()) {
                    decoratedIdFieldMap.put(f.getName(), new AngularField(f));

                }
            }
        }

        return decoratedIdFieldMap;
    }
    
    public String getIdFieldName() {
        String idFieldName = ID_FIELD_DOES_NOT_EXIST;
        if(!getIdFields().isEmpty()) {
            idFieldName = getIdFields().values().iterator().next().getName();
        }
        return idFieldName;
    }

    public Map<String, AngularField> getAllFields() {
        if (allFieldMap == null) {
            Map<String, Field> entityFieldMap = new HashMap<>();
            if (entity.getIdFields() != null) {
                entityFieldMap.putAll(entity.getIdFields());
            }
            if (entity.getFields() != null) {
                entityFieldMap.putAll(entity.getFields());
            }
            if ((entityFieldMap == null) || (entityFieldMap.isEmpty())) {
                allFieldMap = Collections.<String, AngularField> emptyMap();

            } else {
                allFieldMap = new HashMap<>((int) (entityFieldMap.size() * 1.25));
                for (Field f : entityFieldMap.values()) {
                    allFieldMap.put(f.getName(), new AngularField(f));

                }
            }
        }
        return allFieldMap;
    }

    @Override
    public String getDocumentation() {
        return entity.getDocumentation();
    }

    @Override
    public String getTable() {
        return entity.getTable();
    }

    @Override
    public Map<String, Field> getFields() {
        return entity.getFields();
    }

    @Override
    public Field getField(String name) {
        return entity.getField(name);
    }

    @Override
    public Field getIdField(String name) {
        return entity.getIdField(name);
    }

    @Override
    public Map getComposites() {
        return entity.getComposites();
    }

    @Override
    public Composite getComposite(String name) {
        return entity.getComposite(name);
    }

    @Override
    public Map getRelations() {
        return entity.getRelations();
    }

    @Override
    public Relation getRelation(String type) {
        return entity.getRelation(type);
    }

    @Override
    public Map getInverseRelations() {
        return entity.getInverseRelations();
    }

    @Override
    public Relation getInverseRelation(String type) {
        return entity.getInverseRelation(type);
    }

    @Override
    public Map getReferences() {
        return entity.getReferences();
    }

    @Override
    public Reference getReference(String type) {
        return entity.getReference(type);
    }

    @Override
    public Map getQueries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query getQuery(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLockStrategy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean useOptimisticLocking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransient(boolean transientEntity) {
        entity.setTransient(transientEntity);
    }

    @Override
    public boolean isTransient() {
        return entity.isTransient();
    }

    @Override
    public String getApplicationName() {
        return entity.getApplicationName();
    }

    @Override
    public Parent getParent() {
        return entity.getParent();
    }

    @Override
    public boolean isNonPersistentParentEntity() {
        return entity.isNonPersistentParentEntity();
    }

    @Override
    public boolean isChildOfNonPersistentParentEntity() {
        return entity.isChildOfNonPersistentParentEntity();
    }

}

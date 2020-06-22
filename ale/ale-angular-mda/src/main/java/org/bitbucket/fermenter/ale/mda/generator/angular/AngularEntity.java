package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Parent;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;

public class AngularEntity implements Entity, AngularNamedElement {

    private static final String ID_FIELD_DOES_NOT_EXIST = "ID_FIELD_DOES_NOT_EXIST";
    private Entity entity;
    private Map<String, AngularField> allFieldMap;
    private Map<String, AngularAssociation> decoratedAssociationsMap;

    public AngularEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getPackage() {
        return entity.getPackage();
    }

    public Map<String, AngularAssociation> getAssociations() {
        if (decoratedAssociationsMap == null) {
            List<Relation> entityRelations = entity.getRelations();
            List<Reference> entityReferences = entity.getReferences();
            if ((entityRelations == null || entityRelations.isEmpty())
                    && (entityReferences == null || entityReferences.isEmpty())) {
                decoratedAssociationsMap = Collections.<String, AngularAssociation> emptyMap();
            } else {
                decoratedAssociationsMap = new HashMap<>();
                if (entityRelations != null) {
                    for (Relation r : entityRelations) {
                        decoratedAssociationsMap.put(r.getType(), new AngularAssociation(r));
                    }
                }
                if (entityReferences != null) {
                    for (Reference r : entityReferences) {
                        decoratedAssociationsMap.put(r.getType(), new AngularAssociation(r));
                    }
                }
            }
        }
        return decoratedAssociationsMap;
    }

    @Override
    public Field getIdentifier() {
        return entity.getIdentifier();
    }

    public String getIdFieldName() {
        String idFieldName = ID_FIELD_DOES_NOT_EXIST;
        Field identifier = getIdentifier();
        if (identifier != null) {
            idFieldName = getIdentifier().getName();
        }
        return idFieldName;
    }

    public Map<String, AngularField> getAllFields() {
        if (allFieldMap == null) {
            Map<String, Field> entityFieldMap = new HashMap<>();
            Field identifier = getIdentifier();
            if (identifier != null) {
                entityFieldMap.put(identifier.getName(), identifier);
            }
            if (entity.getFields() != null) {
                for (Field field : entity.getFields())
                    entityFieldMap.put(field.getName(), field);
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
    public List<Field> getFields() {
        return entity.getFields();
    }

    @Override
    public List<Relation> getRelations() {
        return entity.getRelations();
    }

    @Override
    public Relation getRelation(String type) {
        return entity.getRelation(type);
    }

    @Override
    public List<Entity> getInverseRelations() {
        return entity.getInverseRelations();
    }

    @Override
    public List<Reference> getReferences() {
        return entity.getReferences();
    }

    @Override
    public Boolean isTransient() {
        return (entity.isTransient() == null) ? Boolean.FALSE : entity.isTransient();
    }

    @Override
    public Parent getParent() {
        return entity.getParent();
    }

    @Override
    public Boolean isNonPersistentParentEntity() {
        return (entity.isNonPersistentParentEntity() == null) ? Boolean.FALSE : entity.isNonPersistentParentEntity();
    }

    @Override
    public Boolean isChildOfNonPersistentParentEntity() {
        return (entity.isChildOfNonPersistentParentEntity() == null) ? Boolean.FALSE
                : entity.isChildOfNonPersistentParentEntity();
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public String getFileName() {
        return entity.getFileName();
    }

    @Override
    public void validate() {
        entity.validate();

    }

    @Override
    public LockStrategy getLockStrategy() {
        return entity.getLockStrategy();
    }

}

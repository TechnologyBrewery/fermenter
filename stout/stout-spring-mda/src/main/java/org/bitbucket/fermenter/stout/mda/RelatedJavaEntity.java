package org.bitbucket.fermenter.stout.mda;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;

public class RelatedJavaEntity extends JavaEntity {

    private Entity entity;
    private Entity parentEntity;
    private Map<String, Field> decoratedIdFieldMap;

    public RelatedJavaEntity(Entity entity, Entity parentEntity) {
        super(entity);

        this.entity = entity;
        this.parentEntity = parentEntity;

    }

    public String getLabel() {
        return StringUtils.uncapitalise(entity.getName());
    }

    public Map getIdFields() {
        if (isSelfRelation().booleanValue()) {
            if (decoratedIdFieldMap == null) {
                Field idField = entity.getIdentifier();
                if ((idField == null)) {
                    decoratedIdFieldMap = Collections.emptyMap();

                } else {
                    decoratedIdFieldMap = new HashMap<>();
                    decoratedIdFieldMap.put(idField.getName(), new SelfReferenceField(idField));
                }
            }

        } else {
            Relation relation = entity.getRelation(parentEntity.getName());
            Field parentIdentifier = relation.getParentIdentifier(entity.getName());
            decoratedIdFieldMap = new HashMap<>();
            decoratedIdFieldMap.put(parentIdentifier.getName(), parentIdentifier);

        }

        return decoratedIdFieldMap;
    }

    public Field getIdField(String name) {
        return (Field) getIdFields().get(name);
    }

    class SelfReferenceField extends JavaField {

        private Field field;
        private String overriddenColumnName;

        SelfReferenceField(Field field) {
            super(field);
            this.field = field;
        }

        /**
         * Prevents this field from having the same column name as its parent
         * 
         * @see org.bitbucket.fermenter.stout.mda.element.java.JavaField#getColumn()
         */
        public String getColumn() {
            if (overriddenColumnName == null) {
                overriddenColumnName = "FK_" + field.getColumn();

            }

            return overriddenColumnName;
        }

    }

    public Boolean isSelfRelation() {
        return (entity.getName().equals(parentEntity.getName())) ? Boolean.TRUE : Boolean.FALSE;
    }

}

package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;

public class RelatedJavaEntity extends JavaEntity {

    private Entity entity;
    private Entity parentEntity;
    private Field decoratedIdentifier;

    public RelatedJavaEntity(Entity entity, Entity parentEntity) {
        super(entity);

        this.entity = entity;
        this.parentEntity = parentEntity;

    }

    public String getLabel() {
        return StringUtils.uncapitalize(entity.getName());
    }

    @Override
    public Field getIdentifier() {
        if (isSelfRelation().booleanValue()) {
            if (decoratedIdentifier == null) {
                Field idField = entity.getIdentifier();
                decoratedIdentifier = new SelfReferenceField(idField);
            }

        } else {
            Relation relation = entity.getRelation(parentEntity.getName());
            Field parentIdentifier = relation.getParentIdentifier(entity.getName());
            decoratedIdentifier = parentIdentifier;

        }

        return decoratedIdentifier;
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

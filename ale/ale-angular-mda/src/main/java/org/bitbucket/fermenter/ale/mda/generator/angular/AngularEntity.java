package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.fermenter.mda.metamodel.element.BaseEntityDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;

public class AngularEntity extends BaseEntityDecorator implements Entity, AngularNamedElement {

    private static final String ID_FIELD_DOES_NOT_EXIST = "ID_FIELD_DOES_NOT_EXIST";
    private Map<String, AngularField> allFieldMap;
    private Map<String, AngularField> importFieldMap;
    private Map<String, AngularAssociation> decoratedAssociationsMap;

    public AngularEntity(Entity wrapped) {
        super(wrapped);
    }

    public Map<String, AngularAssociation> getAssociations() {
        if (decoratedAssociationsMap == null) {
            List<Relation> entityRelations = getRelations();
            List<Reference> entityReferences = getReferences();
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
            if (getFields() != null) {
                for (Field field : getFields())
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
    
    public Map<String, AngularField> getAllImportFields() {
        if (importFieldMap == null) {
            Map<String, Field> entityFieldMap = new HashMap<>();
            List<String> importedTypes = new ArrayList<>();
            Field identifier = getIdentifier();
            if (identifier != null) {
                entityFieldMap.put(identifier.getName(), identifier);
            }
            if (getFields() != null) {
                for (Field field : getFields()) {
                    entityFieldMap.put(field.getName(), field);
                }
            }
            if ((entityFieldMap == null) || (entityFieldMap.isEmpty())) {
                importFieldMap = Collections.<String, AngularField> emptyMap();
            } else {
                importFieldMap = new HashMap<>((int) (entityFieldMap.size() * 1.25));
                for (Field f : entityFieldMap.values()) {
                    if (!importedTypes.contains(f.getType())) {
                        importedTypes.add(f.getType());
                        importFieldMap.put(f.getName(), new AngularField(f));
                    }

                }
            }
        }
        return importFieldMap;
    }

    @Override
    public Boolean isTransient() {
        return (wrapped.isTransient() == null) ? Boolean.FALSE : wrapped.isTransient();
    }

}

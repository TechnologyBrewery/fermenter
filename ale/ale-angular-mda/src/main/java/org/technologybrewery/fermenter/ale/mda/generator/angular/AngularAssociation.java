package org.technologybrewery.fermenter.ale.mda.generator.angular;

import java.util.Collection;

import org.technologybrewery.fermenter.mda.metamodel.element.Field;
import org.technologybrewery.fermenter.mda.metamodel.element.Reference;
import org.technologybrewery.fermenter.mda.metamodel.element.Relation;

import com.google.common.base.CaseFormat;

public class AngularAssociation implements Relation {

    private static final String RELATION_NON_NULL = "Relations must be instantiated with a non-null relation!";
    private Relation relation;
    private Reference reference;

    public AngularAssociation(Relation relationToDecorate) {
        if (relationToDecorate == null) {
            throw new IllegalArgumentException(RELATION_NON_NULL);
        }
        relation = relationToDecorate;
    }

    public AngularAssociation(Reference referenceToDecorate) {
        if (referenceToDecorate == null) {
            throw new IllegalArgumentException(RELATION_NON_NULL);
        }
        reference = referenceToDecorate;
    }

    public String getTypeLowerHyphen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getType());
    }

    public String getDocumentation() {
        if (reference != null) {
            return reference.getDocumentation();
        } else {
            return relation.getDocumentation();
        }
    }

    public Multiplicity getMultiplicity() {
        if (reference != null) {
            return Multiplicity.ONE_TO_ONE;
        } else {
            return relation.getMultiplicity();
        }
    }

    @Override
    public FetchMode getFetchMode() {
        throw new UnsupportedOperationException();
    }

    public String getType() {
        if (reference != null) {
            return reference.getType();
        } else {
            return relation.getType();
        }
    }

    public String getTable() {
        throw new UnsupportedOperationException();
    }

    public Collection getKeys(String parentEntityName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validate() {
        if (reference != null) {
            reference.validate();
        } else {
            relation.validate();
        }

    }

    @Override
    public String getPackage() {
        if (reference != null) {
            return reference.getPackage();
        } else {
            return relation.getPackage();
        }
    }

    @Override
    public Field getParentIdentifier(String parentEntityName) {
        if (relation != null) {
            return relation.getParentIdentifier(relation.getType());
        }
        return null;
    }

    public String getLabelLowerCamel() {
        if (reference != null) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, reference.getName());
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, relation.getType());
        }
    }

}

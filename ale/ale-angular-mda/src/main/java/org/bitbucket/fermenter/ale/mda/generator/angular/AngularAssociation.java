package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Collection;

import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

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

    public String getTypeLowerHypen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getType());
    }

    public String getDocumentation() {
        if (reference != null) {
            return reference.getDocumentation();
        } else {
            return relation.getDocumentation();
        }
    }

    public String getMultiplicity() {
        if (reference != null) {
            return "1-1";
        } else {
            return relation.getMultiplicity();
        }
    }

    @Override
    public String getFetchMode() {
        throw new UnsupportedOperationException();
    }

    public String getType() {
        if (reference != null) {
            return reference.getType();
        } else {
            return relation.getType();
        }
    }

    public String getLabelLowerCamel() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getLabel());
    }

    public String getLabel() {
        if (reference != null) {
            return reference.getLabel();
        } else {
            return relation.getLabel();
        }
    }

    public String getTable() {
        throw new UnsupportedOperationException();
    }

    public Collection getKeys(String parentEntityName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection getChildRelations() {
        throw new UnsupportedOperationException();
    }

}

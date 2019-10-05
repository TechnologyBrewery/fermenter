package org.bitbucket.fermenter.stout.mda;


import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;

public class JavaRelation implements Relation {

    private Relation relation;

    /**
     * Create a new instance of {@link Relation} with the correct functionality set to generate Java code
     * 
     * @param relationToDecorate
     *            The {@link Relation} to decorate
     */
    public JavaRelation(Relation relationToDecorate) {
        if (relationToDecorate == null) {
            throw new IllegalArgumentException("JavaRelations must be instatiated with a non-null relation!");
        }
        relation = relationToDecorate;
    }

    public String getDocumentation() {
        return relation.getDocumentation();
    }

    public Multiplicity getMultiplicity() {
        return relation.getMultiplicity();
    }

    /**
     * {@inheritDoc}
     * 
     * Return the fetch mode, defaulting to eager unless otherwise specified. While many would like the default to be
     * lazy, unless you are working with very large data sizes, this often is less convenient in practice and has little
     * performance impact.
     */
    @Override
    public FetchMode getFetchMode() {
        FetchMode fetchMode = relation.getFetchMode();
        return (fetchMode != null) ? fetchMode : FetchMode.EAGER;
    }

    public String getType() {
        return relation.getType();
    }

    public String getUncapitalizedType() {
        return StringUtils.uncapitalize(getType());
    }

    @Override
    public void validate() {
        relation.validate();

    }

    @Override
    public String getPackage() {
        return relation.getPackage();
    }

    @Override
    public String getLocalColumn() {
        return relation.getLocalColumn();
    }

    @Override
    public Field getParentIdentifier(String parentEntityName) {
        return relation.getParentIdentifier(parentEntityName);
    }

    public String getLabel() {
        return StringUtils.uncapitalize(getType());
    }
}

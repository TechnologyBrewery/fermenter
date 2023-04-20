package org.technologybrewery.fermenter.stout.mda;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseRelationDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Relation;

public class JavaRelation extends BaseRelationDecorator implements Relation {

    /**
     * {@inheritDoc}
     */
    public JavaRelation(Relation wrapped) {
        super(wrapped);
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
        FetchMode fetchMode = wrapped.getFetchMode();
        return (fetchMode != null) ? fetchMode : FetchMode.EAGER;
    }

    /**
     * Uncapitalizes the relation type.
     * 
     * @return uncapitalized type
     */
    public String getUncapitalizedType() {
        return StringUtils.uncapitalize(getType());
    }
    
}

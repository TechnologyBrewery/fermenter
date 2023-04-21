package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Adds the concept of a parent column for mapping foreign key fields.
 */
public class ForeignKeyFieldElement extends FieldElement {

    private String parentColumn;

    /**
     * Returns the name of the parent column.
     * 
     * @return parent column name
     */
    public String getParentColumn() {
        // for convenience, just return the column name if an override has not been set:
        return (parentColumn != null) ? parentColumn : getColumn();
    }

    /**
     * Sets the name of the parent column.
     * 
     * @param parentColumn
     *            parent column name
     */
    public void setParentColumn(String parentColumn) {
        this.parentColumn = parentColumn;
    }

    /**
     * Returns the name of the child column.
     * 
     * @return parent column name
     */
    public String getChildColumn() {
        return getColumn();
    }

}

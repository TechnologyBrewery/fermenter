package org.bitbucket.fermenter.mda.metadata.element;

import java.util.Map;

public interface Entity {

    static final String LOCK_STATEGY_OPTIMISTIC = "optimistic";
    static final String LOCK_STATEGY_NONE = "none";

    String getNamespace();
    
    /**
     * @return Logical entity name
     */
    String getName();

    String getDocumentation();

    /**
     * @return Returns the table.
     */
    String getTable();

    Map<String, Field> getFields();

    Field getField(String name);

    Map<String, Field> getIdFields();

    Field getIdField(String name);

    Map getComposites();

    Composite getComposite(String name);

    Map getRelations();

    Relation getRelation(String type);

    Map getInverseRelations();

    Relation getInverseRelation(String type);

    Map getReferences();

    Reference getReference(String type);

    /**
     * @return Returns the superclass.
     */
    String getSuperclass();

    /**
     * @return Returns the parent.
     */
    String getParent();

    Map getQueries();

    Query getQuery(String name);

    String getLockStrategy();

    boolean useOptimisticLocking();

    /**
     * Sets whether or not the entity should persist.
     * 
     * @param transientEntity
     *            setting
     */
    void setTransient(boolean transientEntity);

    /**
     * Returns whether or not the entity should persist.
     * @return setting
     */
     boolean isTransient();

    /**
     * Returns the name of the application from which this element originates
     * 
     * @return Application name
     */
    String getApplicationName();

}
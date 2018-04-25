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
     * @return Returns the parent entity of this entity.
     */
    Parent getParent();

    /**
     * Returns whether this entity has been marked as the parent of any other entity and the defined inheritance
     * strategy indicates that this parent entity is non-persistent. <br>
     * <br>
     * <b>NOTE:</b> While somewhat similar, this models a different concept than {@link Entity#isTransient()}. Transient
     * entities have *no* persistent state, while non-persistent parent entities may have persistent fields declared
     * within them, but the parent entities are not directly persisted (their concrete child subclasses are).
     * 
     * @return
     */
    boolean isNonPersistentParentEntity();
    
    Map getQueries();

    Query getQuery(String name);

    String getLockStrategy();

    boolean useOptimisticLocking();

    /**
     * Sets whether or not the entity has any persistent state.
     * 
     * @param transientEntity
     *            setting
     */
    void setTransient(boolean transientEntity);

    /**
     * Returns whether or not the entity has any persistent state. If true, this entity will be exclusively used as a
     * transfer object.
     * 
     * @return
     */
    boolean isTransient();

    /**
     * Returns the name of the application from which this element originates
     * 
     * @return Application name
     */
    String getApplicationName();

}
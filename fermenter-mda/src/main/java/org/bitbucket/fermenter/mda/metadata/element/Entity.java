package org.bitbucket.fermenter.mda.metadata.element;


import java.util.Map;

public interface Entity {
	
	public static final String LOCK_STATEGY_OPTIMISTIC = "optimistic";
	public static final String LOCK_STATEGY_NONE = "none";

	/**
	 * @return Logical entity name
	 */
	public String getName();
	
	public String getDocumentation();

	/**
	 * @return Returns the table.
	 */
	public String getTable();

	public Map getFields();

	public Field getField(String name);

	public Map getIdFields();

	public Field getIdField(String name);

	public Map getComposites();

	public Composite getComposite(String name);

	public Map getRelations();

	public Relation getRelation(String type);

	public Map getInverseRelations();

	public Relation getInverseRelation(String type);

	public Map getReferences();

	public Reference getReference(String type);

	/**
	 * @return Returns the superclass.
	 */
	public String getSuperclass();

	/**
	 * @return Returns the parent.
	 */
	public String getParent();

	public Map getQueries();

	public Query getQuery(String name);

	public String getLockStrategy();

	public boolean useOptimisticLocking();
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	public String getApplicationName();

}
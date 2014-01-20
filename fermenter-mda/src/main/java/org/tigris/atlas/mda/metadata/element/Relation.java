package org.tigris.atlas.mda.metadata.element;

import java.util.Collection;

public interface Relation {

	public String getDocumentation();
	
	/**
	 * @return Returns the multiplicity.
	 */
	public String getMultiplicity();

	/**
	 * @return Returns the type.
	 */
	public String getType();

	public String getLabel();

	public String getTable();

	/**
	 * Returns the relations for the next level in the object tree
	 * @return
	 */
	public Collection getChildRelations();

	/**
	 * Gets the key fields that the child class will reference for this relation
	 * @return The key fields
	 */
	public Collection getKeys();

}
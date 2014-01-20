package org.tigris.atlas.mda.metadata.element;

import java.util.Collection;

/**
 * Metadata for system input forms
 * 
 * @author sandrews
 *
 */
public interface Form {
	
	/**
	 * Get the name of the form
	 * 
	 * @return String The form name
	 */
	public String getName();
	
	/**
	 * Get the list of associated entities
	 * 
	 * @return Collection Entities associated with the form
	 */
	public Collection getEntities();
	
	/**
	 * Get the list of fields on a form not associated with any entity.
	 * 
	 * @return Fields not associated with an entity
	 */
	public Collection getFormOnlyFields();
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	public String getApplicationName();

}

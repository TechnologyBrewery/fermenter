package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

@Deprecated
public interface Reference {

	/**
	 * @return Returns the type.
	 */
	public String getType();
	
	/**
	 * Answer the project is this is an external reference
	 * 
	 * @return String - The project name
	 */
	public String getProject();

	public String getLabel();

	public String getName();
	
	public String getDocumentation();

	/**
	 * Gets the foreign keys associated with this reference plus all parent references
	 * @return List of foreign key fields
	 */
	public List getForeignKeyFields();

	/**
	 * @return Returns the required.
	 */
	public String getRequired();

	public boolean isRequired();

}
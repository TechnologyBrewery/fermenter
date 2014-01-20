package org.tigris.atlas.mda.metadata.element;

public interface Parameter {

	public String getName();
	
	public String getDocumentation();

	public String getType();
	
	/**
	 * If this type is a cross-project entity, then it must
	 * have a project 
	 * @return The name of the project or null if not a cross-
	 * project entity
	 */
	public String getProject();
	
	public boolean isMany();

}
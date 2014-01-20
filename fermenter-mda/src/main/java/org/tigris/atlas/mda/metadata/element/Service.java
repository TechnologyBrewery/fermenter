package org.tigris.atlas.mda.metadata.element;


import java.util.Map;

public interface Service {
	
	public String getName();
	
	public String getDocumentation();

	public Map getOperations();

	public Operation getOperation(String name);
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	public String getApplicationName();

}
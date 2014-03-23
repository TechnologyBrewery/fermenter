package org.tigris.atlas.mda.metadata.element;


import java.util.Map;

public interface Service {
	
	String getName();
	
	String getDocumentation();

	Map<String, Operation> getOperations();

	Operation getOperation(String name);
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	String getApplicationName();

}
package org.tigris.atlas.mda.metadata.element;

import java.util.Collection;

public interface Composite {

	public String getName();	
	
	public String getType();
	
	public String getProject();
	
	public String getPrefix();
	
	public String getLabel();

	public Collection getFields();

	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	public String getApplicationName();	
	
}
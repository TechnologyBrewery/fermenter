package org.tigris.atlas.mda.metadata.element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServiceMetadata extends MetadataElement implements Service {

	private String name;
	private String documentation;
	private String applicationName;
	private Map operations;
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Service#getName()
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * @see org.tigris.atlas.mda.metadata.Service#getDocumentation()
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/*
	 * @see org.tigris.atlas.mda.metadata.Service#getOperations()
	 */
	public Map getOperations() {
		if( operations == null ) {
			operations = new HashMap();
		}
		
		return operations;
	}
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Service#getOperation(java.lang.String)
	 */
	public Operation getOperation(String name) {
		return (Operation) getOperations().get( name );
	}
	
	public void addOperation(Operation operation) {
		getOperations().put( operation.getName(), operation );
	}
	
	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 *
	 */
	public void validate() {
		for (Iterator i = getOperations().values().iterator(); i.hasNext();) {
			OperationMetadata op = (OperationMetadata) i.next();
			op.validate();
		}
	}

}

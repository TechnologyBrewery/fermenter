package org.bitbucket.fermenter.mda.metadata.element;

import java.util.HashMap;
import java.util.Map;

public class ServiceMetadata extends MetadataElement implements Service {

	private String name;
	private String documentation;
	private String applicationName;
	private Map<String, Operation> operations;
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Operation> getOperations() {
		if( operations == null ) {
			operations = new HashMap<String, Operation>();
		}
		
		return operations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Operation getOperation(String name) {
		return (Operation) getOperations().get( name );
	}
	
	public void addOperation(Operation operation) {
		getOperations().put( operation.getName(), operation );
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate() {
		for (Operation o : getOperations().values()) {
			OperationMetadata op = (OperationMetadata)o;
			op.validate();
		}
	}

}

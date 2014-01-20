package org.tigris.atlas.service;

import java.io.Serializable;
import java.util.List;

/**
 * This class contains the information needed to invoke a service
 * that has asynchronously.  Ideally, this would be persisted as a 
 * schema to provide a greater degree of decoupling.  For the time
 * being, this will simply serialize this descriptor.  
 */
public class AsynchronousServiceDescriptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String operationName;
	private String projectName;
	private List paramList;
	private List classList;
	
	/**
	 * @return Returns the operationName.
	 */
	public String getOperationName() {
		return operationName;
	}
	
	/**
	 * @param operationName The operationName to set.
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	/**
	 * @return Returns the projectName.
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * @param projectName The projectName to set.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * @return Returns the serviceName.
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * @param serviceName The serviceName to set.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * @return Returns the paramList.
	 */
	public List getParameterList() {
		return paramList;
	}

	/**
	 * @param paramList The paramList to set.
	 */
	public void setParameterList(List paramList) {
		this.paramList = paramList;
	}

	/**
	 * @return Returns the classList.
	 */
	public List getClassList() {
		return classList;
	}

	/**
	 * @param classList The classList to set.
	 */
	public void setClassList(List classList) {
		this.classList = classList;
	}	
	
}

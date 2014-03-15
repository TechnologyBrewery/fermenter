package org.tigris.atlas.mda.metadata.element;

import java.util.List;

public interface Operation {
	
	/**
	 * These transaction attribute constants are listed exactly as 
	 * they are specified by the JEE specification.  They are uppercased
	 * to eliminate the possibility of case-related issues when reading
	 * and translating these values.
	 */
	public static final String TRANSACTION_REQUIRED = "Required";
	public static final String TRANSACTION_REQUIRES_NEW = "RequiresNew";
	public static final String TRANSACTION_MANDATORY = "Mandatory";
	public static final String TRANSACTION_NOT_SUPPORTED = "NotSupported";
	public static final String TRANSACTION_SUPPORTS = "Supports";
	public static final String TRANSACTION_NEVER= "Never";
	
	/**
	 * This view type represents that an operation should be visiable to both
	 * local and remote service clients.
	 */
	public static final String VIEW_TYPE_BOTH = "both";
	
	/**
	 * This view type represents that an operation should be visiable only to remote service clients.
	 */
	public static final String VIEW_TYPE_REMOTE = "remote";
	
	/**
	 * This view type represents that an operation should be visiable only to local service clients.
	 */
	public static final String VIEW_TYPE_LOCAL = "local";	
	
	/**
	 * Represents a synchronous operation invocation
	 */
	public static final String TRANSMISSION_METHOD_SYNC = "synchronous";

	/**
	 * Represents an asynchronous operation invocation
	 */
	public static final String TRANSMISSION_METHOD_ASYNC = "asynchronous";
	
	String getName();
	
	String getDocumentation();

	String getReturnType();

	String getLowercaseName();

	List<Parameter> getParameters();

	String getReturnManyType();

	/**
	 * @return Returns the transactionAttribute.
	 */
	String getTransactionAttribute();
	
	/**
	 * Returns where this operation can be viewed - typically, 'remote', 'local' or 'both'.
	 * By default, this wil return 'both'
	 * @return
	 */
	String getViewType();
	
	/**
	 * Returns the transmission method of this operation - either synchronous or asynchronous.
	 * If no value is specified, <tt>TRANSMISSION_METHOD_SYNC</tt> will be returned.
	 * @return <tt>TRANSMISSION_METHOD_SYNC</tt> or <tt>TRANSMISSION_METHOD_ASYNC</tt>
	 */	
	String getTransmissionMethod();

}
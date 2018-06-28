package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

@Deprecated
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
	 * Returns the encoding of the response produced by this operation.
	 * 
	 * @return encoding of the response produced by this operation (i.e. UTF-8, ISO-8859, etc.)
	 */
	String getResponseEncoding();
	
	/**
	 * @return true if the data to be sent is GZIP compressed; false otherwise
	 */
	boolean isCompressedWithGzip();
}
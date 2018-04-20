package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

@Deprecated
public interface Enumeration {
		
	static final String TYPE_NAMED_ENUMERATION = "named";
	static final String TYPE_VALUED_ENUMERATION = "valued";
	
	/**
	 * @return Returns the name.
	 */
	String getName();
	
	String getNamespace();
	
	/**
	 * @return Returns the type.
	 */
	String getType();
	
	Boolean isNamed();
	
	/**
	 * @return Returns the maxLength.
	 */
	String getMaxLength();
		
	/**
	 * @return Returns the enumList.
	 */
	List getEnumList();
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	String getApplicationName();

}
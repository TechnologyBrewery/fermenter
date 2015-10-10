package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

public interface Enumeration {
		
	public static final String TYPE_NAMED_ENUMERATION = "named";
	public static final String TYPE_VALUED_ENUMERATION = "valued";
	
	/**
	 * @return Returns the name.
	 */
	public String getName() ;
	
	/**
	 * @return Returns the type.
	 */
	public String getType();
	
	public Boolean isNamed();
	
	/**
	 * @return Returns the maxLength.
	 */
	public String getMaxLength();
		
	/**
	 * @return Returns the enumList.
	 */
	public List getEnumList();
	
	/**
	 * Returns the name of the application from which this element originates
	 * @return Application name
	 */
	public String getApplicationName();

}
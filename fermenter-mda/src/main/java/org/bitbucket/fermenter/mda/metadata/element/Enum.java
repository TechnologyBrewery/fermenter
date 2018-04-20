package org.bitbucket.fermenter.mda.metadata.element;

@Deprecated
public interface Enum {

	/**
	 * @return Returns the name.
	 */
	public String getName();
	
	/**
	 * @return Returns the value.
	 */
	public String getValue();
	
	public boolean hasValue();

}
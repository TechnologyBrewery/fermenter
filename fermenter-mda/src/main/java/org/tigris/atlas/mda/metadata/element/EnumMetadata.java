package org.tigris.atlas.mda.metadata.element;

import org.apache.commons.lang.StringUtils;

public class EnumMetadata extends MetadataElement implements Enum {

	private String name;
	private String value;	
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean hasValue() {
		return (StringUtils.isBlank(value));
	}
	
	/**
	 * Ensures that the correct type combinations have been specified
	 */	
	public void validate() {
		
	}

}

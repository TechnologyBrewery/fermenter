package org.bitbucket.fermenter.mda.metadata.element;

import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of the Enum metadata concept.
 */
public class EnumMetadata extends MetadataElement implements Enum {

	private String name;
	private String value;	
	
	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasValue() {
		return (StringUtils.isNotBlank(value));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate() {
		
	}

}

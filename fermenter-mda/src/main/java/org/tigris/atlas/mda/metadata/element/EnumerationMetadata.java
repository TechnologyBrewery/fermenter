package org.tigris.atlas.mda.metadata.element;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.mda.metadata.MetadataManager;

public class EnumerationMetadata extends MetadataElement implements Enumeration {

	private String name;
	private String applicationName;
	private String type;
	private String maxLength;
	private List enumList;
	
	private static Log log = LogFactory.getLog(EnumerationMetadata.class);
	
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

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return (type != null) ? type : TYPE_NAMED_ENUMERATION;
	}	
	
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public Boolean isNamed() {
		return new Boolean(TYPE_NAMED_ENUMERATION.equals(getType()));
	}
	
	/**
	 * @return Returns the maxLength.
	 */
	public String getMaxLength() {
		//if named, then determine the max length programmatically:
		if ((isNamed().booleanValue()) && (maxLength == null)) {
			List enumList = getEnumList();
			Enum e;
			String name;
			int currentLength;
			int maxiumLength = 0;
			Iterator i = enumList.iterator();
			while (i.hasNext()) {
				e = (Enum)i.next();
				name = e.getName();
				currentLength = name.length();
				if (currentLength > maxiumLength) {
					maxiumLength = currentLength;
				}
			}
			
			maxLength = Integer.toString(maxiumLength);
		}
		return maxLength;
	}
	
	/**
	 * @param maxLength The maxLength to set.
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	
	public void addEnum(Enum enumInstance) {
		if (enumList == null) {
			enumList = new ArrayList();
		}
		
		enumList.add(enumInstance);

	}
		
	/**
	 * @return Returns the enumList.
	 */
	public List getEnumList() {
		return (enumList != null) ? enumList : Collections.EMPTY_LIST;
	}

	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	public void validate() {
		validateName();
		validateType();		
		MetadataManager.validateElements(getEnumList());
	}
	
	
	/**
	 * Ensures that name is correctly defined
	 */
	private void validateName() {
		if (StringUtils.isEmpty(getName())) {
			log.error("enumerations must specify a name!");
		}
	}
	
	/**
	 * Ensures that the correct type combinations have been specified
	 */
	private void validateType() {
		String type = getType();
		if (StringUtils.isNotBlank(type)) {
			Collection enumInstanceCollection = getEnumList();
			Enum e;
			Iterator i = enumInstanceCollection.iterator();
			if (TYPE_VALUED_ENUMERATION.equals(type)) {
				while (i.hasNext()) {
					e = (Enum)i.next();
					if (!e.hasValue()) {
						log.error("enum '" + e.getName() + "' must specify a value!");
					}
				}
				
			}  else if (TYPE_VALUED_ENUMERATION.equals(type)) {
				while (i.hasNext()) {
					e = (Enum)i.next();
					if (e.hasValue()) {
						log.error("enum '" + e.getName() + "' must not specify a value!");
					}
				}
				
			}
			
		} else {
			log.error("enumeration must be '" + TYPE_NAMED_ENUMERATION + "' or '" + TYPE_VALUED_ENUMERATION + "'!");
			
		}
	}
	
}

package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

public interface Enumeration extends NamespacedMetamodel {
		
    Integer getMaxLength();
    
	/**
	 * Returns the enum instances within this enumeration.
	 * @return enums
	 */
	List<Enum> getEnums();

}
package org.tigris.atlas.mda.element.java;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.element.Enum;

public class JavaEnum implements Enum {
	
	private Enum enumInstance;
	
	/**
	 * Create a new instance of <tt>Enum</tt> with the correct functionality set 
	 * to generate Java code
	 * @param enumInstanceToDecorate The <tt>Enum</tt> to decorate
	 */
	public JavaEnum(Enum enumInstanceToDecorate) {
		if (enumInstanceToDecorate == null) {
			throw new IllegalArgumentException("JavaEnums must be instatiated with a non-null enum!");
		}
		enumInstance = enumInstanceToDecorate;
	}	

	public String getName() {
		return enumInstance.getName();
	}

	public String getValue() {
		return enumInstance.getValue();
	}

	public boolean hasValue() {
		return enumInstance.hasValue();
	}
	
	//java specific generation methods:
	
	/**
	 * @return Returns the uppercased name.
	 */
	public String getUppercasedName() {
		String name = getName();
		
		if( StringUtils.contains( name, '(' ) ) {
			name = StringUtils.remove( name, '(' );
		}
		if( StringUtils.contains( name, ')' ) ) {
			name = StringUtils.remove( name, ')' );
		}
		if( StringUtils.contains( name, '-' ) ) {
			name = name.replace( '-', '_' );
		}
		if( StringUtils.contains( name, '\'' ) ) {
			name = name.replace( '\'', '_' );
		}
		if( StringUtils.contains( name, ' ' ) ) {
			name = name.replace( ' ', '_' );
		}
		
		return StringUtils.upperCase( name );
	}	

}

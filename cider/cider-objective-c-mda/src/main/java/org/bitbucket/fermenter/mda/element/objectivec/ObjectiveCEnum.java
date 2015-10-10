package org.bitbucket.fermenter.mda.element.objectivec;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Enum;

public class ObjectiveCEnum implements Enum {

	private Enum enumInstance;

	/**
	 * Create a new instance of <tt>Enum</tt> with the correct functionality set
	 * to generate Objective-C code
	 * @param enumInstanceToDecorate The <tt>Enum</tt> to decorate
	 */
	public ObjectiveCEnum(Enum enumInstanceToDecorate) {
		if (enumInstanceToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCEnums must be instatiated with a non-null enum!");
		}
		enumInstance = enumInstanceToDecorate;
	}

	@Override
	public String getName() {
		return enumInstance.getName();
	}

	@Override
	public String getValue() {
		return enumInstance.getValue();
	}

	@Override
	public boolean hasValue() {
		return enumInstance.hasValue();
	}

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

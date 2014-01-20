package org.tigris.atlas.mda.element.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Enum;
import org.tigris.atlas.mda.metadata.element.Enumeration;

/**
 *
 */
public class JavaEnumeration implements Enumeration {
	
	private Enumeration enumeration;
	private List decoratedEnumList;
	
	/**
	 * Create a new instance of <tt>Enumeration</tt> with the correct functionality set 
	 * to generate Java code
	 * @param enumerationToDecorate The <tt>Enumeration</tt> to decorate
	 */
	public JavaEnumeration(Enumeration enumerationToDecorate) {
		if (enumerationToDecorate == null) {
			throw new IllegalArgumentException("JavaEnumerations must be instatiated with a non-null enumeration!");
		}
		enumeration = enumerationToDecorate;
	}	

	/**
	 * @see org.tigris.atlas.mda.element.Enumeration#getName()
	 */
	public String getName() {
		return enumeration.getName();
	}

	/**
	 * @see org.tigris.atlas.mda.metadata.element.Composite#getApplicationName()
	 */
	public String getApplicationName() {
		return enumeration.getApplicationName();
	}
	
	/**
	 * @see org.tigris.atlas.mda.element.Enumeration#getType()
	 */
	public String getType() {
		return enumeration.getType();
	}

	/**
	 * @see org.tigris.atlas.mda.element.Enumeration#isNamed()
	 */
	public Boolean isNamed() {
		return enumeration.isNamed();
	}
	
	public String getColumnType() {
		return isNamed().booleanValue() ? "String" : "Integer";
	}

	/**
	 * @see org.tigris.atlas.mda.element.Enumeration#getMaxLength()
	 */
	public String getMaxLength() {
		return enumeration.getMaxLength();
	}

	/**
	 * @see org.tigris.atlas.mda.element.Enumeration#getEnumList()
	 */
	public List getEnumList() {
		if (decoratedEnumList == null) {
			List enumerationEnumList = enumeration.getEnumList();
			if ((enumerationEnumList == null) || (enumerationEnumList.size() == 0)) {
				decoratedEnumList = Collections.EMPTY_LIST;
				
			} else {
				Enum e;
				decoratedEnumList = new ArrayList((int)(enumerationEnumList.size()));
				Iterator i = enumerationEnumList.iterator();
				while (i.hasNext()) {
					e = (Enum)i.next();
					decoratedEnumList.add(new JavaEnum(e));
					
				}
				
			}
		}
		
		return decoratedEnumList;
	}

	public String getUncapitalizedName() {
		return StringUtils.uncapitalize( getName() );
	}
	
	public String getImport() {
		if (StringUtils.isBlank(getApplicationName())) {
			return JavaElementUtils.getJavaImportType(MetadataRepository.getInstance().getApplicationName(), getName());
		} else {
			return JavaElementUtils.getJavaImportType(getApplicationName(), getName());
		}
	}
	
}

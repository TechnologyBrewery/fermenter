package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Enum;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.mda.metamodel.MetadataRepositoryManager;

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

	@Override
    public String getNamespace() {
        return enumeration.getNamespace();
    }



    /**
	 * @see org.bitbucket.fermenter.stout.mda.element.Enumeration#getName()
	 */
	public String getName() {
		return enumeration.getName();
	}

	/**
	 * @see org.bitbucket.fermenter.stout.mda.metadata.element.Composite#getApplicationName()
	 */
	public String getApplicationName() {
		return enumeration.getApplicationName();
	}
	
	/**
	 * @see org.bitbucket.fermenter.stout.mda.element.Enumeration#getType()
	 */
	public String getType() {
		return enumeration.getType();
	}

	/**
	 * @see org.bitbucket.fermenter.stout.mda.element.Enumeration#isNamed()
	 */
	public Boolean isNamed() {
		return enumeration.isNamed();
	}
	
	public String getColumnType() {
		return isNamed().booleanValue() ? "String" : "Integer";
	}

	/**
	 * @see org.bitbucket.fermenter.stout.mda.element.Enumeration#getMaxLength()
	 */
	public String getMaxLength() {
		return enumeration.getMaxLength();
	}

	/**
	 * @see org.bitbucket.fermenter.stout.mda.element.Enumeration#getEnumList()
	 */
	public List<Enum> getEnumList() {
		if (decoratedEnumList == null) {
			List<Enum> enumerationEnumList = enumeration.getEnumList();
			if ((enumerationEnumList == null) || (enumerationEnumList.size() == 0)) {
				decoratedEnumList = Collections.EMPTY_LIST;
				
			} else {
				Enum e;
				decoratedEnumList = new ArrayList<>((int)(enumerationEnumList.size()));
				Iterator<Enum> i = enumerationEnumList.iterator();
				while (i.hasNext()) {
					e = i.next();
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
			MetadataRepository metadataRepository = 
	                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			return JavaElementUtils.getJavaImportType(metadataRepository.getApplicationName(), getName());
		} else {
			return JavaElementUtils.getJavaImportType(getApplicationName(), getName());
		}
	}
	
}

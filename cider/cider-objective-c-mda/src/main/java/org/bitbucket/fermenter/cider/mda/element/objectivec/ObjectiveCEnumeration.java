package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.cider.mda.objectivec.ObjectiveCTypeManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Enum;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;

/**
 *
 */
public class ObjectiveCEnumeration implements Enumeration {

	private Enumeration enumeration;
	private List decoratedEnumList;

	/**
	 * Create a new instance of <tt>Enumeration</tt> with the correct functionality set
	 * to generate Objective-C code
	 * @param enumerationToDecorate The <tt>Enumeration</tt> to decorate
	 */
	public ObjectiveCEnumeration(Enumeration enumerationToDecorate) {
		if (enumerationToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCEnumerations must be instatiated with a non-null enumeration!");
		}
		enumeration = enumerationToDecorate;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.element.Enumeration#getName()
	 */
	@Override
	public String getName() {
		return ObjectiveCTypeManager.getObjectiveCClassPrefix() + enumeration.getName();
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Composite#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return enumeration.getApplicationName();
	}

	/**
	 * @see org.bitbucket.fermenter.mda.element.Enumeration#getType()
	 */
	@Override
	public String getType() {
		return enumeration.getType();
	}

	/**
	 * @see org.bitbucket.fermenter.mda.element.Enumeration#isNamed()
	 */
	@Override
	public Boolean isNamed() {
		return enumeration.isNamed();
	}

	public String getColumnType() {
		return isNamed().booleanValue() ? "String" : "Integer";
	}

	/**
	 * @see org.bitbucket.fermenter.mda.element.Enumeration#getMaxLength()
	 */
	@Override
	public String getMaxLength() {
		return enumeration.getMaxLength();
	}

	/**
	 * @see org.bitbucket.fermenter.mda.element.Enumeration#getEnumList()
	 */
	@Override
	public List getEnumList() {
		if (decoratedEnumList == null) {
			List enumerationEnumList = enumeration.getEnumList();
			if ((enumerationEnumList == null) || (enumerationEnumList.size() == 0)) {
				decoratedEnumList = Collections.EMPTY_LIST;

			} else {
				Enum e;
				decoratedEnumList = new ArrayList((enumerationEnumList.size()));
				Iterator i = enumerationEnumList.iterator();
				while (i.hasNext()) {
					e = (Enum)i.next();
					decoratedEnumList.add(new ObjectiveCEnum(e));

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
			MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			return ObjectiveCElementUtils.getObjectiveCImportType(repo.getApplicationName(), getName());
		} else {
			return ObjectiveCElementUtils.getObjectiveCImportType(getApplicationName(), getName());
		}
	}

}

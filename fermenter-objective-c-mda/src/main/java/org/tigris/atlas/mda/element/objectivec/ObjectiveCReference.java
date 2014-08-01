package org.tigris.atlas.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.PackageManager;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Reference;

public class ObjectiveCReference implements Reference {

	private Reference reference;
	private List decoratedFoerignKeyFieldList;
	private String importName;

	/**
	 * Create a new instance of <tt>Reference</tt> with the correct functionality set
	 * to generate Objective-C code
	 * @param referenceToDecorate The <tt>Reference</tt> to decorate
	 */
	public ObjectiveCReference(Reference referenceToDecorate) {
		if (referenceToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCReferences must be instatiated with a non-null reference!");
		}
		reference = referenceToDecorate;
	}

	@Override
	public String getType() {
		return ObjectiveCElementUtils.getObjectiveCType(MetadataRepository.getInstance().getApplicationName(), reference.getType());
	}

	public String getTypeAttributes() {
		return "nonatomic, strong";
	}

	@Override
	public String getLabel() {
		return reference.getLabel();
	}

	@Override
	public String getName() {
		return reference.getName();
	}

	@Override
	public String getDocumentation() {
		return reference.getDocumentation();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(reference.getName());
	}

	@Override
	public List getForeignKeyFields() {
		if (decoratedFoerignKeyFieldList == null) {
			List referenceForeignKeyFieldList = reference.getForeignKeyFields();
			if ((referenceForeignKeyFieldList == null) || (referenceForeignKeyFieldList.size() == 0)) {
				decoratedFoerignKeyFieldList = Collections.EMPTY_LIST;

			} else {
				Field f;
				decoratedFoerignKeyFieldList = new ArrayList((referenceForeignKeyFieldList.size()));
				Iterator i = referenceForeignKeyFieldList.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedFoerignKeyFieldList.add(new ObjectiveCField(f));

				}

			}
		}

		return decoratedFoerignKeyFieldList;
	}

	@Override
	public String getRequired() {
		return reference.getRequired();
	}

	@Override
	public boolean isRequired() {
		return reference.isRequired();
	}

	@Override
	public String getProject() {
		return reference.getProject();
	}

	public boolean isExternal() {
		String currentProject = MetadataRepository.getInstance().getApplicationName();
		return !StringUtils.isBlank(getProject()) && !getProject().equals(currentProject);
	}

	public String getImportPrefix() {
		return PackageManager.getBasePackage( getProject() );
	}

	public String getImport() {
		if (importName == null) {
			String appName = isExternal() ? getProject() : MetadataRepository.getInstance().getApplicationName();
			importName = ObjectiveCElementUtils.getObjectiveCImport(appName, getType());
		}

		return importName;
	}

	/**
	 *
	 *
	 * @return
	 */
	public Set<String> getFkImports() {
		Set importSet = new HashSet( );

		if( ! isExternal() ) {
			return importSet;
		}

		ObjectiveCField fk;
		Iterator fks = getForeignKeyFields().iterator();
		while (fks.hasNext()) {
			fk = (ObjectiveCField) fks.next();
			importSet.add(fk.getImport());
		}

		return importSet;
	}


	public String getFkCondition() {
		if( ! isExternal() ) {
			return null;
		}

		StringBuffer sb = new StringBuffer(100);
		ObjectiveCField fk;
		Iterator fks = getForeignKeyFields().iterator();
		while (fks.hasNext()) {
			fk = (ObjectiveCField) fks.next();
			sb.append( "get" );
			sb.append( getCapitalizedName() );
			sb.append( fk.getCapitalizedName() );
			sb.append( "() == null" );
			if( fks.hasNext() ) {
				sb.append( "&&" );
			}
		}

		return sb.toString();
	}

	public String getUncapitalizedType() {
		return StringUtils.uncapitalize( getType() );
	}

}

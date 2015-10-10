package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Reference;

public class ObjectiveCReference implements Reference {

	private Reference reference;
	private List<ObjectiveCField> decoratedFoerignKeyFieldList;
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

	public String getWrappedType() {
		return reference.getType();
	}

	public String getUncapitalizedWrappedType() {
		return StringUtils.uncapitalize(getWrappedType());
	}

	public Entity getTypeEntity() {
		return ObjectiveCElementUtils.getObjectiveCEntity(reference.getType());
	}

	@Override
	public String getLabel() {
		return reference.getLabel();
	}

	@Override
	public String getName() {
		return reference.getName();
	}

	public String getSerializedName() {
		return getName();
	}

	@Override
	public String getDocumentation() {
		return reference.getDocumentation();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(reference.getName());
	}

	@Override
	public List<ObjectiveCField> getForeignKeyFields() {
		if (decoratedFoerignKeyFieldList == null) {
			@SuppressWarnings("unchecked")
			List<Field> referenceForeignKeyFieldList = reference.getForeignKeyFields();
			if ((referenceForeignKeyFieldList == null) || (referenceForeignKeyFieldList.size() == 0)) {
				decoratedFoerignKeyFieldList = Collections.<ObjectiveCField>emptyList();
			}
			else {
				decoratedFoerignKeyFieldList = new ArrayList<ObjectiveCField>(referenceForeignKeyFieldList.size());
				for (Field f : referenceForeignKeyFieldList) {
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
		Set<String> importSet = new HashSet<String>();

		if (!isExternal()) {
			return importSet;
		}

		for (ObjectiveCField fk : getForeignKeyFields()) {
			importSet.add(fk.getImport());
		}

		return importSet;
	}


	public String getFkCondition() {
		if (!isExternal()) {
			return null;
		}

		StringBuffer sb = new StringBuffer(100);
		Iterator<ObjectiveCField> fks = getForeignKeyFields().iterator();
		while (fks.hasNext()) {
			ObjectiveCField fk = fks.next();
			sb.append("get");
			sb.append(getCapitalizedName());
			sb.append(fk.getCapitalizedName());
			sb.append("() == null");
			if (fks.hasNext()) {
				sb.append("&&");
			}
		}

		return sb.toString();
	}

	public String getUncapitalizedType() {
		return StringUtils.uncapitalize( getType() );
	}

}

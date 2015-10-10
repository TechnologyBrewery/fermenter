package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.FormEntity;
import org.bitbucket.fermenter.mda.metadata.element.FormFieldMetadata;

public class ObjectiveCFormEntity implements FormEntity {

	private FormEntity entity;
	private Map fields;
	//private List composites;

	public ObjectiveCFormEntity(FormEntity entity) {
		super();
		this.entity = entity;
	}

	@Override
	public String getProject() {
		return entity.getProject();
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public String getType() {
		return entity.getType();
	}

	@Override
	public Collection getFields() {
		if (fields == null) {
			fields = new HashMap();

			for (Iterator i = entity.getFields().iterator(); i.hasNext();) {
				Field field = (Field) i.next();
				ObjectiveCField jField = new ObjectiveCFormField((FormFieldMetadata) field);
				fields.put(jField.getName(), jField);
			}

		}

		return Collections.unmodifiableCollection(fields.values());
	}

	public String getImport() {
		StringBuffer buff = new StringBuffer();
		String base = null;
		if (StringUtils.isBlank(getProject())) {
			String prj = MetadataRepository.getInstance().getApplicationName();
			base = PackageManager.getBasePackage(prj);
		} else {
			base = PackageManager.getBasePackage(getProject());
		}
		buff.append(base);
		buff.append(".transfer.");
		buff.append(getType());
		return buff.toString();
	}

	public Set getFieldImports() {
		Set imports = new HashSet();

		for (Iterator i = getFields().iterator(); i.hasNext();) {
			Field field = (Field) i.next();
			if (field.isExternal()) {
				imports.add(ObjectiveCElementUtils.getObjectiveCImportType(field.getProject(), field.getType()));
			} else {
				imports.add(ObjectiveCElementUtils.getObjectiveCImportType(getProject(), field.getType()));
			}
		}

		return imports;
	}

	public String getUncapitalizedName() {
		return StringUtils.uncapitalize( getName() );
	}

	/*public Collection getComposites() {
		if (composites == null) {
			composites = new ArrayList();
			for (Iterator i = entity.getComposites().iterator(); i.hasNext();) {
				Composite c = (Composite) i.next();
				composites.add(new ObjectiveCComposite(c));
			}
		}
		return composites;
	}*/

}

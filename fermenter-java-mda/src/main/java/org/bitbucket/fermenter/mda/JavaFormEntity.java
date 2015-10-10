package org.bitbucket.fermenter.mda;

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

public class JavaFormEntity implements FormEntity {

	private FormEntity entity;
	private Map fields;
	//private List composites;
	
	public JavaFormEntity(FormEntity entity) {
		super();
		this.entity = entity;
	}
	
	public String getProject() {
		return entity.getProject();
	}
	
	public String getName() {
		return entity.getName();
	}
	
	public String getType() {
		return entity.getType();
	}
	
	public Collection getFields() {
		if (fields == null) {
			fields = new HashMap();
			
			for (Iterator i = entity.getFields().iterator(); i.hasNext();) {
				Field field = (Field) i.next();
				JavaField jField = new JavaFormField((FormFieldMetadata) field);
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
				imports.add(JavaElementUtils.getJavaImportType(field.getProject(), field.getType()));
			} else {
				imports.add(JavaElementUtils.getJavaImportType(getProject(), field.getType()));	
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
				composites.add(new JavaComposite(c));
			}
		}
		return composites;
	}*/

}

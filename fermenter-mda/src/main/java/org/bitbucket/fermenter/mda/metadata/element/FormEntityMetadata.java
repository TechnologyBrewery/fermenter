package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;

public class FormEntityMetadata extends MetadataElement implements FormEntity {

	private String project;
	private String name;
	private String type;
	private Map fields = new HashMap();
	private List composites = new ArrayList();

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public Collection getFields() {
		return Collections.unmodifiableCollection(fields.values());
	}
	
	public void addField(FormFieldMetadata field) {
		fields.put(field.getName(), field);
	}
	
	public Field getField(String name) {
		return (Field) fields.get(name);
	}

	public String getName() {
		return ( name == null ) ? getType() : name;
	}

	public void setName(String name) {
		this.name = StringUtils.capitalize(name);
	}

	public void setFields(Map fields) {
		this.fields = fields;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void validate() {
		for (Iterator i= composites.iterator(); i.hasNext();) {
			validateComposite((CompositeInstanceMetadata) i.next());
		}
	}
	
	public void addComposite(CompositeInstanceMetadata composite) {
		composites.add(composite);
	}
	
	private void validateComposite(CompositeInstanceMetadata composite) {
		// Look up the composite and ensure that the project is set correctly
		Entity entity = MetadataRepository.getInstance().getEntity(getProject(), getType());
		Map compositeMap =  MetadataRepository.getInstance().getAllComposites();
		Composite compositeDef = entity.getComposite(composite.getName());
		CompositeInstanceMetadata instance = new CompositeInstanceMetadata();
		copy(compositeDef, instance);
		instance.linkToBaseComposite(compositeMap);
		instance.setProject(compositeDef.getProject() == null ? getProject() : compositeDef.getProject());
		
		// Add each composite field as a form field
		for (Iterator i = instance.getFields().iterator(); i.hasNext();) {
			Field field = (Field) i.next();
			FormFieldMetadata formField = new FormFieldMetadata();
			copy(field, formField);
			formField.setBaseFieldName(formField.getName());
			formField.setName(StringUtils.uncapitalize(composite.getName()) + StringUtils.capitalize(formField.getName()));
			formField.setCompositeName(instance.getName());
			addField(formField);
		}
	}
	
	private void copy(Object src, Object dest) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
}

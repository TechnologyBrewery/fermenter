package org.tigris.atlas.mda.metadata.element;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.MetadataRepository;

public class CompositeInstanceMetadata extends MetadataElement implements Composite {

	private Composite referencedComposite;
	
	private String label;
	private String applicationName;
	private String name;
	private String prefix;
	private String project;
	private String type;
	
	private Map fields;
	
	public CompositeInstanceMetadata() {
		super();
	}

	public String getLabel() {
		return StringUtils.isBlank(label) ? referencedComposite.getLabel() : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return StringUtils.isBlank(applicationName) ? referencedComposite.getApplicationName() : applicationName;
	}

	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getName() {
		return StringUtils.isBlank(name) ? CompositeMetadata.getDefaultName(getType()) : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getProject() {
		return StringUtils.isBlank(project) ? referencedComposite.getProject() : project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;		
		
	}
	
	public Collection getFields() {
		if (fields == null) {
			fields = new HashMap();
			
			Composite c = referencedComposite;
			
			Collection coll = c.getFields();
			for (Iterator i = coll.iterator(); i.hasNext();) {
				FieldMetadata oldField = (FieldMetadata) i.next();
				
				FieldMetadata newField = new FieldMetadata();
				//Need to copy over field attrs
				try {
					BeanUtils.copyProperties(newField, oldField);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				if (!StringUtils.isBlank(getPrefix())) {
					newField.setColumn(getPrefix() + oldField.getColumn());
				}

				fields.put(newField.getName(), newField);
			}
		}
		
		return fields.values();
	}

	public void validate() {
		for (Iterator i = getFields().iterator(); i.hasNext();) {
			FieldMetadata f = (FieldMetadata) i.next();
			f.validate();
		}
	}
	
	/**
	 * Links this instance of a composite to the base composite object to allow for pass-through
	 * retrieval of data on the composite interface that cannot be set from within an entity
	 * @param compositeMap
	 */
	public void linkToBaseComposite(Map compositeMap) {
		referencedComposite = (Composite)compositeMap.get(type);
		
	}

}

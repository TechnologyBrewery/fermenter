package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

public class CompositeMetadata extends MetadataElement implements Composite {

	private String name;
	private String applicationName;
	private String prefix;
	private String project;
	private String type;
	private String label;
	private List fields;

	public CompositeMetadata() {
		super();
		
		fields = new ArrayList();
	}
	
	public void addField(Field f) {
		fields.add(f);
	}
	
	public Collection getFields() {
		return Collections.unmodifiableList(fields);
	}

	public String getName() {
		return StringUtils.isBlank(name) ? getDefaultName(getType()) : name;
	}
	
	public void setName(String value) {
		name = value;
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	public void validate() {
		for (Iterator i = getFields().iterator(); i.hasNext();) {
			FieldMetadata f = (FieldMetadata) i.next();
			f.validate();
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getProject() {
	    MetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return (StringUtils.isNotBlank(project)) ? project : metadataRepository.getApplicationName();
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	static String getDefaultName(String type) {
		return StringUtils.uncapitalize(type);
	}

}

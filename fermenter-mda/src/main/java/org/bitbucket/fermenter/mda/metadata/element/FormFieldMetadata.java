package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FormFieldMetadata extends FieldMetadata {

	private String project;
	private List enumerationLabels;
	private String compositeName;
	private String baseFieldName;

	public String getBaseFieldName() {
		return StringUtils.isBlank(baseFieldName) ? getName() : baseFieldName;
	}

	public void setBaseFieldName(String baseFieldName) {
		this.baseFieldName = baseFieldName;
	}

	public FormFieldMetadata() {
		super();
		enumerationLabels = new ArrayList();
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public List getEnumerationLabels() {
		return Collections.unmodifiableList(enumerationLabels);
	}
	
	public void addEnumerationLabel(EnumerationLabel el) {
		enumerationLabels.add(el);
	}

	public String getCompositeName() {
		return compositeName;
	}

	public void setCompositeName(String compositeName) {
		this.compositeName = compositeName;
	}
	
}

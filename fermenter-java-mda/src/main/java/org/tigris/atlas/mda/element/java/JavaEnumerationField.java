package org.tigris.atlas.mda.element.java;

import java.util.Collection;

import org.tigris.atlas.mda.metadata.element.FormFieldMetadata;


public class JavaEnumerationField {

	private JavaFormEntity entity;
	private JavaField field;
	
	public JavaFormEntity getEntity() {
		return entity;
	}

	public void setEntity(JavaFormEntity entity) {
		this.entity = entity;
	}

	public JavaField getField() {
		return field;
	}

	public void setField(JavaField field) {
		this.field = field;
	}

	public JavaEnumerationField(JavaFormEntity entity, JavaField field) {
		super();
		this.entity = entity;
		this.field = field;
	}
	
	public Collection getEnumerationLabels() {
		return ((FormFieldMetadata)field.getFieldObject()).getEnumerationLabels();
	}
	
}

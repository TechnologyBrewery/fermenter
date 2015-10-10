package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.Collection;

import org.bitbucket.fermenter.mda.metadata.element.FormFieldMetadata;


public class ObjectiveCEnumerationField {

	private ObjectiveCFormEntity entity;
	private ObjectiveCField field;

	public ObjectiveCFormEntity getEntity() {
		return entity;
	}

	public void setEntity(ObjectiveCFormEntity entity) {
		this.entity = entity;
	}

	public ObjectiveCField getField() {
		return field;
	}

	public void setField(ObjectiveCField field) {
		this.field = field;
	}

	public ObjectiveCEnumerationField(ObjectiveCFormEntity entity, ObjectiveCField field) {
		super();
		this.entity = entity;
		this.field = field;
	}

	public Collection getEnumerationLabels() {
		return ((FormFieldMetadata)field.getFieldObject()).getEnumerationLabels();
	}

}

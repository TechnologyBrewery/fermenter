package org.bitbucket.fermenter.cider.mda.element.objectivec;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.FormFieldMetadata;

public class ObjectiveCFormField extends ObjectiveCField {

	private FormFieldMetadata metadata;

	public ObjectiveCFormField(FormFieldMetadata field) {
		super(field);

		metadata = field;
	}

	public String getCompositeName() {
		return StringUtils.capitalize(metadata.getCompositeName());
	}

	public String getBaseFieldName() {
		return metadata.getBaseFieldName();
	}

	public String getCapitalizedBaseFieldName() {
		return StringUtils.capitalize(getBaseFieldName());
	}

}

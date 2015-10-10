package org.bitbucket.fermenter.mda;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.FormFieldMetadata;

public class JavaFormField extends JavaField {

	private FormFieldMetadata metadata;
	
	public JavaFormField(FormFieldMetadata field) {
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

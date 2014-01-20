package org.tigris.atlas.mda.element.java;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.FormatMetadataManager;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Enumeration;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Format;
import org.tigris.atlas.mda.metadata.element.Pattern;

public class JavaField implements Field {
	
	private Field field;
	private String importName;
	
	/**
	 * Create a new instance of <tt>Field</tt> with the correct functionality set 
	 * to generate Java code
	 * @param fieldToDecorate The <tt>Field</tt> to decorate
	 */
	public JavaField(Field fieldToDecorate) {
		if (fieldToDecorate == null) {
			throw new IllegalArgumentException("JavaFields must be instantiated with a non-null field!");
		}
		field = fieldToDecorate;
	}

	public String getColumn() {
		return field.getColumn();
	}

	public String getGenerator() {
		return field.getGenerator();
	}

	public String getLabel() {
		return field.getLabel();
	}

	public String getName() {
		return field.getName();
	}
	
	public String getDocumentation() {
		return field.getDocumentation();
	}

	public String getType() {
		return field.getType();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(getName());
	}

	public String getUppercasedName() {
		return field.getName().toUpperCase();
	}

	public String getUppercasedType() {
		return field.getType().toUpperCase();
	}

	public Boolean isSimpleType() {
		return field.isSimpleType();
	}

	public Boolean isEnumerationType() {
		return field.isEnumerationType();
	}

	public Enumeration getEnumeration() {
		Enumeration e = field.getEnumeration();
		return (e != null) ? new JavaEnumeration(e) : null;
	}

	public String getMaxLength() {
		return field.getMaxLength();
	}

	public boolean hasMaxLength() {
		return field.hasMaxLength();
	}

	public String getMinLength() {
		return field.getMinLength();
	}

	public boolean hasMinLength() {
		return field.hasMinLength();
	}

	public String getMaxValue() {
		return field.getMaxValue();
	}

	public boolean hasMaxValue() {
		return field.hasMaxValue();
	}

	public String getMinValue() {
		return field.getMinValue();
	}

	public boolean hasMinValue() {
		return field.hasMinValue();
	}

	public String getRequired() {
		return field.getRequired();
	}

	public boolean isRequired() {
		return field.isRequired();
	}

	public String getSourceName() {
		return field.getSourceName();
	}
	
	//java-specific generation methods:
	
	public String getJavaType() {
		return JavaElementUtils.getJavaType(MetadataRepository.getInstance().getApplicationName(), getType());
	}
	
	public String getImport() {
		if (importName == null ) {
			if (isExternal()) {
				importName = JavaElementUtils.getJavaImportType(getProject(), getType());
			} else {
				importName = JavaElementUtils.getJavaImportType(MetadataRepository.getInstance().getApplicationName(), getType());	
			}
		}
		
		return importName;
	}
	
	Field getFieldObject() {
		return field;
	}

	public String getScale() {
		return field.getScale();
	}

	public boolean hasScale() {
		return field.hasScale();
	}

	public String getProject() {
		return field.getProject();
	}

	public boolean isExternal() {
		return field.isExternal();
	}
	
	public String getFormat() {
		return field.getFormat();
	}
	
	public boolean hasFormat() {
		return field.hasFormat();
	}
	
	public String getPatterns() {
		Format format = FormatMetadataManager.getInstance().getFormat(getFormat());
		
		StringBuffer buff = new StringBuffer(100);
		for (Iterator i = format.getPatterns().iterator(); i.hasNext();) {
			Pattern pattern = (Pattern) i.next();
			
			buff.append("\"");
			buff.append(StringEscapeUtils.escapeJava(pattern.getText()));
			buff.append("\"");
			
			if (i.hasNext()) {
				buff.append(", ");
			}
		}
		
		return buff.toString();
	}

}

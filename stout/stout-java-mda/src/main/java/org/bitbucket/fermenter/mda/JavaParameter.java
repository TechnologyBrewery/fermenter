package org.bitbucket.fermenter.mda;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.mda.metadata.element.Parameter;

public class JavaParameter implements Parameter {

	private Parameter parameter;
	private String importName;
	private String javaType;
	private String uncapitalizedJavaType;
	private String basePackage;
	private String signatureName;
	private String signatureSuffix;

	public JavaParameter(Parameter parameterToDecorate) {
		parameter = parameterToDecorate;
	}

	public String getName() {
		return parameter.getName();
	}
	
	public String getDocumentation() {
		return parameter.getDocumentation();
	}

	public String getType() {
		return parameter.getType();
	}

	public String getProject() {
		return parameter.getProject();
	}

	/**
	 * @deprecated Use getImport() instead
	 */
	public String getJavaImportType() {
		return getImport();
	}

	private String getProjectValue() {
		String project = getProject();
		project = (project != null) ? project : MetadataRepository.getInstance().getApplicationName();
		return project;
	}

	public String getImport() {
		if (importName == null ) {
			importName = JavaElementUtils.getJavaImportType(getProjectValue(), getType());
		}

		return importName;
	}

	public String getJavaType() {
		if (javaType == null) {
			javaType = JavaElementUtils.getJavaType(getProjectValue(), getType());
		}
		return javaType;
	}

	public String getUncapitalizedJavaType() {
		if (uncapitalizedJavaType == null) {
			uncapitalizedJavaType = StringUtils.uncapitalize(getJavaType());
		}
		return uncapitalizedJavaType;
	}

	public boolean isEntity() {
		return MetadataRepository.getInstance().getEntity(getProjectValue(), getType() ) != null;
	}

	public boolean isEnumeration() {
		return MetadataRepository.getInstance().getEnumeration(getProjectValue(), getType() ) != null;
	}

	public Enumeration getEnumeration() {
		Enumeration e = MetadataRepository.getInstance().getEnumeration(getProjectValue(), getType());
		return (e != null) ? new JavaEnumeration(e) : null;
	}

	public String getUppercaseName() {
		return StringUtils.capitalize( getName() );
	}

	public String getBasePackage() {
		if (basePackage == null) {
			String projectName = getProjectValue();
			basePackage = PackageManager.getBasePackage(projectName);
		}

		return basePackage;
	}

	/**
	 * The parameter name with a comma, if appropriate
	 */
	public String getSignatureName() {
		if (signatureName != null) {
			StringBuffer sb = new StringBuffer(150);
			sb.append(getName()).append(getSignatureSuffix());
			signatureName = sb.toString();
		}

		return signatureName;
	}

	/**
	 * @return Returns the signatureSuffix.
	 */
	public String getSignatureSuffix() {
		return (signatureSuffix != null) ? signatureSuffix : "";
	}

	/**
	 * @param signatureSuffix The signatureSuffix to set.
	 */
	public void setSignatureSuffix(String signatureSuffix) {
		this.signatureSuffix = signatureSuffix;
	}


	public boolean isMany() {
		return parameter.isMany();
	}
}

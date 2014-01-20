package org.tigris.atlas.mda.element.java;

import java.util.Iterator;
import java.util.List;

import org.tigris.atlas.mda.PackageManager;
import org.tigris.atlas.mda.java.JavaTypeManager;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Enumeration;

public class JavaElementUtils {
	
	static String getJavaImportType(String appName, String type) {
		String javaImportType = null;
		if( "void".equals(type) ) {
			javaImportType = "void";
			
		} else {
			// Attempt to resolve primitive type
			javaImportType = JavaTypeManager.getJavaType( type );
			
			if( javaImportType == null ) {
				// Assume it's an application entity or enumeration at this point
				MetadataRepository repo = MetadataRepository.getInstance();
				Entity e = repo.getEntity(appName, type);
				if (e != null) {					
					javaImportType = createFullyQualifiedName(type, ".transfer.", appName);
				} else {
					Enumeration enumeration = repo.getEnumeration(appName, type);
					if (enumeration != null) {
						javaImportType = createFullyQualifiedName(type, ".enumeration.", appName);
					} else {
						javaImportType = type;
					}
						
				} 
			}
		}
		
		return javaImportType;
	}

	static String createFullyQualifiedName(String type, String nestedPackage) {
		return createFullyQualifiedName(type, nestedPackage, MetadataRepository.getInstance().getApplicationName());
	}
	
	static String createFullyQualifiedName(String type, String nestedPackage, String applicationName) {
		StringBuffer sb = new StringBuffer(100);
		sb.append(PackageManager.getBasePackage(applicationName));
		sb.append(nestedPackage).append(type);
		return sb.toString();
	}
	 
	public static String getJavaType(String appName, String type) {
		String javaType = getJavaImportType(appName, type);
		int index = (javaType != null) ? javaType.lastIndexOf( "." ) : 0;
		if(  index > 0 ) {
			return javaType.substring( index + 1 );
		} else {
			return type;
		}
	}	
	
	static String createSignatureParameters(List parameterList) {
		StringBuffer params = new StringBuffer(100);
		if (parameterList != null) {
			for (Iterator i = parameterList.iterator(); i.hasNext();) {
				JavaParameter param = (JavaParameter) i.next();
				if (param.isMany()) {
					params.append("Collection");
				} else {
					params.append(param.getJavaType());	
				}
				params.append(" ");
				params.append(param.getName());
				if (i.hasNext()) {
					params.append(", ");
				}
			}
		}
		return params.toString();
	}
	
	/**
	 * Returns the fields for a signature definition of a method
	 * @param fieldList a list of <tt>Field</tt> instances
	 * @return A String like: String foo, Integer bar, Obejct blah
	 */
	static String createSignatureFields(List fieldList) {
		StringBuffer fields = new StringBuffer(100);
		if (fieldList != null) {
			for (Iterator i = fieldList.iterator(); i.hasNext();) {
				JavaField field = (JavaField) i.next();
				fields.append(field.getJavaType());
				fields.append(" ");
				fields.append(field.getName());
				if (i.hasNext()) {
					fields.append(", ");
				}
			}
		}
		return fields.toString();
	}	
	
	/**
	 * Returns the fields for a call to a method
	 * @param fieldList a list of <tt>Field</tt> instances
	 * @return A String like: foo, bar, blah.
	 */
	static String createSignatureFieldParams(List fieldList) {
		StringBuffer fields = new StringBuffer(100);
		if (fieldList != null) {
			for (Iterator i = fieldList.iterator(); i.hasNext();) {
				JavaField field = (JavaField) i.next();
				fields.append(field.getName());
				if (i.hasNext()) {
					fields.append(", ");
				}
			}
		}
		return fields.toString();
	}		
	
	
	public static String getBaseJndiName(String basePackage) {
		return (basePackage != null) ? basePackage.replace('.', '/') : "";
	}	
}

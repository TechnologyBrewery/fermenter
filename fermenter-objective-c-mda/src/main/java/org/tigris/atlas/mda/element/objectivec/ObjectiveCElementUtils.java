package org.tigris.atlas.mda.element.objectivec;

import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.tigris.atlas.mda.PackageManager;
import org.tigris.atlas.mda.objectivec.ObjectiveCTypeManager;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Enumeration;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Parameter;

public class ObjectiveCElementUtils {

	static final String VOID = "void";

	/** Needs to be a {@link List} and not {@link Collection} due to JAX-RS parameter requirements. */
	static final String PARAM_COLLECTION_TYPE = "List";

	static String getObjectiveCImport(String appName, String type) {
		String importFile = null;

		String importType = getObjectiveCImportType(appName, type);
		switch (importType) {
			case "void":
			case "long":
			case "int":
			case "char":
			case "BOOL":
			case "NSString":
			case "NSDate":
			case "NSTimeInterval":
			case "NSDecimalNumber":
			case "NSData":
				break;
			default:
				importFile = importType + ".h";
				break;
		}
		return importFile;
	}

	static String getObjectiveCImportType(String appName, String type) {
		String objectiveCImportType = null;
		if (VOID.equals(type)) {
			objectiveCImportType = VOID;
		}
		else {
			// Attempt to resolve primitive type
			objectiveCImportType = ObjectiveCTypeManager.getObjectiveCType(type);

			if (objectiveCImportType == null) {
				// Assume it's an application entity or enumeration at this point
				MetadataRepository repo = MetadataRepository.getInstance();
				Entity e = repo.getEntity(type);
				if (e != null) {
					objectiveCImportType = (new ObjectiveCEntity(e)).getName();
				}
				else {
					Enumeration enumeration = repo.getEnumeration(type);
					if (enumeration != null) {
						enumeration = new ObjectiveCEnumeration(enumeration);
						objectiveCImportType = enumeration.getName();
					}
					else {
						objectiveCImportType = type;
					}
				}
			}
		}

		return objectiveCImportType;
	}

	static String createFullyQualifiedName(String type, String nestedPackage) {
		return createFullyQualifiedName(type, nestedPackage, MetadataRepository.getInstance().getApplicationName());
	}

	static String createFullyQualifiedName(String type, String nestedPackage, String applicationName) {
		StringBuilder sb = new StringBuilder();
		sb.append(PackageManager.getBasePackage(applicationName));
		sb.append(nestedPackage).append(type);
		return sb.toString();
	}

	public static String getObjectiveCType(String appName, String type) {
		return getObjectiveCImportType(appName, type);
	}

	static String createSignatureParameters(List<Parameter> parameterList) {
		return createSignatureParameters(parameterList, null, null);
	}

	static String createSignatureParameters(List<Parameter> parameterList, String fieldNameSuffix, String fieldTypeSuffix) {
		StringBuilder params = new StringBuilder();
		if (parameterList != null) {
			boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
			boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

			for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
				ObjectiveCParameter param = (ObjectiveCParameter)i.next();
				if (param.isMany()) {
					params.append(PARAM_COLLECTION_TYPE + "<").append(param.getObjectiveCType());
					if ((hasFieldTypeSuffix) && (param.isEntity())) {
						params.append(fieldTypeSuffix);
					}
					params.append(">");
				} else {
					params.append(param.getObjectiveCType());
					if ((hasFieldTypeSuffix) && (param.isEntity())) {
						params.append(fieldTypeSuffix);
					}
				}
				params.append(" ");
				params.append(param.getName());
				if ((hasFieldNameSuffix) && (param.isEntity())) {
					params.append(fieldNameSuffix);
				}
				if (i.hasNext()) {
					params.append(", ");
				}
			}
		}
		return params.toString();
	}

	static String createJaxRSSignatureParameters(List<Parameter> parameterList, String fieldNameSuffix, String fieldTypeSuffix) {
		StringBuilder params = new StringBuilder();
		if (parameterList != null) {
			boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
			boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

			for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
				ObjectiveCParameter param = (ObjectiveCParameter)i.next();
				if (param.isMany()) {
					params.append(PARAM_COLLECTION_TYPE + "<").append(param.getObjectiveCType());
					if ((hasFieldTypeSuffix) && (param.isEntity())) {
						params.append(fieldTypeSuffix);
					}
					params.append(">");
				} else {
					params.append(param.getObjectiveCType());
					if ((hasFieldTypeSuffix) && (param.isEntity())) {
						params.append(fieldTypeSuffix);
					}
				}
				params.append(" ");
				params.append(param.getName());
				if ((hasFieldNameSuffix) && (param.isEntity())) {
					params.append(fieldNameSuffix);
				}
				if (i.hasNext()) {
					params.append(", ");
				}
			}
		}
		return params.toString();
	}

	/**
	 * Returns the fields for a signature definition of a method.
	 * @param fieldList a list of {@link Field} instances
	 * @return A String like: String foo, Integer bar, Object blah
	 */
	static String createSignatureFields(List<Field> fieldList) {
		return createSignatureFields(fieldList, null, null);
	}

	/**
	 * Returns the fields for a signature definition of a method. Suffix will only be used if the field references an
	 * entity type.
	 * @param fieldList a list of {@link Field} instances
	 * @param fieldNameSuffix a suffix to add to the end of each field name, if it needs to be altered
	 * @param fieldTypeSuffix a suffix to add to the end of each field type, if it needs to be altered
	 * @return A String like: String foo, Integer bar, Object blah
	 */
	static String createSignatureFields(List<Field> fieldList, String fieldNameSuffix, String fieldTypeSuffix) {
		StringBuilder fields = new StringBuilder();
		if (fieldList != null) {
			boolean hasFieldTypeSuffix = StringUtils.isNotBlank(fieldTypeSuffix);
			boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

			for (Iterator<Field> i = fieldList.iterator(); i.hasNext();) {
				ObjectiveCField field = (ObjectiveCField) i.next();
				fields.append(field.getType());
				if ((hasFieldTypeSuffix) && (field.isEntity())) {
					fields.append(fieldTypeSuffix);
				}
				fields.append(" ");
				fields.append(field.getName());
				if ((hasFieldNameSuffix) && (field.isEntity())) {
					fields.append(fieldNameSuffix);
				}
				if (i.hasNext()) {
					fields.append(", ");
				}
			}
		}
		return fields.toString();
	}

	/**
	 * Returns the fields for a call to a method exactly as the fields are defined.
	 * @param fieldList a list of {@link Field} instances
	 * @return A String like: foo, bar, blah.
	 */
	static String createSignatureFieldParams(List<Field> fieldList) {
		return createSignatureFieldParams(fieldList, null);
	}

	/**
	 * Returns the fields for a call to a method, with an optional suffix for name and type. Suffix will only be used
	 * if the field references an entity type.
	 * @param fieldList a list of {@link Field} instances
	 * @param fieldNameSuffix a suffix to add to the end of each field name, if it needs to be altered
	 * @return A String like: foo, bar, blah.
	 */
	static String createSignatureFieldParams(List<Field> fieldList, String fieldNameSuffix) {
		StringBuilder fields = new StringBuilder();
		if (fieldList != null) {
			boolean hasFieldNameSuffix = StringUtils.isNotBlank(fieldNameSuffix);

			for (Iterator<Field> i = fieldList.iterator(); i.hasNext();) {
				ObjectiveCField field = (ObjectiveCField) i.next();
				fields.append(field.getName());
				if ((hasFieldNameSuffix) && (field.isEntity())) {
					fields.append(fieldNameSuffix);
				}
				if (i.hasNext()) {
					fields.append(", ");
				}
			}
		}
		return fields.toString();
	}

	/**
	 * Returns a base JNDI name as the base packaage name with forward slashes substituted for periods.
	 * @param basePackage package name (e.g., com.foo.bar)
	 * @return base JNDI string (e.g., com/foo/bar)
	 */
	public static String getBaseJndiName(String basePackage) {
		return (basePackage != null) ? basePackage.replace('.', '/') : "";
	}
}

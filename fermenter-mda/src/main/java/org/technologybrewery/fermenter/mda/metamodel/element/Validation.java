package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public interface Validation extends NamespacedMetamodel {

	String getDocumentation();

	Boolean isEnumerationType();

	/**
	 * @return Returns the maxLength.
	 */
	Integer getMaxLength();

	/**
	 * @return Returns the minLength.
	 */
	Integer getMinLength();

	/**
	 * @return Returns the maxValue.
	 */
	String getMaxValue();

	/**
	 * @return Returns the minValue.
	 */
	String getMinValue();

	Integer getScale();

	Collection<String> getFormats();

	enum BaseType {
		STRING("string"), FLOAT("float"), DATE("date"), TIMESTAMP("timestamp"), LONG("long"), DOUBLE("double"),
		INTEGER("integer"), SHORT("short"), NUMERIC_BOOLEAN("numeric_boolean"), BOOLEAN("boolean"),
		BIG_DECIMAL("big_decimal"), CHARACTER("character"), BLOB("blob"), GEOSPATIAL_POINT("geospatial_point"),
		UUID("uuid");

		private String value;

		private BaseType(String value) {
			this.value = value;
		}

		/**
		 * Returns the instance for the passed strategy value (ignoring case).
		 * 
		 * @param value string representation
		 * @return instance
		 */
		public static BaseType fromString(String value) {
			BaseType matchedType = null;
			for (BaseType type : BaseType.values()) {
				if (StringUtils.equalsIgnoreCase(type.value, value)) {
					matchedType = type;
					break;
				}
			}

			return matchedType;
		}

		/**
		 * {@inheritDoc}
		 */
		@JsonValue
		@Override
		public String toString() {
			return value;
		}
		
		/**
		 * A comma-separated list of valid options.
		 * 
		 * @return valid options
		 */
		public static String options() {
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (BaseType type : values()) {
				if (!isFirst) {
					sb.append(", ");
				}

				sb.append(type.toString());

				isFirst = false;
			}

			return sb.toString();
		}
		
		public static Boolean isSimpleType(String typeName) {
			return BaseType.fromString(typeName) != null;
		}

	}

}

package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public interface Field extends Metamodel {

	/**
	 * @return Returns the column.
	 */
	String getColumn();

	/**
	 * Returns documentation for the field.
	 * 
	 * @return description of the field
	 */
	String getDocumentation();

	/**
	 * Package for the type.
	 * 
	 * @return Returns the type package
	 */
	String getPackage();

	/**
	 * Field type (simple type, type dictionary type, or enumeration).
	 * 
	 * @return Returns the type.
	 */
	String getType();

	/**
	 * Returns validation constraints for this field.
	 * 
	 * @return validation rules
	 */
	Validation getValidation();

	/**
	 * @return Returns the required.
	 */
	Boolean isRequired();

	/**
	 * @return Returns the generator.
	 */
	Generator getGenerator();

	enum Generator {
		UUID("uuid"), IDENTITY("identity"), ASSIGNED("assigned"), AUTO("auto");

		private String value;

		private Generator(String value) {
			this.value = value;
		}

		/**
		 * Returns the instance for the passed generator value (ignoring case).
		 * 
		 * @param value string representation
		 * @return instance
		 */
		public static Generator fromString(String value) {
			Generator matchedGenerator = null;
			for (Generator generator : Generator.values()) {
				if (StringUtils.equalsIgnoreCase(generator.value, value)) {
					matchedGenerator = generator;
					break;
				}
			}

			return matchedGenerator;
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
			for (Generator generator : values()) {
				if (!isFirst) {
					sb.append(", ");
				}

				sb.append(generator.toString());

				isFirst = false;
			}

			return sb.toString();
		}

	}

}
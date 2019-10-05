package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the contract for an entity that a child of another entity.
 */
public interface Relation extends Validatable {

	/**
	 * Returns the type package of relation.
	 * 
	 * @return relation type package
	 */
	String getPackage();
	
	/**
	 * Returns the type of relation.
	 * 
	 * @return relation type
	 */
	String getType();	

	/**
	 * Returns relation-level documentation.
	 * 
	 * @return relation documentation
	 */
	String getDocumentation();

	/**
	 * Returns multiplicity of this relation (e.g., 1-M, 1-1, M-M).
	 * 
	 * @return multiplicity
	 */
	Multiplicity getMultiplicity();

	/**
	 * Returns the column name in which the primary key will be stored in this
	 * referencing entity.
	 * 
	 * @return local column name
	 */
	String getLocalColumn();

	/**
	 * Returns the fetch mode (eager or lazy) for this relation.
	 * 
	 * @return fetch mode
	 */
	FetchMode getFetchMode();

	/**
	 * Enumerated values representing multiplicity options.
	 */
	public enum Multiplicity {

		ONE_TO_MANY("1-M"), ONE_TO_ONE("1-1"), MANY_TO_MANY("M-M");

		private String value;

		private Multiplicity(String value) {
			this.value = value;
		}

		/**
		 * Returns the instance for the passed multiplicity value (ignoring case). If no
		 * known match is found, null is returned.
		 * 
		 * @param value string representation
		 * @return instance
		 */
		public static Multiplicity fromString(String value) {
			Multiplicity matchedMultiplicity = null;

			if (StringUtils.isNotBlank(value)) {
				String lowerCasedValue = value.toLowerCase();
				if ((ONE_TO_MANY.toString().equalsIgnoreCase(lowerCasedValue))
						|| ("one-to-many".equals(lowerCasedValue)) || ("one-many".equals(lowerCasedValue))) {
					matchedMultiplicity = ONE_TO_MANY;

				} else if ((ONE_TO_ONE.toString().equalsIgnoreCase(lowerCasedValue))
						|| ("one-to-one".equals(lowerCasedValue)) || ("one-one".equals(lowerCasedValue))) {
					matchedMultiplicity = ONE_TO_ONE;

				} else if ((MANY_TO_MANY.toString().equalsIgnoreCase(lowerCasedValue))
						|| ("many-to-many".equals(lowerCasedValue)) || ("many-many".equals(lowerCasedValue))) {
					matchedMultiplicity = MANY_TO_MANY;

				}
			}

			return matchedMultiplicity;
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
			for (Multiplicity mode : values()) {
				if (!isFirst) {
					sb.append(", ");
				}

				sb.append(mode.toString());

				isFirst = false;
			}

			return sb.toString();
		}
		
	}

	/**
	 * Enumerated values representing fetch mode options.
	 */
	public enum FetchMode {

		EAGER("eager"), LAZY("lazy");

		private String value;

		private FetchMode(String value) {
			this.value = value;
		}

		/**
		 * Returns the instance for the passed fetch mode value (ignoring case). If no
		 * known match is found, null is returned.
		 * 
		 * @param value string representation
		 * @return instance
		 */
		public static FetchMode fromString(String value) {
			FetchMode matchedFetchMode = null;

			if (StringUtils.isNotBlank(value)) {
				String lowerCasedValue = value.toLowerCase();
				if (EAGER.toString().equals(lowerCasedValue)) {
					matchedFetchMode = EAGER;

				} else if (LAZY.toString().equals(lowerCasedValue)) {
					matchedFetchMode = LAZY;

				}
			}

			return matchedFetchMode;
		}

		/**
		 * {@inheritDoc}
		 */
		@JsonValue
		@Override
		public String toString() {
			return value;
		}
		
		public String toUpperCase() {
		    return value.toUpperCase();
		}

		/**
		 * A comma-separated list of valid options.
		 * 
		 * @return valid options
		 */
		public static String options() {
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (FetchMode mode : values()) {
				if (!isFirst) {
					sb.append(", ");
				}

				sb.append(mode.toString());

				isFirst = false;
			}

			return sb.toString();
		}
		
	}
	
	/**
     * Gets the key fields that the child class will reference for this relation
     * @return The key fields
     */
    public Field getParentIdentifier(String parentEntityName);

}
package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the contract for an entity parent type.
 */
public interface Parent extends NamespacedMetamodel {

	/**
	 * Returns the type of parent.
	 * 
	 * @return parent type
	 */
	String getType();

	/**
	 * Returns inheritance strategy when considering this parent.
	 * 
	 * @return inheritance strategy
	 */
	InheritanceStrategy getInheritanceStrategy();

	/**
	 * Types of inheritance strategies that can be used when processing parent
	 * references.
	 */
	enum InheritanceStrategy {

		MAPPED_SUPERCLASS("mapped-superclass");

		private String value;

		private InheritanceStrategy(String value) {
			this.value = value;
		}

		/**
		 * Returns the instance for the passed inheritance value (ignoring case).
		 * 
		 * @param value string representation
		 * @return instance
		 */
		public static InheritanceStrategy fromString(String value) {
			InheritanceStrategy matchedStrategy = null;
			for (InheritanceStrategy strategy : InheritanceStrategy.values()) {
				if (StringUtils.equalsIgnoreCase(strategy.value, value)) {
					matchedStrategy = strategy;
					break;
				}
			}

			return matchedStrategy;
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
			for (InheritanceStrategy strategy : values()) {
				if (!isFirst) {
					sb.append(", ");
				}

				sb.append(strategy.toString());

				isFirst = false;
			}

			return sb.toString();
		}

	}

}
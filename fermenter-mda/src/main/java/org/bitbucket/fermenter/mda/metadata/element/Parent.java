package org.bitbucket.fermenter.mda.metadata.element;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a parent of an {@link Entity}.
 */
public interface Parent {

    enum InheritanceStrategy {
        MAPPED_SUPERCLASS("mapped-superclass");
        private String value;

        private InheritanceStrategy(String value) {
            this.value = value;
        }

        public static InheritanceStrategy fromString(String value) {
            for (InheritanceStrategy strategy : InheritanceStrategy.values()) {
                if (StringUtils.equalsIgnoreCase(strategy.value, value)) {
                    return strategy;
                }
            }
            throw new IllegalArgumentException(
                    "Could not find enum " + InheritanceStrategy.class.getName() + " for value: " + value);
        }
    }

    String getType();

    InheritanceStrategy getInheritanceStrategy();
}

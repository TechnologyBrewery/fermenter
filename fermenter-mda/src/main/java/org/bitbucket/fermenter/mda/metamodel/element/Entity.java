package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the contract for a entity that contains fields and associations.
 */
public interface Entity extends NamespacedMetamodel {

    /**
     * Returns service-level documentation.
     * 
     * @return service documentation
     */
    String getDocumentation();

    /**
     * Returns the name of the table mapped to this entity.
     * 
     * @return table name
     */
    String getTable();

    /**
     * Returns the lock strategy for the table. (e.g., optimistic, pessimistic, none)
     * 
     * @return lock strategy
     */
    LockStrategy getLockStrategy();

    /**
     * Determines whether this is a persistent or transient object. By default, all entities as persistent.
     * 
     * @return transient nature of this instance
     */
    Boolean isTransient();

    /**
     * Returns information about a parent entity.
     * 
     * @return parent entity info
     */
    Parent getParent();

    /**
     * Returns the identifier for this instance. If you want to use a multi-part key, we recommend treating those values
     * as a business keys and maintain a single programmatic key. This makes generation substantially more
     * straightforward in these situations.
     * 
     * @return identifier
     */
    Field getIdentifier();
    
    /**
     * Returns fields for this instance.
     * 
     * @return list of fields
     */
    List<Field> getFields();    
    
    /**
     * Returns references for this instance.
     * 
     * @return list of references
     */
	List<Reference> getReferences();
	
    /**
     * Returns relations for this instance.
     * 
     * @return list of relations
     */
	List<Relation> getRelations();	

    /**
     * Enumerated values representing allowed lock strategies.
     */
    public enum LockStrategy {
        OPTIMISTIC("optimistic"), NONE("none");

        private String value;

        private LockStrategy(String value) {
            this.value = value;
        }

        /**
         * Returns the instance for the passed strategy value (ignoring case).
         * 
         * @param value
         *            string representation
         * @return instance
         */
        public static LockStrategy fromString(String value) {
            LockStrategy matchedStrategy = null;
            for (LockStrategy strategy : LockStrategy.values()) {
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
    }

}
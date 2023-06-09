package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for an entity referencing another entity.
 */
public interface Reference extends NamespacedMetamodel {
    
    /**
     * Returns the type of reference.
     * 
     * @return reference type
     */
    String getType();
    
    /**
     * Returns reference-level documentation.
     * 
     * @return reference documentation
     */
    String getDocumentation();
    
	/**
	 * @return Returns the required.
	 */
	Boolean isRequired(); 

    /**
     * Returns the column name in which the primary key will be stored in this referencing entity.
     * 
     * @return local column name
     */
    String getLocalColumn();
    
    /**
     * Gets the foreign keys associated with this reference plus all parent references
     * @return List of foreign key fields
     */
    List<Field> getForeignKeyFields();

    
}

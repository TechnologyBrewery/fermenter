package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Defines the contract for an entity referencing another entity.
 */
public interface Reference extends Metamodel {
    
    /**
     * Returns the type of parameter.
     * 
     * @return parameter type
     */
    Type getType();
    
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
    
}
package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for an operation.
 */
public interface Operation extends Metamodel {
    
    /**
     * These transaction attribute constants are listed exactly as 
     * they are specified by the JEE specification.  They are uppercased
     * to eliminate the possibility of case-related issues when reading
     * and translating these values.
     */
    static final String TRANSACTION_REQUIRED = "Required";
    static final String TRANSACTION_REQUIRES_NEW = "RequiresNew";
    static final String TRANSACTION_MANDATORY = "Mandatory";
    static final String TRANSACTION_NOT_SUPPORTED = "NotSupported";
    static final String TRANSACTION_SUPPORTS = "Supports";
    static final String TRANSACTION_NEVER= "Never";     

    /**
     * Returns documentation for this operation.
     * 
     * @return service documentation
     */
    String getDocumentation();
    
    /**
     * Returns the transaction attribute of this operation.
     * 
     * @return
     */
    String getTransactionAttribute();
    
    /**
     * Provides information about what this operation returns.
     * 
     * @return operation 
     * return info
     */
    Return getReturn();
    
    /**
     * Returns the parameter instances within this operation.
     * 
     * @return parameters
     */
    List<Parameter> getParameters();
    
    /**
     * Returns whether or not this operation uses GZip compression.
     * 
     * @return GZip compression
     */
    Boolean isCompressedWithGZip();    

}
package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for an operation.
 */
public interface Operation extends Metamodel {

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
     * @return operation return info
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
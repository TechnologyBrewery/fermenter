package org.technologybrewery.fermenter.mda.metamodel.element;

/**
 * Defines the contract for an operation return type.
 */
public interface Return extends NamespacedMetamodel {

    /**
     * Returns the type of parameter.
     * 
     * @return parameter type
     */
    String getType();

    /**
     * Returns whether or not this returns many instances of the type or a
     * single instance.
     * 
     * @return is many?
     */
    Boolean isMany();

    /**
     * Returns operation response encoding.
     * 
     * @return response encoding
     */
    String getResponseEncoding();

    /**
     * Returns whether or not this operation returns a paged response.
     * 
     * @return isPagedResponse boolean
     */
    Boolean isPagedResponse();
}

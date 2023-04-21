package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for a service that contains one or more operations.
 */
public interface Service extends NamespacedMetamodel {

    /**
     * Returns service-level documentation.
     * 
     * @return service documentation
     */
    String getDocumentation();

    /**
     * Returns the operation instances within this service.
     * 
     * @return operations
     */
    List<Operation> getOperations();

}

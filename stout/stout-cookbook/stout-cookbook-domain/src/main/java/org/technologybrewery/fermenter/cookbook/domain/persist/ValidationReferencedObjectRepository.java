package org.technologybrewery.fermenter.cookbook.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;

/**
 * Data access repository for the ValidationReferencedObject business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface ValidationReferencedObjectRepository extends JpaRepository<ValidationReferencedObjectBO, UUID>, JpaSpecificationExecutor<ValidationReferencedObjectBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

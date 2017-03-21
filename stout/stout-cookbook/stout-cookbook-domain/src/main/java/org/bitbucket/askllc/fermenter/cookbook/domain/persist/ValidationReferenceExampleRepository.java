package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferenceExampleBO;

/**
 * Data access repository for the ValidationReferenceExample business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface ValidationReferenceExampleRepository extends JpaRepository<ValidationReferenceExampleBO, String>, JpaSpecificationExecutor<ValidationReferenceExampleBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBO;

/**
 * Data access repository for the ValidationExample business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface ValidationExampleRepository extends JpaRepository<ValidationExampleBO, String>, JpaSpecificationExecutor<ValidationExampleBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import java.lang.String;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.NonUUIDKeyEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the NonUUIDKeyEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface NonUUIDKeyEntityRepository extends JpaRepository<NonUUIDKeyEntityBO, String>, JpaSpecificationExecutor<NonUUIDKeyEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
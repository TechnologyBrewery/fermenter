package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import java.util.UUID;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.CachedEntityExampleBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the CachedEntityExample business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface CachedEntityExampleRepository extends JpaRepository<CachedEntityExampleBO, UUID>, JpaSpecificationExecutor<CachedEntityExampleBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
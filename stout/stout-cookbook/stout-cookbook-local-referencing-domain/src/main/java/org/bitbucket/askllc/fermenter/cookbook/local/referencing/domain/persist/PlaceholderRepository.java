package org.bitbucket.askllc.fermenter.cookbook.local.referencing.domain.persist;

import java.util.UUID;

import org.bitbucket.askllc.fermenter.cookbook.local.referencing.domain.bizobj.PlaceholderBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the Placeholder business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface PlaceholderRepository extends JpaRepository<PlaceholderBO, UUID>, JpaSpecificationExecutor<PlaceholderBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
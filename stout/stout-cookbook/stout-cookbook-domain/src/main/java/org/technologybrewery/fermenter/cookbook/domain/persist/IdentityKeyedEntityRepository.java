package org.technologybrewery.fermenter.cookbook.domain.persist;

import java.lang.Integer;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.IdentityKeyedEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the IdentityKeyedEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface IdentityKeyedEntityRepository extends JpaRepository<IdentityKeyedEntityBO, Integer>, JpaSpecificationExecutor<IdentityKeyedEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

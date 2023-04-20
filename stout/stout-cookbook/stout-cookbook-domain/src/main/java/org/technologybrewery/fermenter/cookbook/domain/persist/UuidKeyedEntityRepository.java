package org.technologybrewery.fermenter.cookbook.domain.persist;

import java.util.UUID;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.UuidKeyedEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the UuidKeyedEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface UuidKeyedEntityRepository extends JpaRepository<UuidKeyedEntityBO, UUID>, JpaSpecificationExecutor<UuidKeyedEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

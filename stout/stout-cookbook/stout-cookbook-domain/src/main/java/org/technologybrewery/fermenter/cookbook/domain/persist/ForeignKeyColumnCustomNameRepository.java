package org.technologybrewery.fermenter.cookbook.domain.persist;

import java.util.UUID;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.ForeignKeyColumnCustomNameBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the ForeignKeyColumnCustomName business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface ForeignKeyColumnCustomNameRepository extends JpaRepository<ForeignKeyColumnCustomNameBO, UUID>, JpaSpecificationExecutor<ForeignKeyColumnCustomNameBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

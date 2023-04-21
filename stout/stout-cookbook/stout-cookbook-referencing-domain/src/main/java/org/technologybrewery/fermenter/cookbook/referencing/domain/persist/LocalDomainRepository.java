package org.technologybrewery.fermenter.cookbook.referencing.domain.persist;

import java.util.UUID;

import org.technologybrewery.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the LocalDomain business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface LocalDomainRepository extends JpaRepository<LocalDomainBO, UUID>, JpaSpecificationExecutor<LocalDomainBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

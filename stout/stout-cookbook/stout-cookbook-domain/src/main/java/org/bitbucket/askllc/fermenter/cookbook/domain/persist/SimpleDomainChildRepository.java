package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;

/**
 * Data access repository for the SimpleDomainChild business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface SimpleDomainChildRepository extends JpaRepository<SimpleDomainChildBO, UUID>, JpaSpecificationExecutor<SimpleDomainChildBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import java.lang.Long;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.LongKeyedEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the LongKeyedEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface LongKeyedEntityRepository extends JpaRepository<LongKeyedEntityBO, Long>, JpaSpecificationExecutor<LongKeyedEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
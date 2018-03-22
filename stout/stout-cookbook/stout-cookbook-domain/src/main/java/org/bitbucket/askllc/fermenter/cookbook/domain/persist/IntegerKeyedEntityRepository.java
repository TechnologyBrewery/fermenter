package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import java.lang.Integer;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.IntegerKeyedEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the IntegerKeyedEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface IntegerKeyedEntityRepository extends JpaRepository<IntegerKeyedEntityBO, Integer>, JpaSpecificationExecutor<IntegerKeyedEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}
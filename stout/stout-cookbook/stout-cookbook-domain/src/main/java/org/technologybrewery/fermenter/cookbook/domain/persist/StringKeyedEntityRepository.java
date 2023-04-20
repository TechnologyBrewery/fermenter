package org.technologybrewery.fermenter.cookbook.domain.persist;

import java.lang.String;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.StringKeyedEntityBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data access repository for the StringKeyedEntity business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */ 
public interface StringKeyedEntityRepository extends JpaRepository<StringKeyedEntityBO, String>, JpaSpecificationExecutor<StringKeyedEntityBO> {
	
	/**
	 * Developers should leverage this interface to define any query logic
	 * that cannot be realized through {@link JpaRepository}'s built-in
	 * functionality.  
	 */

}

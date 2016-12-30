package org.bitbucket.askllc.fermenter.cookbook.domain.persist;

import java.util.List;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access repository for the SimpleDomain business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public interface SimpleDomainRepository extends JpaRepository<SimpleDomainBO, String> {

	/**
	 * Developers should leverage this interface to define any query logic that cannot be realized through
	 * {@link JpaRepository}'s built-in functionality.
	 */

	/**
	 * Returns all @{link SimpleDomainBO}s with the given type. Developers can use Spring Data JPA's capability to
	 * automatically derive query functionality based off of the method name as demonstrated here.
	 * 
	 * @param type
	 * @return
	 */
	List<SimpleDomainBO> findByType(String type);
}
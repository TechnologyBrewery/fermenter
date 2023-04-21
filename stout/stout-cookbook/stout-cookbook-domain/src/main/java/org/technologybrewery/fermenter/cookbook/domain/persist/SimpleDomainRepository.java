package org.technologybrewery.fermenter.cookbook.domain.persist;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Data access repository for the SimpleDomain business object.
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public interface SimpleDomainRepository
		extends JpaRepository<SimpleDomainBO, UUID>, JpaSpecificationExecutor<SimpleDomainBO> {

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

	/**
	 * Retrieves a page of {@link SimpleDomainBO}s with relations eagerly fetched based on the paging attributes
	 * provided within the given {@link Pageable} instance. While {@link Specification}s may alternatively be used in
	 * conjunction with paging queries, this approach prevents the utilization of a countQuery, which can be used to
	 * improve performance.
	 * 
	 * @param pageable
	 *            defines the page of {@link SimpleDomainBO}s to retrieve.
	 * @return page of {@link SimpleDomainBO}s
	 */
	@Query(value = "select bo from SimpleDomainBO bo left join fetch bo.simpleDomainChilds", countQuery = "select count(*) from SimpleDomainBO")
	Page<SimpleDomainBO> findAllEagerFetchRelations(Pageable pageable);
	
	
	List<SimpleDomainBO> findByTheDate1Before(Date date);
	
	List<SimpleDomainBO> findByTheDate1After(Date date);
	
	List<SimpleDomainBO> findByTheDate1(Date date);

    Page<SimpleDomainBO> findByNameContains(String nameFilter, Pageable pageable);
}

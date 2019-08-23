package org.bitbucket.fermenter.stout.client.service;

import java.util.List;

/**
 * Provides the contract for defining a order of entity processing manually. This is useful when there are non-modeled
 * dependencies between entities (while an edge case, does happen from time to time).
 * 
 * Any items provides by an implementation of this interface will be: (1.) put at the top of the ordering for inserts
 * and updates, (2.) put at the bottom of the ordering for deletes (3.) any missed entities will be added, ordered by
 * the default {@link EntityComparitor}, after this list for inserts and updates (4.) any missed entities will be added,
 * ordered by the default {@link EntityComparitor}, before this list for deletes
 */
public interface CustomFlushOrderProvider {

    /**
     * Provides a custom listing of the desired order for flushing entities via the rest client transactional cache.
     * 
     * @return ordered list
     */
    List<String> getCustomFlushOrder();

}

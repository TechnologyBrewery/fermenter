package org.bitbucket.fermenter.stout.client.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides a transactional cache for use with Stout's rest client.
 *
 * @param <K>
 *            primary key class
 * @param <T>
 *            transfer object class
 */
public class LevelOneCache<K, T> {

    private Map<K, T> lookupMap = new HashMap<>();
    private Set<T> pendingInserts = new HashSet<>();
    private Map<K, T> pendingUpdates = new HashMap<>();
    private Set<K> pendingDeletes = new HashSet<>();

    /**
     * Queue an entity for create. Entities will NOT be available via the cache unless they are flushed as they cannot
     * be guaranteed to have a primary key by which to look them up.
     * 
     * @param instance
     *            instance to create
     */
    public void create(T instance) {
        pendingInserts.add(instance);
    }

    /**
     * Retrieves an entity based on the passed primary key from the pending list of object maintained by this cache.
     * 
     * @param id
     *            primary key to look up
     * @return entity or null if no match found
     */
    public T retrieve(K id) {
        return lookupMap.get(id);
    }

    /**
     * Returns all entities that are pending an insert.
     * 
     * @return pending inserts
     */
    public Collection<T> getPendingInserts() {
        return pendingInserts;
    }

    /**
     * Queue an update. These items WILL be returned via call to the entity maintenance service to retrieve.
     * 
     * @param primaryKey
     *            key of the entity to update
     * @param entity
     *            the entity to update
     */
    public void update(K primaryKey, T entity) {
        pendingUpdates.put(primaryKey, entity);
        lookupMap.put(primaryKey, entity);
    }

    /**
     * Returns all entities that are pending an update.
     * 
     * @return pending updates
     */
    public Collection<T> getPendingUpdates() {
        return pendingUpdates.values();
    }

    /**
     * Queues a delete. These items will NOT be returned via call to the entity maintenance service to retrieve as they
     * have been "removed" from a transactional perspective.
     * 
     * @param primaryKey
     *            key of the entity to delete
     */
    public void delete(K primaryKey) {
        pendingDeletes.add(primaryKey);
        lookupMap.remove(primaryKey);
    }

    /**
     * Queues a collection for delete. These items will NOT be returned via call to the entity maintenance service to
     * retrieve as they have been "removed" from a transactional perspective.
     * 
     * @param primaryKey
     *            key of the entity to delete
     */
    public void delete(Collection<K> primaryKeys) {
        pendingDeletes.addAll(primaryKeys);
        for (K primaryKey : primaryKeys) {
            lookupMap.remove(primaryKey);
        }
    }

    /**
     * Returns all entities that are pending a delete.
     * 
     * @return pending deletes
     */
    public Collection<K> getPendingDeletes() {
        return pendingDeletes;
    }
    
    /**
     * Removes pending inserts and updates.
     */
    public void clearPendingInsertsAndUpdates() {
        pendingInserts.clear();
        pendingUpdates.clear();
    } 
    
    /**
     * Removes pending deletes.
     */
    public void clearPendingDeletes() {
        pendingInserts.clear();
        pendingUpdates.clear();
        pendingDeletes.clear();
    }     
    
    /**
     * Returns all pending actions.
     */
    public void clearPendingActions() {
        clearPendingInsertsAndUpdates();
        clearPendingDeletes();
    }    

}

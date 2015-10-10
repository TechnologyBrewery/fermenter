package org.bitbucket.fermenter.persist;

import org.bitbucket.fermenter.bizobj.BusinessObject;
import org.bitbucket.fermenter.transfer.PrimaryKey;

/**
 * This interface specifies the core behavior that must be specified by all data
 * access objects.
 * 
 */
public interface Dao<BO extends BusinessObject, PK extends PrimaryKey> {

    /**
     * Saves a business object.
     * 
     * @param businessObject
     *            The object to save
     * @return The resulting object after a save
     */
    BO save(BO businessObject);

    /**
     * Delete a BO.
     * 
     * @param pk
     *            The primary key of the BO to delete
     * @return Entity The deleted BO
     */
    BO delete(PK pk);

    /**
     * Find a BO with the specified key.
     * 
     * @param pk
     *            The primary key of the entity to select
     * @return Entity The entity with the specified key or null if no match
     */
    BO findByPrimaryKey(PK pk);

}

package org.tigris.atlas.persist;

import org.tigris.atlas.bizobj.BusinessObject;
import org.tigris.atlas.transfer.PrimaryKey;



/**
 * This interface specifies the core behavior that must be specified by all
 * data access objects.
 * 
 * @author Steve Andrews
 *
 */
public interface Dao {

	/**
	 * Delete a BO
	 * 
	 * @param pk The primary key of the BO to delete
	 * @return Entity The deleted BO
	 */
	public BusinessObject delete(PrimaryKey pk);

	/**
	 * Find a BO with the specified key
	 * 
	 * @param pk The primary key of the entity to select
	 * @return Entity The entity with the specified key or null if no match
	 */
	public BusinessObject findByPrimaryKey(PrimaryKey pk);

}

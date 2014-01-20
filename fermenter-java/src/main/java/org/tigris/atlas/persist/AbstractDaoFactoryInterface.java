package org.tigris.atlas.persist;

import org.tigris.atlas.factory.Factory;

public interface AbstractDaoFactoryInterface extends Factory {

	/**
	 * Creates a data access object of the specified type
	 * @param type
	 * @return
	 */
	public Dao createDao(String type);
	
}

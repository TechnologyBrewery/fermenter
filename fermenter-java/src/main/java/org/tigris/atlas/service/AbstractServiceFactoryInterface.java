package org.tigris.atlas.service;

import org.tigris.atlas.factory.Factory;

public interface AbstractServiceFactoryInterface extends Factory {

	/**
	 * Creates a service of the specified name
	 * @param name
	 * @return
	 */
	public Service createService(String name);
	
}

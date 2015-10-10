package org.bitbucket.fermenter.service;

import org.bitbucket.fermenter.factory.Factory;

public interface AbstractServiceFactoryInterface extends Factory {

	/**
	 * Creates a service of the specified name
	 * @param name
	 * @return
	 */
	public Service createService(String name);
	
}

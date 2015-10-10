package org.bitbucket.fermenter.stout.service;

import org.bitbucket.fermenter.stout.factory.Factory;

public interface AbstractServiceFactoryInterface extends Factory {

	/**
	 * Creates a service of the specified name
	 * @param name
	 * @return
	 */
	public Service createService(String name);
	
}

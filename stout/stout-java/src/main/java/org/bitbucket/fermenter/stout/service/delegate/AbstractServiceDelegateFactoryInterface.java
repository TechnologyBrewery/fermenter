package org.bitbucket.fermenter.stout.service.delegate;

import org.bitbucket.fermenter.stout.factory.Factory;
import org.bitbucket.fermenter.stout.service.Service;

public interface AbstractServiceDelegateFactoryInterface extends Factory {

	/**
	 * Creates a service delegate of the specified name
	 * @param name
	 * @return
	 */
	public Service createServiceDelegate(String name);
	
}

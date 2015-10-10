package org.bitbucket.fermenter.service.delegate;

import org.bitbucket.fermenter.factory.Factory;
import org.bitbucket.fermenter.service.Service;

public interface AbstractServiceDelegateFactoryInterface extends Factory {

	/**
	 * Creates a service delegate of the specified name
	 * @param name
	 * @return
	 */
	public Service createServiceDelegate(String name);
	
}

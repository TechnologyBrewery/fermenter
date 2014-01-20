package org.tigris.atlas.service.delegate;

import org.tigris.atlas.factory.Factory;
import org.tigris.atlas.service.Service;

public interface AbstractServiceDelegateFactoryInterface extends Factory {

	/**
	 * Creates a service delegate of the specified name
	 * @param name
	 * @return
	 */
	public Service createServiceDelegate(String name);
	
}

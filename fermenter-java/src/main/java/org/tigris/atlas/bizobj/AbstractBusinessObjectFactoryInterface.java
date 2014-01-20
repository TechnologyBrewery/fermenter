package org.tigris.atlas.bizobj;

import org.tigris.atlas.factory.Factory;

public interface AbstractBusinessObjectFactoryInterface extends Factory {

	/**
	 * Creates a business object of the specified type
	 * @param type
	 * @return
	 */
	public BusinessObject createBusinessObject(String type);
	
}

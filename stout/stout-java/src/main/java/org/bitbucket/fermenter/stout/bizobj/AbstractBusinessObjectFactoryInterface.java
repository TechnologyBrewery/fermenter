package org.bitbucket.fermenter.stout.bizobj;

import org.bitbucket.fermenter.stout.factory.Factory;

public interface AbstractBusinessObjectFactoryInterface extends Factory {

	/**
	 * Creates a business object of the specified type
	 * @param type
	 * @return
	 */
	public BusinessObject createBusinessObject(String type);
	
}

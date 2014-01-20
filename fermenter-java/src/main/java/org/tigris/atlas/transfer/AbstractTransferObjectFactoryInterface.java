package org.tigris.atlas.transfer;

import org.tigris.atlas.factory.Factory;

public interface AbstractTransferObjectFactoryInterface extends Factory {

	/**
	 * Creates a transfer object of the specified type
	 * @param type
	 * @return
	 */
	public TransferObject createTransferObject(String type);
	
	/**
	 * Creates a primary key of the specified type
	 * @param type
	 * @return
	 */
	public PrimaryKey createPrimaryKey(String type);
	
}

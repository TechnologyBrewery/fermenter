package org.bitbucket.fermenter.stout.transfer;

import org.bitbucket.fermenter.stout.factory.Factory;

public interface AbstractTransferObjectFactoryInterface extends Factory {

    /**
     * Creates a transfer object of the specified type
     * 
     * @param type
     * @return
     */
    public TransferObject createTransferObject(String type);

    /**
     * Creates a primary key of the specified type
     * 
     * @param type
     * @return
     */
    public PrimaryKey createPrimaryKey(String type);

}

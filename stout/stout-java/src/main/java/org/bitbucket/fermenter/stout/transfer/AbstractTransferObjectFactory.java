package org.bitbucket.fermenter.stout.transfer;

import org.bitbucket.fermenter.stout.factory.FactoryManager;

public abstract class AbstractTransferObjectFactory {

    protected static AbstractTransferObjectFactoryInterface getAbstractTransferObjectFactory(Class<?> clazz) {
	return (AbstractTransferObjectFactoryInterface) FactoryManager.createFactory(FactoryManager.TRANSFER_OBJECT,
		clazz);
    }

}

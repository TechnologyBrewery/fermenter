package org.bitbucket.fermenter.transfer;

import org.bitbucket.fermenter.factory.FactoryManager;

public abstract class AbstractTransferObjectFactory {

    protected static AbstractTransferObjectFactoryInterface getAbstractTransferObjectFactory(Class<?> clazz) {
	return (AbstractTransferObjectFactoryInterface) FactoryManager.createFactory(FactoryManager.TRANSFER_OBJECT,
		clazz);
    }

}

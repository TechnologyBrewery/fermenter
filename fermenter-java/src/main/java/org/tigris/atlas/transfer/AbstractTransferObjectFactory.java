package org.tigris.atlas.transfer;

import org.tigris.atlas.factory.FactoryManager;

public abstract class AbstractTransferObjectFactory {

    protected static AbstractTransferObjectFactoryInterface getAbstractTransferObjectFactory(Class<?> clazz) {
	return (AbstractTransferObjectFactoryInterface) FactoryManager.createFactory(FactoryManager.TRANSFER_OBJECT,
		clazz);
    }

}

package org.tigris.atlas.transfer;

import org.tigris.atlas.factory.FactoryFactory;

public abstract class AbstractTransferObjectFactory {
	
	protected static AbstractTransferObjectFactoryInterface getAbstractTransferObjectFactory(Class clazz) {
		return (AbstractTransferObjectFactoryInterface) FactoryFactory.createFactory( FactoryFactory.TRANSFER_OBJECT, clazz );
	}

}

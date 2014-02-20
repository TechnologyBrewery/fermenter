package org.tigris.atlas.service.delegate;

import org.tigris.atlas.factory.FactoryManager;

public abstract class AbstractServiceDelegateFactory {
	
	protected static AbstractServiceDelegateFactoryInterface getAbstractServiceDelegateFactory(Class clazz) {
		return (AbstractServiceDelegateFactoryInterface) FactoryManager.createFactory( FactoryManager.SERVICE_DELEGATE, clazz );
	}
	
}

package org.tigris.atlas.service.delegate;

import org.tigris.atlas.factory.FactoryFactory;

public abstract class AbstractServiceDelegateFactory {
	
	protected static AbstractServiceDelegateFactoryInterface getAbstractServiceDelegateFactory(Class clazz) {
		return (AbstractServiceDelegateFactoryInterface) FactoryFactory.createFactory( FactoryFactory.SERVICE_DELEGATE, clazz );
	}
	
}

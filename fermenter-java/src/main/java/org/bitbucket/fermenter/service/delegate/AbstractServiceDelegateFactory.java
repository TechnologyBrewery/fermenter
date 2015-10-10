package org.bitbucket.fermenter.service.delegate;

import org.bitbucket.fermenter.factory.FactoryManager;

public abstract class AbstractServiceDelegateFactory {
	
	protected static AbstractServiceDelegateFactoryInterface getAbstractServiceDelegateFactory(Class clazz) {
		return (AbstractServiceDelegateFactoryInterface) FactoryManager.createFactory( FactoryManager.SERVICE_DELEGATE, clazz );
	}
	
}

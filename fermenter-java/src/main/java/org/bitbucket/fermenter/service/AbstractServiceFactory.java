package org.bitbucket.fermenter.service;

import org.bitbucket.fermenter.factory.FactoryManager;

public abstract class AbstractServiceFactory {
	
	protected static AbstractServiceFactoryInterface getAbstractServiceFactory(Class clazz) {
		return (AbstractServiceFactoryInterface) FactoryManager.createFactory( FactoryManager.SERVICE, clazz );
	}
	
}

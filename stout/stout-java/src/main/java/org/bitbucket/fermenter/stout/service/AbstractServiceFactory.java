package org.bitbucket.fermenter.stout.service;

import org.bitbucket.fermenter.stout.factory.FactoryManager;

public abstract class AbstractServiceFactory {
	
	protected static AbstractServiceFactoryInterface getAbstractServiceFactory(Class clazz) {
		return (AbstractServiceFactoryInterface) FactoryManager.createFactory( FactoryManager.SERVICE, clazz );
	}
	
}

package org.tigris.atlas.service;

import org.tigris.atlas.factory.FactoryManager;

public abstract class AbstractServiceFactory {
	
	protected static AbstractServiceFactoryInterface getAbstractServiceFactory(Class clazz) {
		return (AbstractServiceFactoryInterface) FactoryManager.createFactory( FactoryManager.SERVICE, clazz );
	}
	
}

package org.tigris.atlas.service;

import org.tigris.atlas.factory.FactoryFactory;

public abstract class AbstractServiceFactory {
	
	protected static AbstractServiceFactoryInterface getAbstractServiceFactory(Class clazz) {
		return (AbstractServiceFactoryInterface) FactoryFactory.createFactory( FactoryFactory.SERVICE, clazz );
	}
	
}

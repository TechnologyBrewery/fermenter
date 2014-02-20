package org.tigris.atlas.persist;

import org.tigris.atlas.factory.FactoryManager;

public abstract class AbstractDaoFactory {
	
	protected static AbstractDaoFactoryInterface getAbstractDaoFactory(Class clazz) {
		return (AbstractDaoFactoryInterface) FactoryManager.createFactory( FactoryManager.DAO, clazz );
	}

}

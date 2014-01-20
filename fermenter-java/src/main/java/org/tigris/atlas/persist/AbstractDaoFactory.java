package org.tigris.atlas.persist;

import org.tigris.atlas.factory.FactoryFactory;

public abstract class AbstractDaoFactory {
	
	protected static AbstractDaoFactoryInterface getAbstractDaoFactory(Class clazz) {
		return (AbstractDaoFactoryInterface) FactoryFactory.createFactory( FactoryFactory.DAO, clazz );
	}

}

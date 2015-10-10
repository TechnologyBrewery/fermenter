package org.bitbucket.fermenter.persist;

import org.bitbucket.fermenter.factory.FactoryManager;

public abstract class AbstractDaoFactory {
	
	protected static AbstractDaoFactoryInterface getAbstractDaoFactory(Class clazz) {
		return (AbstractDaoFactoryInterface) FactoryManager.createFactory( FactoryManager.DAO, clazz );
	}

}

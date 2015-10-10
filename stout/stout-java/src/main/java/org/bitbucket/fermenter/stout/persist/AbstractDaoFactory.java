package org.bitbucket.fermenter.stout.persist;

import org.bitbucket.fermenter.stout.factory.FactoryManager;

public abstract class AbstractDaoFactory {
	
	protected static AbstractDaoFactoryInterface getAbstractDaoFactory(Class clazz) {
		return (AbstractDaoFactoryInterface) FactoryManager.createFactory( FactoryManager.DAO, clazz );
	}

}

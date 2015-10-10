package org.bitbucket.fermenter.stout.bizobj;

import org.bitbucket.fermenter.stout.factory.FactoryManager;

public abstract class AbstractBusinessObjectFactory {
	
	protected static AbstractBusinessObjectFactoryInterface getAbstractBusinessObjectFactory(Class clazz) {
		return (AbstractBusinessObjectFactoryInterface) FactoryManager.createFactory( FactoryManager.BUSINESS_OBJECT, clazz );
	}

}

package org.bitbucket.fermenter.bizobj;

import org.bitbucket.fermenter.factory.FactoryManager;

public abstract class AbstractBusinessObjectFactory {
	
	protected static AbstractBusinessObjectFactoryInterface getAbstractBusinessObjectFactory(Class clazz) {
		return (AbstractBusinessObjectFactoryInterface) FactoryManager.createFactory( FactoryManager.BUSINESS_OBJECT, clazz );
	}

}

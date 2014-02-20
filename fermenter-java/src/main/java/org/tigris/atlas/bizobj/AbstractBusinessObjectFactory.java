package org.tigris.atlas.bizobj;

import org.tigris.atlas.factory.FactoryManager;

public abstract class AbstractBusinessObjectFactory {
	
	protected static AbstractBusinessObjectFactoryInterface getAbstractBusinessObjectFactory(Class clazz) {
		return (AbstractBusinessObjectFactoryInterface) FactoryManager.createFactory( FactoryManager.BUSINESS_OBJECT, clazz );
	}

}

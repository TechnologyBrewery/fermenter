package org.tigris.atlas.bizobj;

import org.tigris.atlas.factory.FactoryFactory;

public abstract class AbstractBusinessObjectFactory {
	
	protected static AbstractBusinessObjectFactoryInterface getAbstractBusinessObjectFactory(Class clazz) {
		return (AbstractBusinessObjectFactoryInterface) FactoryFactory.createFactory( FactoryFactory.BUSINESS_OBJECT, clazz );
	}

}

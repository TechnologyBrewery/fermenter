package org.bitbucket.fermenter.stout.factory;

import java.util.ResourceBundle;

import org.bitbucket.fermenter.stout.util.ResourceBundleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for creating new instances of factories.
 */
public class FactoryManager {

	private static final Logger log = LoggerFactory.getLogger( FactoryManager.class );

	public static final String BUSINESS_OBJECT = "business.object";
	public static final String TRANSFER_OBJECT = "transfer.object";
	public static final String SERVICE = "service";
	public static final String SERVICE_DELEGATE = "service.delegate";
	public static final String DAO = "data.access.object";

	private static final String BUNDLE_NAME = "factory";
	private static final ResourceBundleResolver RESOLVER;

	static {
		RESOLVER = new ResourceBundleResolver(BUNDLE_NAME);
	}

	public static Factory createFactory(String factoryType, Class clazz) {
		return createFactory(factoryType, clazz.getPackage().getName());
	}

	public static Factory createFactory(String factoryType, String name) {
		String factoryClassName  = null;
		Factory factory = null;

		try {
			ResourceBundle bundle = RESOLVER.getResourceBundle(name);
			factoryClassName = bundle.getString(factoryType);
			Class factoryClass = Class.forName( factoryClassName );
			factory = (Factory) factoryClass.newInstance();
		} catch(ClassNotFoundException cnfe) {
			log.error( "Invalid factory: " + factoryClassName );
		} catch (InstantiationException e) {
			log.error( "Invalid factory: " + factoryClassName );
		} catch (IllegalAccessException e) {
			log.error( "Invalid factory: " + factoryClassName );
		}

		return factory;
	}

}

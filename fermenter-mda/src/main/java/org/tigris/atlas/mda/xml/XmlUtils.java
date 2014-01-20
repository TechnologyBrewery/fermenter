package org.tigris.atlas.mda.xml;

import org.apache.commons.digester.Digester;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * Contains commonly used XML operations
 */
public class XmlUtils {
	
	private static EntityResolver resolver;
	private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";
	
	static {
	    //create the entity resolver to ensure proper DTD resolution
		//regardless of whether or not the system id specified in an
		//xml file is valid:
		CatalogManager catalogManager = new CatalogManager( ENTITY_RESOLVER_PROPERTIES );
		resolver = new CatalogResolver(catalogManager);
		
	}
	
	/**
	 * Creates a new <tt>Digester</tt> instance that validates against DTDs provided for atlas-mda 
	 * DTDs.
	 * @param errorHandler
	 * @return
	 */
	public static Digester getNewDigester(ErrorHandler errorHandler) {
		//Digester's javadoc specifically suggests that a new instance
		//should be created even though Digester has a clear() method:
		Digester digester = new Digester();
		digester.setEntityResolver(resolver);
		digester.setValidating(true);
		digester.setErrorHandler(errorHandler);
		return digester;
		
	}	
	
}

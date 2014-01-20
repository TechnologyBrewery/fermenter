package org.tigris.atlas.mda.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.tigris.atlas.mda.xml.LoggingErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 * Responsible for maintaining mappings from logical type names to Java types
 * 
 * @author sandrews
 *
 */
public class JavaTypeManager {

	private Map  typeMap = new HashMap();
	private EntityResolver resolver;

	private static Log log = LogFactory.getLog( JavaTypeManager.class );
	
	private static final String ENTITY_RESOLVER_PROPERTIES = "entityResolver.properties";

	private static final String TYPES = "/types.xml";
    private static JavaTypeManager INSTANCE = null;
    
    static {
    	INSTANCE = new JavaTypeManager();
    }
    	
	public static JavaTypeManager getInstance() {
		if ( INSTANCE == null ) {
			throw new RuntimeException("Type Metadata has not been configured!");
		}
		
		return INSTANCE;
	}
	
	private JavaTypeManager() {
		//create the entity resolver to ensure proper DTD resolution
		//regardless of whether or not the system id specified in an
		//xml file is valid:
		CatalogManager catalogManager = new CatalogManager( ENTITY_RESOLVER_PROPERTIES );
		resolver = new CatalogResolver(catalogManager);
		
		load();	
	}
	
		
	/**
	 * Load in the metadata from the associated file
	 *
	 */
	protected void load() {
		LoggingErrorHandler errorHandler = new LoggingErrorHandler(log);
		Digester digester = getNewDigester(resolver, new File( TYPES ), errorHandler);
		
		digester.push( this );
		digester.addCallMethod( "types/type", "addType", 2 );
		digester.addCallParam( "types/type/name", 0 );
		digester.addCallParam( "types/type/java", 1 );
				
		try {
			digester.parse( JavaTypeManager.class.getResourceAsStream( TYPES ) );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
	}
	
	private Digester getNewDigester(EntityResolver resolver, File fileToParse, LoggingErrorHandler errorHandler) {
		//Digester's javadoc specifically suggests that a new instance
		//should be created even though Digester has a clear() method:
		Digester digester = new Digester();
		digester.setEntityResolver(resolver);
		digester.setValidating(true);
		errorHandler.setFileName(fileToParse.getName());
		digester.setErrorHandler(errorHandler);
		return digester;
	}

	protected Map getTypeMap() {
		return typeMap;
	}	
	
	public void addType(String name, String javaClass) {
		getTypeMap().put( name, javaClass );
	}
	
	public static String getJavaType(String name) {
		return (String) getInstance().getTypeMap().get( name );
	}
}

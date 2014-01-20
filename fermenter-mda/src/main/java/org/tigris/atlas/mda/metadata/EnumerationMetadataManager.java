package org.tigris.atlas.mda.metadata;


import java.util.Collections;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.mda.metadata.element.EnumMetadata;
import org.tigris.atlas.mda.metadata.element.Enumeration;
import org.tigris.atlas.mda.metadata.element.EnumerationMetadata;
import org.tigris.atlas.mda.metadata.element.MetadataElement;

/**
 * Responsible for maintaining the list of enumeration metadata elements in the system.
 * 
 * @author sandrews
 *
 */
class EnumerationMetadataManager extends MetadataManager {

    private static EnumerationMetadataManager INSTANCE = null;
	
	private static Log log = LogFactory.getLog( EnumerationMetadataManager.class );
	
	public static EnumerationMetadataManager getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new EnumerationMetadataManager();
		}
		
		return INSTANCE;
	}
	
	private EnumerationMetadataManager() {
		super();				
	}
	
	protected String getMetadataLocation() {
		return "enumerations";
	}

	/**
	 * Answer metadata for a specified enumeration
	 * 
	 * @param name
	 * @return
	 */
	public static Enumeration getEnumerationMetadata(String applicationName, String name) {
		Map enumerationMap = getInstance().getMetadataMap( applicationName );
		return (enumerationMap != null) ? (Enumeration)enumerationMap.get( name ) : null;
	}
	
	/**
	 * Answer the full collection of enumeration metadata entries
	 * 
	 * @return
	 */
	public static Map getEnumerations(String applicationName) {
		Map enumerationMap = getInstance().getMetadataMap( applicationName );
		return (enumerationMap != null) ? enumerationMap : Collections.EMPTY_MAP;
	}
	
	/**
	 * Returns the metadata element by name from any application that is loaded
	 * @param name The name by which to retrieve
	 * @return The <tt>Enumeration</tt> instance for <tt>name</tt> or null
	 */
	public static Enumeration getEnumerationMetadata(String name) {
		Map map = getInstance().getCompleteMetadataMap();
		return (map != null) ? (Enumeration)map.get(name) : null;
	}
	
	public void addEnumeration(EnumerationMetadata e) {
		e.setApplicationName(currentApplication);
		addMetadataElement( e.getName(), (MetadataElement)e );
	}
	
	/**
	 * Initialize the parsing rules for the digester
	 * 
	 * @param digester Used to parse the metadata file into metadata elements
	 */
	protected void initialize(Digester digester) {
		
		digester.addObjectCreate(	"enumeration", EnumerationMetadata.class.getName());
		digester.addCallMethod( 	"enumeration/name", "setName", 0 );
		digester.addCallMethod( 	"enumeration/type", "setType", 0 );
		
		digester.addObjectCreate( 	"enumeration/enums/enum", EnumMetadata.class.getName() );
		digester.addCallMethod( 	"enumeration/enums/enum/name", "setName", 0 );
		digester.addCallMethod( 	"enumeration/enums/enum/value", "setValue", 0 );
		digester.addSetNext( 		"enumeration/enums/enum", "addEnum", EnumMetadata.class.getName() );
		
		digester.addSetNext( 		"enumeration", "addEnumeration", EnumerationMetadata.class.getName() );
		
		if (log.isDebugEnabled()) {
			log.debug("Initialization complete");
		}

	}

}

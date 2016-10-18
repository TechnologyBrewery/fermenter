package org.bitbucket.fermenter.mda.metadata;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metadata.element.MetadataElement;
import org.bitbucket.fermenter.mda.xml.TrackErrorsErrorHandler;
import org.bitbucket.fermenter.mda.xml.XmlUtils;

/**
 * Base class for metadata loading and management across applications
 * 
 * @author Steve Andrews
 *
 */
public abstract class MetadataManager {

    private Map applicationMap = new HashMap();
    private Map completeMetadataMap = new HashMap();
	protected String currentApplication;

	private static Log log = LogFactory.getLog( MetadataManager.class );
	private static final String METADATA_SUFFIX = ".xml";
	
	protected MetadataManager() {
		
	}
	
	/**
	 * Set up the parsing rules for the digester to parse the metadata.
	 * 
	 * @param digester
	 */
	protected abstract void initialize(Digester digester);
	
	/**
	 * Resets the MetadataManger to ensure a clean set of metadata is available.
	 */
	public void reset() {
	    // TODO: see issue #16 and #17 to eliminate the need for this in the next version: 
	    applicationMap = new HashMap();
	    completeMetadataMap = new HashMap();
	}
	
	/**
	 * Validation occurs after the loading of all Metadata to ensure that we can 
	 * access all metadata in a safe fashion, without having to worry to about 
	 * what is already laoded and what needs to be loaded.
	 */
	protected final void validate() {
		Iterator apps = applicationMap.keySet().iterator();
		while( apps.hasNext() ) {
			validateElements( getMetadataMap( (String) apps.next() ).values() );
		}
	}
	
	/**
	 * Performs validation on <tt>Collection</tt> of <tt>MetadataElements</tt>
	 * @param elementCollection The <tt>Collection</tt> to validate
	 */
	public static void validateElements(Collection elementCollection) {
		if ((elementCollection != null) && (elementCollection.size() > 0)) {
			Iterator i = elementCollection.iterator();
			MetadataElement element;
			while (i.hasNext()) {
				element = (MetadataElement)i.next();
				element.validate();
			}
		}
	}
	
	protected void loadMetadata(String appName, String url) {
		if (StringUtils.isBlank(url)) {
			log.error("Metadata for application '" + appName + "' can not be found!  Please ensure the proper jar is on your classpath.");
		
		} else {
			
			
			currentApplication = appName;
			applicationMap.put( currentApplication, new HashMap() );
			
			List resources = null;
			
			try {
				//url = processURL(url);
				resources = getMetadataResources(url);
			} catch (Exception ex) {
				if (log.isInfoEnabled()) {
					log.info("No " + getMetadataLocation() + " metadata found for '" + appName + "', skipping...");
				}
			}
			
			if (resources == null) {
				return;
			}
			
			try {
				Iterator iterator = resources.iterator();
				while( iterator.hasNext() ) {
					
					loadMetadataFile(((URL)iterator.next()).openStream());
				}
			} catch (Exception ex) {
				throw new RuntimeException("Error while loading metadata", ex);
			}
			
			postLoadMetadata();
		}
	}
	
	protected List getMetadataResources(String name) throws IOException, URISyntaxException {	
		List metadataResources = new ArrayList();
		if (name.indexOf(".jar") != -1) {
			name = "jar:" + name + "!/";
			 URL jarUrl = new URL(name);
			JarURLConnection jarConnection = (JarURLConnection)jarUrl.openConnection();
			JarFile file = jarConnection.getJarFile();
			Enumeration e = file.entries();
			while(e.hasMoreElements()){
				JarEntry newEntry = (JarEntry)e.nextElement();
				if( newEntry.getName().startsWith(this.getMetadataLocation() ) && newEntry.getName().endsWith( METADATA_SUFFIX ) ) {
					metadataResources.add( new URL(name + newEntry.getName())  );
				}
			}
		}
		else{
			File metadataDir = new File( new URI(name + this.getMetadataLocation()) );
			File[] files = metadataDir.listFiles();
			File file = null;
			for(int i=0; i<files.length; i++) {
				file = files[i];
				if( file.getName().endsWith( METADATA_SUFFIX ) ) {
					metadataResources.add( file.toURL() );
				}
			}
		}
		return metadataResources;
	}
	
	protected abstract String getMetadataLocation();
	
	private void loadMetadataFile(InputStream stream) throws Exception {
		TrackErrorsErrorHandler handler = new TrackErrorsErrorHandler(log);
		
		Digester digester = XmlUtils.getNewDigester(handler);
		digester.push(this);
		initialize(digester);
		digester.parse(stream);
		
		if (handler.haveErrorsOccurred()) {
			throw new RuntimeException("XML parsing error(s) encountered; check log for details");
		}
	}
	
	protected void postLoadMetadata() {
		
	}

	/**
	 * 
	 * @param applicationName
	 * @return <tt>Map</tt> The metadata map for a specified application
	 */
	protected Map getMetadataMap(String applicationName) {
		return (Map) applicationMap.get( applicationName );
	}	
	
	protected Map getCompleteMetadataMap() {
		return completeMetadataMap;
	}
	
	/**
	 * Add a metadata element to the current application map.  Can only happen during metadata loading.
	 * 
	 * @param name
	 * @param me
	 */
	protected void addMetadataElement(String name, MetadataElement me) {
		getMetadataMap( currentApplication ).put( name, me );
		completeMetadataMap.put(name, me);

	}	

}

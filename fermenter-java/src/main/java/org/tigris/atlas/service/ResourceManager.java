package org.tigris.atlas.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManager {

	private static final String RESOURCES = "/resources.properties";
	private static final Logger LOG = LoggerFactory.getLogger( ResourceManager.class );
	private static boolean INIT = false;

	public static void init() {
		if( ! INIT ) {
			Collection resources = getResources();
			if( resources==null ) {
				INIT = true;
				return;
			}
			else {
				Iterator resourceIter = resources.iterator();
				String resourceName = null;
				Resource resource = null;
				while( resourceIter.hasNext() ) {
					resourceName = (String) resourceIter.next();
					try {
						resource = (Resource) Class.forName( resourceName ).newInstance();
					} catch (InstantiationException e) {
						LOG.error( "Unable to create instance of: " + resourceName, e );
					} catch (IllegalAccessException e) {
						LOG.error( "Unable to create instance of: " + resourceName, e );
					} catch (ClassNotFoundException e) {
						LOG.error( "Unable to create instance of: " + resourceName, e );
					}
					resource.init();
				}
			}

			INIT = true;
		}
	}

	private static Collection getResources() {
		InputStream is = new ResourceManager().getClass().getResourceAsStream( RESOURCES );
		if( is==null ) {
			LOG.info( "No resources.properties to load." );
			return null;
		}
		Properties props = new Properties();
		try {
			props.load( is );
		} catch (IOException e) {
			LOG.error( "Error loading resources.properties.", e );
		}

		return props.values();
	}

}

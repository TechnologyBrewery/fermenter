package org.tigris.atlas.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManager {

	private static Logger logger = LoggerFactory.getLogger( ConfigurationManager.class );

	private static final String CONFIG_LOCATION_PROPERTY = "config.location";

	public static Properties getConfiguration(String propertyFileName) {
		String location = System.getProperty( CONFIG_LOCATION_PROPERTY );

		if( StringUtils.isBlank( location ) ) {
			IllegalStateException ex =  new IllegalStateException( "Invalid configuration.  Missing system property: " + CONFIG_LOCATION_PROPERTY );
			logger.error( "org.tigris.atlas.config.ConfigurationManager", ex );
			throw ex;
		}

		InputStream is = null;
		try {
			is = new FileInputStream( location + propertyFileName );
			Properties properties = new Properties();
			properties.load( is );
			return properties;
		}
		catch(FileNotFoundException fnfe) {
			logger.error( "org.tigris.atlas.config.ConfigurationManager", fnfe );
			throw new IllegalStateException( "Unable to locate properties file: " + location + propertyFileName );
		}
		catch(IOException ioe) {
			logger.error( "org.tigris.atlas.config.ConfigurationManager", ioe );
			throw new IllegalStateException( "Unable to open properties file: " + location + propertyFileName );
		}
		finally {
			try {
				if( is != null)
					is.close();
			}
			catch(IOException ignore) {
			}
		}
	}
}

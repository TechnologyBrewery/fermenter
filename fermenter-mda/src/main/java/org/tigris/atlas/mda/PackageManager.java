package org.tigris.atlas.mda;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PackageManager {
	
	private static final Log LOG = LogFactory.getLog(PackageManager.class.getName());
	
	private static PackageManager INSTANCE;
	private Map applicationToBasePackage = new HashMap(10);
	
	static {
		INSTANCE = new PackageManager();
	}
	
	private PackageManager() {
	}
	
	/**
	 * Returns a base package for a given application name
	 * @param applicationName The name for which to look
	 * @return The returned base package or null if one does not exist
	 */
	public static String getBasePackage(String applicationName) {
		return (String)INSTANCE.applicationToBasePackage.get(applicationName);
	}
	
	public static void addMapping(String applicationName, URL url) {		
		try {
			InputStream stream = processURL(url);
			Properties props = new Properties();
			props.load(stream);
			
			INSTANCE.applicationToBasePackage.put(applicationName, props.getProperty("basePackage"));
		} catch (IOException ex) {
			LOG.warn("Could not find package properties for application '" + applicationName + "' at URL " + url.getPath());
		}
	}
	
	public static void addMapping(String applicationName, String basePackage) {
		INSTANCE.applicationToBasePackage.put(applicationName, basePackage);
	}
	
	private static InputStream processURL(URL url) throws IOException{
		String sUrl = url.toString();
		if (sUrl.indexOf(".jar") != -1) {
			sUrl = "jar:" + url + "!/" + "package.properties";
			 URL jarUrl = new URL(sUrl);
			JarURLConnection jarConnection = (JarURLConnection)jarUrl.openConnection();
			return jarConnection.getInputStream();
		}
		
		return url.openStream();
	}
	
}

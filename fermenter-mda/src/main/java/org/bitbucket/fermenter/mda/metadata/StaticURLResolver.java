package org.bitbucket.fermenter.mda.metadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

public class StaticURLResolver implements MetadataURLResolver {

	public List getMetadataURLs(Properties properties) {
		String sMetadataLocations = properties.getProperty("metadata.locations");
		Set metadataLocations = new HashSet();
		
		StringTokenizer tok = new StringTokenizer(sMetadataLocations, ";", false);
		while (tok.hasMoreElements()) {
			metadataLocations.add(tok.nextToken());
		}
		
		List urls = new ArrayList();
		for (Iterator i = metadataLocations.iterator(); i.hasNext();) {
			String appName = (String) i.next();
			MetadataURL url = new MetadataURL();
			url.setApplicationName(appName);
			url.setUrl(properties.getProperty("metadata."+appName));
			urls.add(url);
		}
		return urls;
	}

}

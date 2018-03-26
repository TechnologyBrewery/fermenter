package org.bitbucket.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

public class DefaultUrlResolver implements MetadataUrlResolver {

    public List<MetadataUrl> getMetadataURLs(Properties properties) {
		String metadataLocations = properties.getProperty(METADATA_LOCATIONS);
		Set<String> metadataLocationsSet = new HashSet<>();
		
		StringTokenizer tok = new StringTokenizer(metadataLocations, ";", false);
		while (tok.hasMoreElements()) {
			metadataLocationsSet.add(tok.nextToken());
		}
		
		List<MetadataUrl> urls = new ArrayList<>();
		for (String metadataLocation : metadataLocationsSet) {
			MetadataUrl url = new MetadataUrl();
			url.setArtifactId(metadataLocation);
			url.setUrl(properties.getProperty("metadata." + metadataLocation));
			urls.add(url);
		}
		
		return urls;
	}

}

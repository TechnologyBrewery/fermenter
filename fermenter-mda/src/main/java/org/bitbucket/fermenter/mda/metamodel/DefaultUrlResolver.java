package org.bitbucket.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Lookup mechanism is for resolving metadata locations.
 * 
 * NOTE: This has been pulled forward from the legacy approach. There is note in the generator to come back and refresh
 * this once everything is done as it at a minimum convoluted.
 * 
 */
public class DefaultUrlResolver implements MetadataUrlResolver {

    /**
     * {@inheritDoc}
     */
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

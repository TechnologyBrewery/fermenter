package org.bitbucket.fermenter.mda.metamodel;

import java.util.List;
import java.util.Properties;

public interface MetadataUrlResolver {
    
    static final String METADATA_LOCATION_PREFIX = "metadata.";
    static final String METADATA_LOCATIONS = METADATA_LOCATION_PREFIX + "locations";    

	List<MetadataUrl> getMetadataURLs(Properties properties);
	
}

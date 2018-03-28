package org.bitbucket.fermenter.mda.metamodel;

import java.util.List;
import java.util.Properties;

/**
 * Defines the contract for a resolver that can provide access to metadata.
 * 
 * NOTE: This is a legacy concept that will likely be removed completely once we convert everything
 */
public interface MetadataUrlResolver {
    
    static final String METADATA_LOCATION_PREFIX = "metadata.";
    static final String METADATA_LOCATIONS = METADATA_LOCATION_PREFIX + "locations";    

	List<MetadataUrl> getMetadataURLs(Properties properties);
	
}

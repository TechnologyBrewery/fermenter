package org.bitbucket.fermenter.mda.metamodel;

import java.util.List;
import java.util.Properties;

public interface MetadataUrlResolver {

	public List<MetadataUrl> getMetadataURLs(Properties properties);
	
}

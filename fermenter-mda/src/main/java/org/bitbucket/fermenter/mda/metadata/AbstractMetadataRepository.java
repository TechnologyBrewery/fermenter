package org.bitbucket.fermenter.mda.metadata;

import java.util.Properties;

import org.bitbucket.fermenter.mda.metamodel.MetadataRepository;

/**
 * Provides common methods needed for a metadata repository.
 * 
 */
public abstract class AbstractMetadataRepository implements MetadataRepository {
    
    /** Indicates that all metadata should be used. */
    static final String ALL_METADATA_CONTEXT = "all";
    
    /** Indicates that only local metadata should be used (e.g., current application.name). */
    static final String LOCAL_METADATA_CONTEXT = "local";

	protected String applicationName;

	/**
	 * Instantiates this instance with any applicable properties.
	 * 
	 * @param properties
	 *            any applicable properties
	 */
	protected AbstractMetadataRepository(Properties props) {
		if (props != null) {
			applicationName = props.getProperty("application.name");
		}

	}

	/**
	 * Returns the application name.
	 * @return application name
	 */
	public String getApplicationName() {
		return applicationName;
	}

}

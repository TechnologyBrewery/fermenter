package org.bitbucket.fermenter.mda.metadata;

import java.util.Properties;

/**
 * Provides common methods needed for a metadata repository.
 */
public abstract class AbstractMetadataRepository {

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
	 * Loads all metadata and will be invoked immediately after instantiating this instance in the templated workflow.
	 * 
	 * @param properties
	 *            any applicable properties
	 */
	public abstract void load(Properties properties);

	/**
	 * Validates all metadata and will be invoked immediately after loading in the templated workflow.
	 * 
	 * @param properties
	 *            any applicable properties
	 */
	public abstract void validate(Properties properties);

	/**
	 * Returns the application name.
	 * @return application name
	 */
	public String getApplicationName() {
		return applicationName;
	}

}

package org.bitbucket.fermenter.mda.metamodel;

/**
 * Provides common methods needed for a metadata repository.
 */
public abstract class AbstractMetadataRepository implements MetadataRepository {    

	protected String basePackage;

	/**
	 * Instantiates this instance with any applicable properties.
	 * 
	 * @param properties
	 *            any applicable properties
	 */
	public AbstractMetadataRepository(String basePackage) {
		this.basePackage = basePackage;

	}

	/**
	 * Returns the base package under which this repository was instantiated.
	 * @return base package name
	 */
	public String getBasePackage() {
		return basePackage;
	}

}

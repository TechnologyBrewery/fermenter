package org.bitbucket.fermenter.mda.generator;

/**
 * Defines contract for generating files.
 */
public interface Generator {
    
    /**
     * Called to generate a file.
     * @param context contextual information about the overall fermenter-mda environment
     * @throws GenerationException any exceptional condition encountered during generation
     */
	void generate(GenerationContext context) throws GenerationException;
	
	/**
	 * Allows information about metadata to be passed into a generator.
	 * @param metadataContext the desired information (e.g., all, applicationName)
	 */
	void setMetadataContext(String metadataContext);
	
}

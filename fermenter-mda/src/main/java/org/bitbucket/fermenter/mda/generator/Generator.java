package org.bitbucket.fermenter.mda.generator;

/**
 * Defines contract for generating files.
 */
public interface Generator {

    /**
     * Called to generate a file.
     * 
     * @param context
     *            contextual information about the overall fermenter-mda environment
     */
    void generate(GenerationContext context);

    /**
     * Allows information about metadata to be passed into a generator.
     * 
     * @param metadataContext
     *            the desired information (e.g., all, artifactId)
     */
    void setMetadataContext(String metadataContext);

}

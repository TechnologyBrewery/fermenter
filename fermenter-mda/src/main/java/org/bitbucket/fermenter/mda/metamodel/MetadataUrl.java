package org.bitbucket.fermenter.mda.metamodel;

import com.google.common.base.MoreObjects;

/**
 * Stores information about a specific location of metadata.
 */
public class MetadataUrl {

    private String artifactId;
    private String url;
    
    /**
     * New instance
     * @param artifactId artifact id
     * @param url url to artifact id
     */
    public MetadataUrl(String artifactId, String url) {
        this.artifactId = artifactId;
        this.url = url;
    }

    /**
     * Returns the artifactId for this resource (jar or local module).
     * @return artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets the artifactId for this resource (jar or local module).
     * @param artifactId artifact id
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Returns the location of the artifact (jar or path to directory).
     * @return location
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the location of the artifact (jar or path to directory).
     * @param url location
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("artifactId", artifactId).add("url", url).toString();
    }

}

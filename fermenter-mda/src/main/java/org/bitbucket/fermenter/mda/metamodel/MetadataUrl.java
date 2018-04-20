package org.bitbucket.fermenter.mda.metamodel;

import com.google.common.base.MoreObjects;

/**
 * Stores information about a specific location of metadata.
 */
public class MetadataUrl {

    private static final String URL = "url";
    private static final String ARTIFACT_ID = "artifactId";
    private String artifactId;
    private String url;

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
        return MoreObjects.toStringHelper(this).add(ARTIFACT_ID, artifactId).add(URL, url).toString();
    }

}

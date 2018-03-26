package org.bitbucket.fermenter.mda.metamodel;

import com.google.common.base.MoreObjects;

public class MetadataUrl {

    private String artifactId;
    private String url;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("artifactId", artifactId).add("url", url).toString();
    }

}

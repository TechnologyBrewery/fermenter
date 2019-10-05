package org.bitbucket.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides configuration information to the a {@link ModelInstanceRepository}.
 */
public class ModelRepositoryConfiguration {

    private String artifactId;
    private String basePackage;
    private List<String> targetModelInstances = new ArrayList<>();
    private Map<String, ModelInstanceUrl> metamodelInstanceLocations = new HashMap<>();
    
    /**
     * The name (i.e., artifact id) of the current project.
     * @return artifact id
     */
    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public List<String> getTargetModelInstances() {
        return targetModelInstances;
    }
    
    public void setTargetModelInstances(List<String> targetModelInstances) {
        this.targetModelInstances = targetModelInstances;
    }

    public Map<String, ModelInstanceUrl> getMetamodelInstanceLocations() {
        return metamodelInstanceLocations;
    }

    public void setMetamodelInstanceLocations(Map<String, ModelInstanceUrl> metamodelInstanceLocations) {
        this.metamodelInstanceLocations = metamodelInstanceLocations;
    }
}

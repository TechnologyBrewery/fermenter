package org.bitbucket.fermenter.mda.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides configuration information to the a {@link ModelInstanceRepository}.
 */
public class ModelRepositoryConfiguration {
    
    /** Indicates that all metadata should be used. */
    public static final String ALL_METADATA_CONTEXT = "all";

    /** Indicates that only local metadata should be used (e.g., current application.name). */
    public static final String LOCAL_METADATA_CONTEXT = "local";

    /** Indicates that a target set of metadata for a specific list of artifactIds should be used. */
    public static final String TARGETED_METADATA_CONTEXT = "targeted"; 

    private String currentApplicationName;
    private String basePackage;
    private List<String> targetModelInstances = new ArrayList<>();
    private Map<String, MetadataUrl> metamodelInstanceLocations = new HashMap<>();
    
    /**
     * The name (i.e., artifact id) of the current project.
     * @return artifact id
     */
    public String getCurrentApplicationName() {
        return currentApplicationName;
    }
    
    public void setCurrentApplicationName(String currentApplicationName) {
        this.currentApplicationName = currentApplicationName;
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

    public Map<String, MetadataUrl> getMetamodelInstanceLocations() {
        return metamodelInstanceLocations;
    }

    public void setMetamodelInstanceLocations(Map<String, MetadataUrl> metamodelInstanceLocations) {
        this.metamodelInstanceLocations = metamodelInstanceLocations;
    }

}

package org.bitbucket.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds generation target information to support MDA execution. A target
 * represents the application of a generator to a specific template with
 * additional information to control where the generated file will live, whether
 * to overwrite an existing file, etc.
 */
public class Target implements ValidatedElement {

	@JsonProperty(required = true)
	private String name;

	@JsonProperty(required = true)
	private String templateName;

	@JsonProperty(required = true)
	private String outputFile;

	@JsonProperty(required = true)
	private String generator;

	@JsonProperty(required = false, defaultValue = "all")
    private String metadataContext;
	
	@JsonProperty(required = false, defaultValue = "false")
	private boolean overwritable;

    @JsonProperty(defaultValue = "false")
    private boolean versioned;
	
	@JsonProperty(required = false, defaultValue = "main")
    private String artifactType;	

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

    public boolean isVersioned() { return versioned; }

    public void setVersioned(boolean versioned) { this.versioned = versioned; }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public String getMetadataContext() {
        return metadataContext;
    }

    public void setMetadataContext(String metadataContext) {
        this.metadataContext = metadataContext;
    }

    public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public boolean isOverwritable() {
		return overwritable;
	}

	public void setOverwritable(boolean overwritable) {
		this.overwritable = overwritable;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
		
    public String getArtifactType() {
        return (artifactType != null) ? artifactType : "main";
    }

    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
    }

    /**
     * {@inheritDoc}
     */
    public String getSchemaFileName() {
        return "fermenter-2-target-schema.json";
    }
}

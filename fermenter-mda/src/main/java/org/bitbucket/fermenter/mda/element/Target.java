package org.bitbucket.fermenter.mda.element;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds generation target information to support MDA execution. A target
 * represents the application of a generator to a specific template with
 * additional information to control where the generated file will live, whether
 * to overwrite an existing file, etc.
 */
public class Target {

	@JsonProperty(required = true)
	private String name;

	@JsonProperty(required = true)
	private String templateName;

	@JsonProperty(required = true)
	private String outputFile;

	@JsonProperty(required = true)
	private String generator;

	@JsonProperty(required = false, defaultValue = "false")
	private boolean overwritable;

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutputFile() {
		return outputFile;
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
}

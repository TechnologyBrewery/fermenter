package org.bitbucket.fermenter.mda.element;

public class Target {

	private String name;
	private String templateName;
	private String outputFile;
	private String generator;
	private String metadataContext;
	private boolean overwritable;
	private boolean append;
	
	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public String getGenerator() {
		return generator;
	}
	
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
	public String getMetadataContext() {
        return metadataContext;
    }

    public void setMetadataContext(String metadataContext) {
        this.metadataContext = metadataContext;
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

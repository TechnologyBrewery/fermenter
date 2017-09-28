package org.bitbucket.fermenter.mda.generator;

import java.io.File;

import org.apache.velocity.app.VelocityEngine;
import org.bitbucket.fermenter.mda.element.Target;

public final class GenerationContext {

	private String templateName;
	private String outputFile;
	private boolean overwritable;
	private boolean append;
	private String basePackage;
	private String projectName;
	private File basedir;
	private File mainSourceDirectory;
	private File generatedSourceDirectory;
	private VelocityEngine engine;
	private String groupId;
	private String artifactId;
	private String version;
	
	public VelocityEngine getEngine() {
		return engine;
	}

	public void setEngine(VelocityEngine engine) {
		this.engine = engine;
	}

	public GenerationContext() {
		super();
	}
	
	public GenerationContext(Target target) {
		this();
		
		this.templateName = target.getTemplateName();
		this.outputFile = target.getOutputFile();
		this.overwritable = target.isOverwritable();
	}
	
	public File getGeneratedSourceDirectory() {
		return generatedSourceDirectory;
	}
	
	public void setGeneratedSourceDirectory(File generatedSourceDirectory) {
		this.generatedSourceDirectory = generatedSourceDirectory;
	}
	
	public File getMainSourceDirectory() {
		return mainSourceDirectory;
	}
	
	public void setMainSourceDirectory(File mainSourceDirectory) {
		this.mainSourceDirectory = mainSourceDirectory;
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

	public String getBasePackage() {
		return basePackage;
	}
	
	public String getBasePackageAsPath() {
		return getBasePackage().replace('.', '/');
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public File getBasedir() {
		return basedir;
	}

	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}		
	
}

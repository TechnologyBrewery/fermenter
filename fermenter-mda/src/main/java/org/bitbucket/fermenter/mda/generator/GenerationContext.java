package org.bitbucket.fermenter.mda.generator;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.bitbucket.fermenter.mda.element.Target;

public final class GenerationContext {

    private String templateName;
    private String outputFile;
    private boolean overwritable;
    private String artifactType;
    private boolean append;
    private String basePackage;
    private File mainSourceDirectory;
    private File generatedSourceDirectory;
    private File generatedTestSourceDirectory;
    private File testSourceDirectory;
    private VelocityEngine engine;
    private String groupId;
    private String artifactId;
    private String version;
    private String descriptiveName;
    private String scmUrl;
    private Map<String, String> propertyVariables;

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
        this.artifactType = target.getArtifactType();
    }

    public File getGeneratedSourceDirectory() {
        return generatedSourceDirectory;
    }

    public void setGeneratedSourceDirectory(File generatedSourceDirectory) {
        this.generatedSourceDirectory = generatedSourceDirectory;
    }

    public File getGeneratedTestSourceDirectory() {
        return generatedTestSourceDirectory;
    }

    public void setGeneratedTestSourceDirectory(File generatedTestSourceDirectory) {
        this.generatedTestSourceDirectory = generatedTestSourceDirectory;
    }

    public File getMainSourceDirectory() {
        return mainSourceDirectory;
    }

    public void setMainSourceDirectory(File mainSourceDirectory) {
        this.mainSourceDirectory = mainSourceDirectory;
    }

    public File getTestSourceDirectory() {
        return testSourceDirectory;
    }

    public void setTestSourceDirectory(File testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
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

    public String getDescriptiveName() {
        return descriptiveName;
    }

    public void setDescriptiveName(String descriptiveName) {
        this.descriptiveName = descriptiveName;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    /**
     * Returns the property variables or an empty map for null-safe use.
     * 
     * @return property variables.
     */
    public Map<String, String> getPropertyVariables() {
        return propertyVariables != null ? propertyVariables : Collections.emptyMap();
    }

    public void setPropertyVariables(Map<String, String> propertyVariables) {
        this.propertyVariables = propertyVariables;
    }

}

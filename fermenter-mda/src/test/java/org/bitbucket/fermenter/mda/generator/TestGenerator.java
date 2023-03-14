package org.bitbucket.fermenter.mda.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class TestGenerator extends AbstractGenerator {
    private final int size;
    private final boolean overwritable;
    private final String name;

    public TestGenerator(int fileSize, boolean overwritable) {
        this.size = fileSize;
        this.overwritable = overwritable;
        name = UUID.randomUUID().toString();
    }

    @Override
    protected String getOutputSubFolder() {
        return "test-generator-output/";
    }

    @Override
    public void generate(GenerationContext context) {
        writeTemplate();
        context.setOverwritable(overwritable);
        context.setTemplateName(getTemplateName());
        context.setOutputFile(getOutputName());
        super.generateFile(context, new VelocityContext());
    }

    @Override
    protected File getBaseFile(GenerationContext gc) {
        return new File("target");
    }

    private void writeTemplate() {
        try {
            Path templateFile = getTemplatePath();
            String randomData = RandomStringUtils.randomAlphanumeric(size);
            Files.writeString(templateFile, randomData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write test template file", e);
        }
    }

    public Path getTemplatePath() {
        return Paths.get("target", "test-classes", getTemplateName());
    }

    public Path getOutputPath() {
        return getBaseFile(null).toPath()
            .resolve(getOutputSubFolder())
            .resolve(getOutputName());
    }

    private String getTemplateName() {
        return name + "-template.vm" ;
    }

    private String getOutputName() {
        return name + "-output" ;
    }

    public long getFileSize() {
        return size;
    }
}

package org.technologybrewery.fermenter.brett;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;

import java.io.File;

/**
 * Supports the generation of resources at the root-level of a Habushu project.
 */
public class HabushuRootLevelResourceGenerator extends AbstractPythonGenerator {

    @Override
    protected String getOutputSubFolder() {
        return "";
    }

    @Override
    public void generate(GenerationContext generationContext) {
        VelocityContext velocityContext = populateCommonPythonContext(null, generationContext);
        generateFile(generationContext, velocityContext);
    }

    @Override
    protected File getBaseFile(GenerationContext gc) {
        return gc.getProjectDirectory();
    }
}

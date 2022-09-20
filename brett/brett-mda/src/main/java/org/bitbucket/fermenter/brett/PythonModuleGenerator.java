package org.bitbucket.fermenter.brett;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;

/**
 * Supports the generation of Python modules within a Habushu-compliant project. Later iterations may
 * seek to add additional logic to more facilitate generation.
 */
public class PythonModuleGenerator extends AbstractPythonGenerator {
    @Override
    protected String getOutputSubFolder() {
        return "";
    }

    @Override
    public void generate(GenerationContext generationContext) {
        VelocityContext velocityContext = populateCommonPythonContext(null, generationContext);
        generateFile(generationContext, velocityContext);
    }
}

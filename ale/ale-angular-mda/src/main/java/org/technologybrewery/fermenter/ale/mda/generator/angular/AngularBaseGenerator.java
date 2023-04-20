package org.technologybrewery.fermenter.ale.mda.generator.angular;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;

public class AngularBaseGenerator extends AbstractGenerator {

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

    @Override
    public void generate(GenerationContext context) {
        VelocityContext vc = new VelocityContext();
        String fileName = context.getOutputFile();
        context.setOutputFile(fileName);
        generateFile(context, vc);
    }
}

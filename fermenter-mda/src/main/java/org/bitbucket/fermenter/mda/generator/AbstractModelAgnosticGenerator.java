package org.bitbucket.fermenter.mda.generator;

import org.apache.velocity.VelocityContext;

/**
 * Generator that does not rely on the presence of any information contained within the meta-model.
 */
public abstract class AbstractModelAgnosticGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) throws GenerationException {
        VelocityContext vc = new VelocityContext();
        vc.put("basePackage", context.getBasePackage());
        vc.put("artifactId", context.getArtifactId());

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        fileName = replaceArtifactId(fileName, context.getArtifactId());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }
}

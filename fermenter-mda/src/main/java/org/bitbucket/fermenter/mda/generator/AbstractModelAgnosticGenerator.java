package org.bitbucket.fermenter.mda.generator;

import org.apache.velocity.VelocityContext;

/**
 * Generator that does not rely on the presence of any information contained within the meta-model.
 */
public abstract class AbstractModelAgnosticGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) {
        VelocityContext vc = getNewVelocityContext(context);
        vc.put("basePackage", context.getBasePackage());

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        fileName = replaceArtifactId(fileName, context.getArtifactId());
        fileName = replaceCapitalizedCamelCasedArtifactId(fileName, (String)vc.get(CAPITALIZED_CAMEL_CASED_ARTIFACT_ID));
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }
}

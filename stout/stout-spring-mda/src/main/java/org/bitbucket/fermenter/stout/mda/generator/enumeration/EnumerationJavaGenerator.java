package org.bitbucket.fermenter.stout.mda.generator.enumeration;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.stout.mda.JavaEnumeration;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public class EnumerationJavaGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();
        MetadataRepository metadataRepository = MetadataRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Map<String, Enumeration> enumerations = metadataRepository.getEnumerationsByMetadataContext(metadataContext,
                currentApplication);

        JavaEnumeration javaEnumeration;
        VelocityContext vc;
        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());

        for (Enumeration enumeration : enumerations.values()) {
            javaEnumeration = new JavaEnumeration(enumeration);

            vc = new VelocityContext();
            vc.put("enumeration", javaEnumeration);
            vc.put("basePackage", context.getBasePackage());

            fileName = replaceEnumerationName(basefileName, enumeration.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}

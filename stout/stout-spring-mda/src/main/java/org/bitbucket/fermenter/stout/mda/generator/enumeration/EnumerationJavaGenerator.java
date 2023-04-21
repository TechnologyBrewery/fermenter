package org.technologybrewery.fermenter.stout.mda.generator.enumeration;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.Enumeration;
import org.technologybrewery.fermenter.stout.mda.JavaEnumeration;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

public class EnumerationJavaGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
        Map<String, Enumeration> enumerations = metadataRepository.getEnumerations(metadataRepository.getBasePackage());

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

            fileName = replace("enumerationName", basefileName, enumeration.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}

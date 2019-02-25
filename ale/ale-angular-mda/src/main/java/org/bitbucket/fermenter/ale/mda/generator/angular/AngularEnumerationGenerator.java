package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.BaseEnumertionDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;


public class AngularEnumerationGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);
        Map<String, Enumeration> enumerations = metadataRepository.getEnumerations(metadataRepository.getBasePackage());

        BaseEnumertionDecorator angularEnumeration;
        VelocityContext vc;
        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());

        for (Enumeration enumeration : enumerations.values()) {
            angularEnumeration = new BaseEnumertionDecorator(enumeration);

            vc = new VelocityContext();
            vc.put("enumeration", angularEnumeration);
            vc.put("basePackage", context.getBasePackage());

            fileName = replaceEnumerationName(basefileName, enumeration.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

}
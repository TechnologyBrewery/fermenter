package org.technologybrewery.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseEnumertionDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Enumeration;
import com.google.common.base.CaseFormat;


public class AngularEnumerationGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        Map<String, Enumeration> enumerations = metadataRepository.getEnumerationsByContext(metadataContext);
        

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

            fileName = replaceEnumerationName(basefileName, getNameLowerHyphen(enumeration.getName()));
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

    private String getNameLowerHyphen(String nameUpperCamel) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, nameUpperCamel);
    }

}

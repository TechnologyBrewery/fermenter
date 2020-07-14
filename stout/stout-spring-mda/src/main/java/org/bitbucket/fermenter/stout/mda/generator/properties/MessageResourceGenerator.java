package org.bitbucket.fermenter.stout.mda.generator.properties;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.MessageGroup;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public class MessageResourceGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);

        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());

        String basePackage = metadataRepository.getBasePackage();
        Map<String, MessageGroup> messageGroups = metadataRepository.getMessageGroups(basePackage);
        for (MessageGroup messageGroup : messageGroups.values()) {
            fileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
            fileName = replaceMessageGroupName(fileName, messageGroup.getName());
            context.setOutputFile(fileName);

            VelocityContext vc = new VelocityContext();
            vc.put("messages", messageGroup);
            generateFile(context, vc);
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_RESOURCES;
    }
}

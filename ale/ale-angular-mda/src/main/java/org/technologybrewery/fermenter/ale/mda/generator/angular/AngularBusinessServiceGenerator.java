package org.technologybrewery.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.service.AbstractServiceGenerator;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.Service;

public class AngularBusinessServiceGenerator extends AbstractServiceGenerator {

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);

        String baseFileName = context.getOutputFile();

        // Key piece - need to generate for all artifacts and WITHOUT having a
        // local set of entities.
        for (String artifactId : metadataRepository.getArtifactIds()) {
            Map<String, Service> services = metadataRepository.getServicesByArtifactId(artifactId);
            if (services != null) {
                for (Service service : services.values()) {
                    AngularService angularService = new AngularService(service);
                    context.setArtifactId(artifactId);
                    VelocityContext vc = getNewVelocityContext(context);
                    populateVelocityContext(vc, angularService, context);

                    // KEY piece - need to set the entity name to be
                    // lower-hyphen for the file name
                    String fileName = replaceServiceName(baseFileName, angularService.getNameLowerHyphen());
                    context.setOutputFile(fileName);

                    generateFile(context, vc);
                }
            }
        }
    }

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

    @Override
    protected void populateVelocityContext(VelocityContext vc, Service service, GenerationContext generationContext) {
        AngularService angularService = (AngularService) service;
        vc.put("service", angularService);
    }

}

package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.service.AbstractServiceGenerator;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Service;

public class AngularBusinessServiceGenerator extends AbstractServiceGenerator {
    private static final String BASE_PACKAGE = "basePackage";
    protected static final String SERVICE = "service";

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);

        String fileName;
        String baseFileName = context.getOutputFile();
        baseFileName = replaceBasePackage(baseFileName, context.getBasePackageAsPath());

        for (String artifactId : metadataRepository.getArtifactIds()) {
            Map<String, Service> services = metadataRepository.getServicesByArtifactId(artifactId);
            if (services != null) {
                for (Service service : services.values()) {
                    AngularService angularService = new AngularService(service, artifactId);
                    VelocityContext vc = new VelocityContext();
                    vc.put(SERVICE, angularService);
                    vc.put(BASE_PACKAGE, context.getBasePackage());

                    fileName = replaceServiceName(baseFileName, angularService.getNameLowerHypen());
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
        // nothing to do as this is just done in the generate method for simplicity.
    }

}

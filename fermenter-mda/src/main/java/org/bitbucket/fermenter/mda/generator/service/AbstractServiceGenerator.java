package org.bitbucket.fermenter.mda.generator.service;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Service;

/**
 * Iterates through each service in the meta-model and enables the generation of a single file for each service.
 */
public abstract class AbstractServiceGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();
        MetadataRepository metadataRepository = MetadataRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);

        Map<String, Service> services = metadataRepository.getServicesByMetadataContext(metadataContext,
                currentApplication);

        String fileName;
        String baseFileName = context.getOutputFile();
        baseFileName = replaceBasePackage(baseFileName, context.getBasePackageAsPath());

        for (Service service : services.values()) {
            VelocityContext vc = new VelocityContext();
            populateVelocityContext(vc, service, context);

            fileName = replaceServiceName(baseFileName, service.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    /**
     * Enables subclasses to add any additional metadata to the provided {@link VelocityContext} to facilitate the
     * generation of the provided {@link Service}.
     * 
     * @param vc
     * @param service
     * @param generationContext
     */
    protected abstract void populateVelocityContext(VelocityContext vc, Service service,
            GenerationContext generationContext);

}

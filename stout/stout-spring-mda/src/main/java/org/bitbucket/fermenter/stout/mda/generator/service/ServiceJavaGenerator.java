package org.bitbucket.fermenter.stout.mda.generator.service;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaService;
import org.bitbucket.fermenter.stout.mda.generator.AbstractJavaGenerator;

/**
 * Iterates through service instances, passing {@link JavaService}s instance to the templates.
 *
 */
public class ServiceJavaGenerator extends AbstractJavaGenerator {

    /**
     * {@inheritDoc}
     */
    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();
        MetadataRepository metadataRepository = MetadataRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);

        Map<String, Service> services = metadataRepository.getServicesByMetadataContext(metadataContext,
                currentApplication);

        JavaService javaService;
        VelocityContext vc;
        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());

        for (Service service : services.values()) {
            javaService = new JavaService(service);

            vc = new VelocityContext();
            vc.put("service", javaService);
            vc.put("basePackage", context.getBasePackage());
            vc.put("artifactId", context.getArtifactId());
            vc.put("version", context.getVersion());

            fileName = replaceServiceName(basefileName, service.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

}

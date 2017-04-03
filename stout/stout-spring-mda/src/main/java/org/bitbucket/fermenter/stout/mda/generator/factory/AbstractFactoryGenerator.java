package org.bitbucket.fermenter.stout.mda.generator.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaElementUtils;
import org.bitbucket.fermenter.stout.mda.JavaService;

public abstract class AbstractFactoryGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();

        VelocityContext vc = new VelocityContext();
        vc.put("prefix", context.getBasePackage());
        vc.put("basePackage", context.getBasePackage());

        MetadataRepository metadataRepository = MetadataRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);

        Map<String, Entity> entityMap = metadataRepository.getEntitiesByMetadataContext(metadataContext,
                currentApplication);
        Collection<Entity> entities = entityMap.values();
        vc.put("entities", entities);

        Map<String, Service> serviceMap = metadataRepository.getServicesByMetadataContext(metadataContext,
                currentApplication);
        Collection<String> serviceNames;
        Collection<Service> services;

        serviceNames = serviceMap.keySet();
        services = serviceMap.values();

        Collection<Service> javaServices = new HashSet<Service>();
        for (Iterator<Service> i = services.iterator(); i.hasNext();) {
            Service service = (Service) i.next();
            javaServices.add(new JavaService(service));
        }
        vc.put("serviceNames", serviceNames);
        vc.put("services", javaServices);
        vc.put("baseJndiName", JavaElementUtils.getBaseJndiName(context.getBasePackage()));

        vc.put("pathPrefix", context.getBasePackageAsPath());

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        fileName = replaceProjectName(fileName, context.getProjectName());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }

}

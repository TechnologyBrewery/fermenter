package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Collection;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.MetadataRepositoryManager;

public abstract class AbstractAllEntitiesAwareGenerator extends AbstractGenerator {

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

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }

}

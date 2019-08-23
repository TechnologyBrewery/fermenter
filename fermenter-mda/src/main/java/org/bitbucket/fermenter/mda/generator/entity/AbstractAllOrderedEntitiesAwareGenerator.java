package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

/**
 * Provides foundational functionality to generate across "all entities" with ordering based on dependencies within the
 * entities (e.g., referenced objects first, leaf nodes last). Otherwise has the same characteristics as
 * {@link AbstractAllEntitiesAwareGenerator}.
 *
 */
public abstract class AbstractAllOrderedEntitiesAwareGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) {
        String currentApplication = context.getArtifactId();

        VelocityContext vc = new VelocityContext();
        vc.put("prefix", context.getBasePackage());
        vc.put("basePackage", context.getBasePackage());

        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);

        Set<Entity> entities = metadataRepository.getEntitiesByDependencyOrder(metadataContext, currentApplication);
        vc.put("entities", entities);

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }

}

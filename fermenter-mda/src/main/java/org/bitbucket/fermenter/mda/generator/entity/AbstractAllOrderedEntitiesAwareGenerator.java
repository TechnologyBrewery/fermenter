package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;

/**
 * Provides foundational functionality to generate across "all entities" with ordering based on dependencies within the
 * entities (e.g., referenced objects first, leaf nodes last). Otherwise has the same characteristics as
 * {@link AbstractAllEntitiesAwareGenerator}.
 *
 */
public abstract class AbstractAllOrderedEntitiesAwareGenerator extends AbstractGenerator {

    /**
     * Gets all entities for this the target's context in their dependency order.
     * 
     * {@inheritDoc}
     */
    public void generate(GenerationContext context) {
        VelocityContext vc = new VelocityContext();
        vc.put("prefix", context.getBasePackage());
        vc.put("basePackage", context.getBasePackage());

        DefaultModelInstanceRepository metamodelRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);

        Set<Entity> entities = metamodelRepository.getEntitiesByDependencyOrder(metadataContext);
        vc.put("entities", entities);

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }

}

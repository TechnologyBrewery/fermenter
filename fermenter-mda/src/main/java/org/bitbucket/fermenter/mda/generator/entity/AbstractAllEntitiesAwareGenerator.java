package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Collection;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;

/**
 * Abstract generator that provides all entities at once. This is often useful for creating factories where you need to
 * register all entities with some class or resource.
 */
public abstract class AbstractAllEntitiesAwareGenerator extends AbstractGenerator {

    /**
     * Generates with all entities in the "entities" variable of the Velocity context.
     * 
     * {@inheritDoc}
     */
    public void generate(GenerationContext context) {
        VelocityContext vc = new VelocityContext();
        vc.put("prefix", context.getBasePackage());
        vc.put("basePackage", context.getBasePackage());

        DefaultModelInstanceRepository metamodelRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);

        Map<String, Entity> entityMap = metamodelRepository.getEntitiesByContext(metadataContext);
        Collection<Entity> entities = entityMap.values();
        vc.put("entities", entities);

        String fileName = context.getOutputFile();
        fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
        context.setOutputFile(fileName);

        generateFile(context, vc);
    }

}
